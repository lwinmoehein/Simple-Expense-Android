package lab.justonebyte.simpleexpense.ui.onboarding

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import lab.justonebyte.simpleexpense.data.SettingPrefRepository
import lab.justonebyte.simpleexpense.ui.components.SnackBarType
import javax.inject.Inject


data class LoginUiState(
    val currentSnackBar : SnackBarType? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val application: Context,
    private val settingsRepository: SettingPrefRepository
    ): ViewModel() {
    private val _viewModelUiState = MutableStateFlow(
        LoginUiState()
    )
    val viewModelUiState: StateFlow<LoginUiState>
        get() = _viewModelUiState

    private val token = mutableStateOf("")


    init {
        viewModelScope.launch {

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
            settingsRepository.updateIsAppOnboardingShowed(true)
        }
    }
}

