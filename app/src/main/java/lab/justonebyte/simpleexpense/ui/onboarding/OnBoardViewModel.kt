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
import lab.justonebyte.simpleexpense.ui.components.SnackBarType
import lab.justonebyte.simpleexpense.utils.RetrofitHelper
import lab.justonebyte.simpleexpense.utils.createIsOnboardDoneFlagFile
import lab.justonebyte.simpleexpense.workers.runVersionSync
import javax.inject.Inject


data class OnBoardUiState(
    val currentSnackBar : SnackBarType? = null,
    val isLoggingIn:Boolean = false,
    val firebaseUser: FirebaseUser? = null
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
                    settingsRepository.updateSelectedCurrency(it.data.currency)
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

