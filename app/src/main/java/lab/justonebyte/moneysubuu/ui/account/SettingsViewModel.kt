package lab.justonebyte.moneysubuu.ui.account

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import lab.justonebyte.moneysubuu.api.AuthService
import lab.justonebyte.moneysubuu.data.CategoryRepository
import lab.justonebyte.moneysubuu.data.SettingPrefRepository
import lab.justonebyte.moneysubuu.data.TransactionRepository
import lab.justonebyte.moneysubuu.model.*
import lab.justonebyte.moneysubuu.ui.components.SnackBarType
import lab.justonebyte.moneysubuu.utils.RetrofitHelper
import lab.justonebyte.moneysubuu.workers.runVersionSync
import javax.inject.Inject

data class SettingUiState(
    val selectedCurrency: Currency = Currency.Kyat,
    val defaultBalanceType: BalanceType = BalanceType.MONTHLY,
    val defaultLanguage:AppLocale = AppLocale.English,
    val currentSnackBar : SnackBarType? = null,
    val companionApps: AppList? = null
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingRepository: SettingPrefRepository,
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    @ApplicationContext private val application: Context
): ViewModel() {

    private val _viewModelUiState = MutableStateFlow(
        SettingUiState()
    )
    val viewModelUiState: StateFlow<SettingUiState>
        get() = _viewModelUiState

    val totalTransactions = mutableStateOf(listOf<Transaction>())


    init {
        viewModelScope.launch {
            launch { collectSelectedCurrency() }
            launch { collectDefaultBalanceType() }
            launch { collectLanguage() }
//            launch { getCompanionApps() }
            launch { getTransactions() }
            launch { collectToken() }
        }
    }
    private suspend fun getTransactions(){
        transactionRepository.getTotalTransactions().collect{ transactions->
          totalTransactions.value = transactions
        }
    }
    private suspend fun getCompanionApps(){
//        val companionAppService = RetrofitHelper.getInstance().create(AuthService::class.java)
//        // launching a new coroutine
//        GlobalScope.launch {
//          try{
//              val result = companionAppService.getAccessToken()
//              result.body()?.let {
//                  Log.i("access token:", it.data.token)
//                  settingRepository.updateToken(it.data.token)
//              }
//          }catch (e:Exception){
//
//          }
////            _viewModelUiState.update { it.copy(companionApps = result.body()) }
//        }
    }
    suspend fun fetchAccessTokenByGoogleId(googleId:String){
        Log.i("access token:fetch",googleId)
        val companionAppService = RetrofitHelper.getInstance("").create(AuthService::class.java)
        GlobalScope.launch {
            try{
                val result = companionAppService.getAccessToken(googleId)
                result.body()?.let {
                    Log.i("access token:", it.data.token)
                    settingRepository.updateToken(it.data.token)
                    runVersionSync(application,"categories",it.data.token)
                    runVersionSync(application,"categories",it.data.token)
                }
            }catch (e:Exception){
                Log.i("access token fail","cannot fetch access token.")
            }
        }
    }
    private suspend fun collectSelectedCurrency(){
        settingRepository.selectedCurrency.collect{
            _viewModelUiState.update { uiState->
                uiState.copy(
                    selectedCurrency = Currency.getFromValue(it)
                )
            }
        }
    }

    private suspend fun collectToken(){
        settingRepository.accessToken.collect{
            Log.i("pref:token:",it)
        }
    }
    private suspend fun collectLanguage(){
        settingRepository.selectedLocale.collect{
            _viewModelUiState.update { uiState->
                uiState.copy(
                    defaultLanguage = AppLocale.getFromValue(it)
                )
            }
        }
    }
    private suspend fun collectDefaultBalanceType(){
        settingRepository.defaultBalanceType.collect{
            _viewModelUiState.update { uiState->
                uiState.copy(
                    defaultBalanceType = when(it){
                        BalanceType.TOTAL.value->BalanceType.TOTAL
                        BalanceType.YEARLY.value->BalanceType.YEARLY
                        BalanceType.DAILY.value->BalanceType.DAILY
                        else->BalanceType.MONTHLY
                    }
                )
            }
        }
    }


    fun updateCurrency(currency: Currency){
        viewModelScope.launch {
            settingRepository.updateSelectedCurrency(currency.value)
        }
    }
    fun updateLocale(appLocale: AppLocale){
        viewModelScope.launch {
            settingRepository.updateLocale(appLocale.value)
        }
    }

    suspend fun logOut(){
        viewModelScope.launch {
            transactionRepository.deleteAll()
            categoryRepository.deleteAll()
            settingRepository.updateToken("")
            categoryRepository.populateCategories()
        }
    }

    fun updateDefaultBalanceType(balanceType: BalanceType){
        viewModelScope.launch {
            settingRepository.updateBalanceType(balanceType.value)
        }
    }
    fun showSnackBar(type: SnackBarType){
        _viewModelUiState.update {
            it.copy(currentSnackBar = type)
        }
    }
    fun clearSnackBar(){
        _viewModelUiState.update {
            it.copy(currentSnackBar = null)
        }
    }
    fun exportDate(from:String,to:String,format:FileFormat){

    }

}