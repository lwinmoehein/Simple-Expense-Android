package lab.justonebyte.simpleexpense.ui.account

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.api.AuthService
import lab.justonebyte.simpleexpense.api.BetweenPostData
import lab.justonebyte.simpleexpense.api.ExportService
import lab.justonebyte.simpleexpense.api.UpdateProfilePostData
import lab.justonebyte.simpleexpense.data.CategoryRepository
import lab.justonebyte.simpleexpense.data.SettingPrefRepository
import lab.justonebyte.simpleexpense.data.TransactionRepository
import lab.justonebyte.simpleexpense.model.AppList
import lab.justonebyte.simpleexpense.model.BalanceType
import lab.justonebyte.simpleexpense.ui.components.SnackBarType
import lab.justonebyte.simpleexpense.utils.RetrofitHelper
import lab.justonebyte.simpleexpense.utils.getDecodedPath
import lab.justonebyte.simpleexpense.workers.CURRENCY_CODE
import lab.justonebyte.simpleexpense.workers.GetVersionInfoWorker
import lab.justonebyte.simpleexpense.workers.KEY_TABLE_NAME
import lab.justonebyte.simpleexpense.workers.OBJECTS_STRING
import lab.justonebyte.simpleexpense.workers.TOKEN
import lab.justonebyte.simpleexpense.workers.UpdateCurrencyWorker
import lab.justonebyte.simpleexpense.workers.runVersionSync
import java.util.Currency
import java.util.concurrent.TimeUnit
import javax.inject.Inject

data class SettingUiState(
    val selectedCurrency: Currency = Currency.getInstance("USD"),
    val defaultBalanceType: BalanceType = BalanceType.MONTHLY,
    val currentSnackBar : SnackBarType? = null,
    val companionApps: AppList? = null,
    val downloadFolder:String? = null,
    val readableDownloadFolder:String? = null,
    val isExportingFile:Boolean = false,
    val isLoggingIn:Boolean = false,
    val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
)

data class FileExportRequest(
    val from:String,
    val to:String,
    val format:FileFormat,
    val isAdShown:Boolean = false
)



