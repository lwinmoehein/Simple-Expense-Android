package lab.justonebyte.simpleexpense.ui.account

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.api.AuthService
import lab.justonebyte.simpleexpense.data.CategoryRepository
import lab.justonebyte.simpleexpense.data.SettingPrefRepository
import lab.justonebyte.simpleexpense.data.TransactionRepository
import lab.justonebyte.simpleexpense.model.AppList
import lab.justonebyte.simpleexpense.model.AppLocale
import lab.justonebyte.simpleexpense.model.BalanceType
import lab.justonebyte.simpleexpense.model.Currency
import lab.justonebyte.simpleexpense.model.Transaction
import lab.justonebyte.simpleexpense.ui.components.SnackBarType
import lab.justonebyte.simpleexpense.utils.RetrofitHelper
import lab.justonebyte.simpleexpense.utils.getDecodedPath
import lab.justonebyte.simpleexpense.workers.runVersionSync
import okhttp3.ResponseBody
import javax.inject.Inject

data class SettingUiState(
    val selectedCurrency: Currency = Currency.Kyat,
    val defaultBalanceType: BalanceType = BalanceType.MONTHLY,
    val defaultLanguage:AppLocale = AppLocale.English,
    val currentSnackBar : SnackBarType? = null,
    val companionApps: AppList? = null,
    val downloadFolder:String? = null,
    val readableDownloadFolder:String? = null
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

    private var token = mutableStateOf("")



    init {
        viewModelScope.launch {
            launch { collectSelectedCurrency() }
            launch { collectDefaultBalanceType() }
            launch { collectLanguage() }
//            launch { getCompanionApps() }
            launch { getTransactions() }
            launch { collectToken() }
            launch { collectDownloadFolder() }
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
            token.value = it
        }
    }
    private suspend fun collectDownloadFolder(){
        settingRepository.downloadFolder.collect{
            _viewModelUiState.update { uiState->
                uiState.copy(
                    downloadFolder = it
                )
            }
            if(it.isNotEmpty()){
                val uri = Uri.parse(it)
                _viewModelUiState.update { uiState->
                    uiState.copy(
                        readableDownloadFolder = uri.getDecodedPath()
                    )
                }
            }
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
    fun exportDate(from:String, to:String, format:FileFormat){
        viewModelScope.launch {
            when(format.nameId){
                R.string.excel_format-> generateExcelFile(from,to)
                else->generatePDFFile()
            }
        }
    }

    private fun generatePDFFile() {
        TODO("Not yet implemented")
    }


    private suspend fun generateExcelFile(from: String, to:String ) {
       TODO("Not yet ")
    }

    private suspend fun updateDownloadFolder(folderUri:String){
        settingRepository.updateDownloadFolder(folderUri)
    }

}