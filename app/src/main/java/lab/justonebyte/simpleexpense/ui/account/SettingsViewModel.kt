package lab.justonebyte.simpleexpense.ui.account

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lab.justonebyte.simpleexpense.api.AuthService
import lab.justonebyte.simpleexpense.data.CategoryRepository
import lab.justonebyte.simpleexpense.data.SettingPrefRepository
import lab.justonebyte.simpleexpense.data.TransactionRepository
import lab.justonebyte.simpleexpense.model.*
import lab.justonebyte.simpleexpense.ui.components.SnackBarType
import lab.justonebyte.simpleexpense.utils.RetrofitHelper
import lab.justonebyte.simpleexpense.workers.runVersionSync
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.api.BetweenPostData
import lab.justonebyte.simpleexpense.api.ExportService
import okhttp3.ResponseBody
import java.io.File


import javax.inject.Inject

data class SettingUiState(
    val selectedCurrency: Currency = Currency.Kyat,
    val defaultBalanceType: BalanceType = BalanceType.MONTHLY,
    val defaultLanguage:AppLocale = AppLocale.English,
    val currentSnackBar : SnackBarType? = null,
    val companionApps: AppList? = null
)
const val CREATE_FILE = 1

private sealed class DownloadState {
    data class Downloading(val progress: Int) : DownloadState()
    object Finished : DownloadState()
    data class Failed(val error: Throwable? = null) : DownloadState()
}

private fun ResponseBody.saveFile(): Flow<DownloadState> {
    return flow{
        emit(DownloadState.Downloading(0))
        val destinationFile = File("Downloads")

        try {
            byteStream().use { inputStream->
                destinationFile.outputStream().use { outputStream->
                    val totalBytes = contentLength()
                    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                    var progressBytes = 0L
                    var bytes = inputStream.read(buffer)
                    while (bytes >= 0) {
                        outputStream.write(buffer, 0, bytes)
                        progressBytes += bytes
                        bytes = inputStream.read(buffer)
                        emit(DownloadState.Downloading(((progressBytes * 100) / totalBytes).toInt()))
                    }
                }
            }
            emit(DownloadState.Finished)
        } catch (e: Exception) {
            emit(DownloadState.Failed(e))
        }
    }
        .flowOn(Dispatchers.IO).distinctUntilChanged()
}

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
    private val _downloadedFile : MutableStateFlow<ResponseBody?> = MutableStateFlow(null)

    val downloadedFile :StateFlow<ResponseBody?>
        get() = _downloadedFile
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
    @RequiresApi(Build.VERSION_CODES.O)
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
    private fun hasPermissions(vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (checkSelfPermission(application,permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun generateExcelFile(from: String, to:String ) {
        if(token.value.isNotEmpty()) {
            val exportService = RetrofitHelper.getInstance(token.value).create(ExportService::class.java)
            Log.i("file:","got file")

            viewModelScope.launch {
                val responseBody=exportService.generateExcelFile(BetweenPostData(from,to)).body()
                saveFile(responseBody)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun saveFile(body: ResponseBody?) {
        withContext(Dispatchers.IO) {
            if(body === null) {
                Log.i("file","response body is null")
            }

            body?.saveFile()
                ?.collect{ downloadState->
                    when (downloadState) {
                        is DownloadState.Downloading -> {
                            _downloadedFile.update { body }
                            Log.d("myTag", "progress=${downloadState.progress}")
                        }


                        DownloadState.Finished -> {
//                            _downloadedFile.update { "Done" }
                        }

                        else -> {}
                    }
                }
        }
    }


}