@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingRepository: SettingPrefRepository,
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    @ApplicationContext private val application: Context
): ViewModel() {
    val TAG = "Ad"

    private var _rewardedAd: MutableStateFlow<RewardedAd?> = MutableStateFlow(null)
    private var _isAdLoading:MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var _cacheFileExportRequest:MutableStateFlow<FileExportRequest?> = MutableStateFlow(null)
    val rewardedAd:StateFlow<RewardedAd?>
        get() = _rewardedAd

    val isAdLoading:StateFlow<Boolean>
        get() = _isAdLoading

    private val _viewModelUiState = MutableStateFlow(
        SettingUiState()
    )
    val viewModelUiState: StateFlow<SettingUiState>
        get() = _viewModelUiState

    private var token = mutableStateOf("")


    init {
        viewModelScope.launch {
            launch { collectSelectedCurrency() }
            launch { collectDefaultBalanceType() }
            launch { getTransactions() }
            launch { collectToken() }
            launch { collectDownloadFolder() }
        }
    }
    private suspend fun getTransactions(){
        transactionRepository.getTotalTransactions().collect{ transactions->
        }
    }

    suspend fun fetchAccessTokenByGoogleId(googleId:String){
        _viewModelUiState.update {
            it.copy(isLoggingIn = true)
        }

        val authService = RetrofitHelper.getInstance("").create(AuthService::class.java)

        viewModelScope.launch {
            try{
                val result = authService.getAccessToken(googleId)
                result.body()?.let {
                    Log.i("access token:", it.data.token)
                    settingRepository.updateToken(it.data.token)
                    launch {
                        runVersionSync(application,"categories",it.data.token)
                    }
                    launch {
                        runVersionSync(application,"transactions",it.data.token)
                    }
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
                        firebaseUser =  null,
                        currentSnackBar = SnackBarType.LOGIN_ERROR
                    )
                }
            }
        }
    }
    private suspend fun collectSelectedCurrency(){
        settingRepository.selectedCurrency.collect{
            _viewModelUiState.update { uiState->
                uiState.copy(
                    selectedCurrency = Currency.getInstance(it)
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


    fun updateCurrency(currency: Currency) {
        viewModelScope.launch {
            settingRepository.updateSelectedCurrency(currency.currencyCode)

            val updateCurrencyWorker =   OneTimeWorkRequest.Builder(UpdateCurrencyWorker::class.java)
                .setInputData(
                    Data.Builder().putString(TOKEN,token.value).putString(
                        CURRENCY_CODE,currency.currencyCode).build()
                )
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    OneTimeWorkRequest.MIN_BACKOFF_MILLIS,TimeUnit.MILLISECONDS
                )
                .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
                .build()

            WorkManager.getInstance(application)
                .beginUniqueWork("update_currency", ExistingWorkPolicy.REPLACE, updateCurrencyWorker)
                .enqueue()
        }
    }
    suspend fun logOut(){
        viewModelScope.launch {
            _viewModelUiState.update { uiState->
                uiState.copy(
                    firebaseUser = null,
                    currentSnackBar = SnackBarType.LOGOUT_SUCCESS
                )
            }
            transactionRepository.deleteAll()
            categoryRepository.deleteAll()
            settingRepository.updateToken("")
            settingRepository.updateSelectedCurrency("USD")
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

    fun cacheExportDates(from:String, to:String, format:FileFormat){
        _cacheFileExportRequest.value = FileExportRequest(from,to,format)
    }

    fun exportFileFromCacheRequest(){
        val cachedExportRequest = _cacheFileExportRequest.value
        if(cachedExportRequest!=null) exportDataAsFile(cachedExportRequest.from,cachedExportRequest.to,cachedExportRequest.format)
        _cacheFileExportRequest.value = null
    }

    fun cacheExportRequestAndShowAd(from:String, to:String, format:FileFormat){
        val downloadFolderUriString = _viewModelUiState.value.downloadFolder
        if(!downloadFolderUriString.isNullOrEmpty()) {
            val uri = Uri.parse(_viewModelUiState.value.downloadFolder)
            val file = DocumentFile.fromTreeUri(application, uri)

            if (file != null && file.canRead() && file.canWrite()) {
                cacheExportDates(from, to, format)
                showAd()
            }
        }else{
            showSnackBar(SnackBarType.SELECT_CORRECT_DOWNLOAD_FOLDER)
        }
    }


    fun exportDataAsFile(from:String, to:String, format:FileFormat){
            when(format.nameId){
                R.string.excel_format-> generateExcelFile(from,to)
                else->generatePDFFile(from,to)
            }
    }

    private fun generatePDFFile(from: String, to:String ) {
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
                        downloadAndSaveFile(file, pdf,from,to)
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
                            downloadAndSaveFile(file, excel,from,to)
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
    private suspend fun downloadAndSaveFile(uriFile:DocumentFile,format:FileFormat,from:String,to:String){

        try {
            val exportService =
                RetrofitHelper.getInstance(token.value).create(ExportService::class.java)

            val response = if (format === excel) exportService.generateExcelFile(
                BetweenPostData(
                    from,
                    to
                )
            ) else exportService.generatePDFFile(BetweenPostData(from, to))

            val responseBody = response.body()

            val contentDispositionHeader = response.headers().get("Content-Disposition")
            val fileName = contentDispositionHeader?.let {
                // Extract filename from header value (example: "attachment; filename=example.pdf")
                val filenameRegex = "filename=(.+)".toRegex()
                filenameRegex.find(it)?.groupValues?.get(1)?.replace("\"", "")
            }


            var file: DocumentFile? = null

            file = if (format === excel) {
                uriFile.createFile(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    fileName ?: "simple_expense.xlsx"
                )
            } else {
                uriFile.createFile("application/pdf", fileName ?: "simple_expense.pdf")
            }

            if (file != null) {
                val outputStream = application.contentResolver.openOutputStream(file.uri)
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
                Log.i("Folder:", "file is null.")

                showSnackBar(SnackBarType.FILE_EXPORT_FAILED)
                _viewModelUiState.update {
                    it.copy(isExportingFile = false)
                }

            }
        }catch (e:Exception){
            showSnackBar(SnackBarType.CONNECTION_ERROR)
            _viewModelUiState.update {
                it.copy(isExportingFile = false)
            }
        }
    }

    private suspend fun updateDownloadFolder(folderUri:String){
        settingRepository.updateDownloadFolder(folderUri)
    }

    fun showAd(){
        _isAdLoading.value = true

        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(application,"ca-app-pub-3940256099942544/5224354917", adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                _rewardedAd.value = null
                _rewardedAd.value = null
                exportFileFromCacheRequest()
            }

            override fun onAdLoaded(ad: RewardedAd) {
                _rewardedAd.value = ad
                ad.fullScreenContentCallback = object: FullScreenContentCallback() {
                    override fun onAdClicked() {
                        // Called when a click is recorded for an ad.
                        Log.d(TAG, "Ad was clicked.")
                    }

                    override fun onAdDismissedFullScreenContent() {
                        // Called when ad is dismissed.
                        // Set the ad reference to null so you don't show the ad a second time.
                        Log.d(TAG, "Ad dismissed fullscreen content.")
                        _rewardedAd.value = null
                        if(_cacheFileExportRequest.value?.isAdShown == true){
                            exportFileFromCacheRequest()
                        }
                    }


                    override fun onAdImpression() {
                        // Called when an impression is recorded for an ad.
                        Log.d(TAG, "Ad recorded an impression.")
                         _cacheFileExportRequest.update { it?.copy(isAdShown = true) }
                    }

                    override fun onAdShowedFullScreenContent() {
                        // Called when ad is shown.
                        Log.d(TAG, "Ad showed fullscreen content.")
                    }
                }

            }
        })
    }
    fun changeIsAdLoadingToFalse(){
        _isAdLoading.value = false
    }

}