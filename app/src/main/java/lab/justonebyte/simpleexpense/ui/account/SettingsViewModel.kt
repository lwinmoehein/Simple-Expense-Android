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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.api.AuthService
import lab.justonebyte.simpleexpense.api.BetweenPostData
import lab.justonebyte.simpleexpense.api.ExportService
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
    val readableDownloadFolder:String? = null,
    val isExportingFile:Boolean = false
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
            when(format.nameId){
                R.string.excel_format-> generateExcelFile(from,to)
                else->generatePDFFile()
            }
    }

    private fun generatePDFFile() {

    }


    private fun generateExcelFile(from: String, to:String ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _viewModelUiState.update {
                    it.copy(isExportingFile = true)
                }

                val downloadFolderUriString = _viewModelUiState.value.downloadFolder
                if(!downloadFolderUriString.isNullOrEmpty()) {
                    val uri = Uri.parse(_viewModelUiState.value.downloadFolder)
                    val file = DocumentFile.fromTreeUri(application, uri)

                    if (file != null && file.canRead() && file.canWrite()) {
                        Log.i("Folder:main:w:", file.canWrite().toString())
                        Log.i("Folder:main:r:", file.canRead().toString())

                        val exportService =
                            RetrofitHelper.getInstance(token.value).create(ExportService::class.java)
                        val response = exportService.generateExcelFile(BetweenPostData(from, to))
                        Log.i("file:", "downloaded file:" + response.headers())
                        val responseBody = response.body()

                        val pdfFile = file.createFile("application/pdf", "my_pdf.pdf")

                        if (pdfFile != null) {
                            val outputStream = application.contentResolver.openOutputStream(pdfFile.uri)
                            if (outputStream != null) {
                                responseBody?.byteStream()?.use { inputStream ->
                                    inputStream.copyTo(outputStream)
                                }
                                outputStream.close()
                                showSnackBar(SnackBarType.FILE_EXPORT_SUCCESS)
                                _viewModelUiState.update {
                                    it.copy(isExportingFile = false)
                                }

                            } else {
                                Log.i("Folder:", "Output stream is null.")
                                showSnackBar(SnackBarType.FILE_EXPORT_FAILED)
                                _viewModelUiState.update {
                                    it.copy(isExportingFile = false)
                                }

                            }
                        } else {
                            Log.i("Folder:", "PDF file is null.")

                            showSnackBar(SnackBarType.FILE_EXPORT_FAILED)
                            _viewModelUiState.update {
                                it.copy(isExportingFile = false)
                            }

                        }
                    } else {
                        showSnackBar(SnackBarType.SELECT_CORRECT_DOWNLOAD_FOLDER)
                        _viewModelUiState.update {
                            it.copy(isExportingFile = false)
                        }

                        Log.i("Folder:", "Have no access to download folder.")
                    }
                }else{
                    showSnackBar(SnackBarType.SELECT_CORRECT_DOWNLOAD_FOLDER)
                    _viewModelUiState.update {
                        it.copy(isExportingFile = false)
                    }
                }
            }
        }
    }

    private suspend fun updateDownloadFolder(folderUri:String){
        settingRepository.updateDownloadFolder(folderUri)
    }

}