package lab.justonebyte.simpleexpense.ui.onboarding

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import lab.justonebyte.simpleexpense.api.AuthService
import lab.justonebyte.simpleexpense.data.SettingPrefRepository
import lab.justonebyte.simpleexpense.model.AppLocale
import lab.justonebyte.simpleexpense.ui.components.SnackBarType
import lab.justonebyte.simpleexpense.utils.RetrofitHelper
import lab.justonebyte.simpleexpense.utils.createIsOnboardDoneFlagFile
import lab.justonebyte.simpleexpense.workers.runVersionSync
import javax.inject.Inject


data class OnBoardUiState(
    val currentSnackBar : SnackBarType? = null,
    val isLoggingIn:Boolean = false,
    val firebaseUser: FirebaseUser? = null,
    val currentLocale: AppLocale = AppLocale.English
)

@HiltViewModel
class OnBoardViewModel @Inject constructor(
    @ApplicationContext private val application: Context,
    private val settingsRepository: SettingPrefRepository
    ): ViewModel() {
    private val _viewModelUiState = MutableStateFlow(
        OnBoardUiState()
    )
    val viewModelUiState: StateFlow<OnBoardUiState>
        get() = _viewModelUiState

    private val token = mutableStateOf("")

    init {
        viewModelScope.launch {
            collectLocale()
        }
    }

    private suspend fun collectLocale(){
        settingsRepository.selectedLocale.collect{
            _viewModelUiState.update { uiState->
                uiState.copy(
                    currentLocale = AppLocale.getFromValue(it)
                )
            }
        }
    }

    private suspend fun collectToken() {
        settingsRepository.accessToken.collect{
            token.value = it
        }
    }

    fun showSnackBar(type:SnackBarType){
        _viewModelUiState.update {
            it.copy(currentSnackBar = type)
        }
    }
    fun clearSnackBar(){
        _viewModelUiState.update {
            it.copy(currentSnackBar = null)
        }
    }

    suspend fun changeIsAppOnboardingShowed(){
        viewModelScope.launch {
            createIsOnboardDoneFlagFile(application)
            settingsRepository.updateIsAppOnboardingShowed(true)
        }
    }

    fun changeLocale (locale: AppLocale){
        viewModelScope.launch {
            settingsRepository.updateLocale(locale.value)
        }
    }

    suspend fun fetchAccessTokenByGoogleId(googleId:String){
        _viewModelUiState.update {
            it.copy(isLoggingIn = true)
        }
        Log.i("access token:fetch",googleId)
        val authService = RetrofitHelper.getInstance("").create(AuthService::class.java)
        viewModelScope.launch {
            try{
                val result = authService.getAccessToken(googleId)
                result.body()?.let {
                    Log.i("access token:", it.data.token)
                    settingsRepository.updateToken(it.data.token)
                    runVersionSync(application,"categories",it.data.token)
                    runVersionSync(application,"transactions",it.data.token)
                    _viewModelUiState.update {uiState->
                        uiState.copy(
                            isLoggingIn = false,
                            firebaseUser =  FirebaseAuth.getInstance().currentUser,
                            currentSnackBar = SnackBarType.LOGIN_SUCCESS
                        )
                    }
                }
            }catch (e:Exception){
                Log.i("access token fail","cannot fetch access token.")
                Firebase.auth.signOut()
                GoogleSignIn
                    .getClient(application, GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .signOut()
                _viewModelUiState.update {
                    it.copy(
                        isLoggingIn = false,
                        currentSnackBar = SnackBarType.LOGIN_ERROR,
                        firebaseUser = null
                    )
                }
            }
        }
    }

}

