package lab.justonebyte.simpleexpense.ui.home

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import lab.justonebyte.simpleexpense.api.AuthService
import lab.justonebyte.simpleexpense.api.ExportService
import lab.justonebyte.simpleexpense.data.CategoryRepository
import lab.justonebyte.simpleexpense.data.SettingPrefRepository
import lab.justonebyte.simpleexpense.data.TransactionRepository
import lab.justonebyte.simpleexpense.model.BalanceType
import lab.justonebyte.simpleexpense.model.ShowCase
import lab.justonebyte.simpleexpense.model.Transaction
import lab.justonebyte.simpleexpense.model.TransactionCategory
import lab.justonebyte.simpleexpense.model.TransactionType
import lab.justonebyte.simpleexpense.ui.components.SnackBarType
import lab.justonebyte.simpleexpense.utils.RetrofitHelper
import lab.justonebyte.simpleexpense.utils.getCurrentDay
import lab.justonebyte.simpleexpense.utils.getCurrentMonth
import lab.justonebyte.simpleexpense.utils.getCurrentYear
import lab.justonebyte.simpleexpense.workers.runVersionSync
import java.util.Currency
import java.util.UUID
import javax.inject.Inject

data class HomeUiState(
    val isAppAlreadyIntroduced:Boolean?=null,
    val isOnboardingShowed:Boolean=false,
    val currentAppShowcaseStep:ShowCase?=null,
    val currentBalance:Long,
    val incomeBalance:Long,
    val expenseBalance:Long,
    val totalBalance:Long,
    val currentBalanceType:BalanceType = BalanceType.MONTHLY,
    val currentCurrency: Currency = Currency.getInstance("USD"),
    val categories:List<TransactionCategory>  = emptyList(),
    val transactions:List<Transaction> = emptyList(),
    val currentSnackBar : SnackBarType? = null,
    val selectedDay:String = getCurrentDay(),
    val selectedMonth:String = getCurrentMonth(),
    val selectedYear:String = getCurrentYear(),
    val isTransactionsLoading:Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val settingsRepository: SettingPrefRepository,
    @ApplicationContext private val application: Context
):ViewModel()
{
    private var transactionCollectJobs:Job? = null

    private val _viewModelUiState  = MutableStateFlow(
        HomeUiState(currentBalance = 0, incomeBalance = 0, expenseBalance = 0, totalBalance = 0)
    )
    private var token = mutableStateOf("")

    val viewModelUiState: StateFlow<HomeUiState>
        get() =  _viewModelUiState


    init {
        viewModelScope.launch {
            launch {
                updateIsOnboardingShowed()
            }
            launch {
                collectAndUpdateIsAppOnboardingShowed()
            }
            launch {
                collectAndUpdateIsAppIntroduced()
            }
            launch {
                collectToken()
            }
            launch {
                collectCategories()
            }
            launch {
                bindTransactionsFromBalanceType(_viewModelUiState.value.currentBalanceType)
            }
            launch {
                fetchCurrencyFromAPI()
            }
            launch {
                collectCurrencyFromSetting()
            }
        }
    }

    private suspend fun fetchCurrencyFromAPI() {
        val exportService =
            RetrofitHelper.getInstance(token.value).create(AuthService::class.java)
        val response = exportService.getProfile()
        response.body()?.data?.currency?.let { settingsRepository.updateSelectedCurrency(it) }
    }

    private suspend fun updateIsOnboardingShowed() {
        _viewModelUiState.update { uiState->
            uiState.copy(
                isOnboardingShowed = settingsRepository.isAppOnboardingShowed.last()
            )
        }
    }

    private suspend fun collectAndUpdateIsAppOnboardingShowed(){
        settingsRepository.isAppOnboardingShowed.collect{isShowed->
            _viewModelUiState.update {
                it.copy(isOnboardingShowed = isShowed)
            }
        }
    }

    private suspend fun collectAndUpdateIsAppIntroduced(){
            settingsRepository.isAppIntroduced.collect{isIntroduced->
                _viewModelUiState.update {
                    it.copy(isAppAlreadyIntroduced = isIntroduced)
                }
                if(!isIntroduced){
                    updateAppIntroStep(ShowCase.ADD_TRANSACTION)
                }
                Log.i("isIntroduced:",isIntroduced.toString())
            }
    }
    private suspend fun collectToken() {
        settingsRepository.accessToken.collect{
            if(it.isNotEmpty()){
                token.value = it
                viewModelScope.launch {
                    runVersionSync(application,"categories",token.value)
                }
                viewModelScope.launch {
                    runVersionSync(application,"transactions",token.value)
                }
            }
        }
    }

    suspend fun collectTotalBalance(){
        _viewModelUiState.update {
            it.copy(currentBalanceType = BalanceType.TOTAL)
        }
            transactionRepository.getTotalTransactions().collect{ transactions->
                if(viewModelUiState.value.currentBalanceType==BalanceType.TOTAL) {
                    bindBalanceData(transactions)
                }
        }
    }
    private suspend fun collectCurrencyFromSetting(){
        settingsRepository.selectedCurrency.collect{
            _viewModelUiState.update { homeUiState ->
                homeUiState.copy(currentCurrency = Currency.getInstance(it))
            }
        }
    }
     suspend fun bindTransactionsFromBalanceType(balanceType: BalanceType=viewModelUiState.value.currentBalanceType){

         _viewModelUiState.update { homeUiState ->
             homeUiState.copy(
                 currentBalanceType = balanceType,
                 isTransactionsLoading = true
             )
         }

         transactionCollectJobs?.cancel()

         transactionCollectJobs = viewModelScope.launch {
             when(balanceType){
                 BalanceType.DAILY->collectDailyBalance()
                 BalanceType.MONTHLY->collectMonthlyBalance()
                 BalanceType.YEARLY->collectYearlyBalance()
                 else->collectTotalBalance()
             }

         }
     }
     suspend fun updateCurrentDay(dateValue:String = viewModelUiState.value.selectedDay){
         _viewModelUiState.update {
             it.copy(selectedDay = dateValue, currentBalanceType = BalanceType.DAILY)
         }
         viewModelScope.launch {
             bindTransactionsFromBalanceType()
         }
     }
     suspend fun collectDailyBalance(){
             transactionRepository.getDailyTransactions(viewModelUiState.value.selectedDay).collect{ transactions->
                if(viewModelUiState.value.currentBalanceType==BalanceType.DAILY) {
                    bindBalanceData(transactions)
                }
        }
    }

    suspend fun updateCurrentMonth(monthValue:String =  viewModelUiState.value.selectedMonth){
        _viewModelUiState.update {
            it.copy(selectedMonth = monthValue, currentBalanceType = BalanceType.MONTHLY)
        }
        viewModelScope.launch {
            bindTransactionsFromBalanceType()
        }
    }

     suspend fun collectMonthlyBalance(){
            transactionRepository.getMonthlyTransactions(viewModelUiState.value.selectedMonth).collect{ transactions->
                if(viewModelUiState.value.currentBalanceType==BalanceType.MONTHLY) {
                    bindBalanceData(transactions)
                }
        }
    }

    suspend fun updateCurrentYear(year:String= viewModelUiState.value.selectedYear){
        _viewModelUiState.update {
            it.copy(selectedYear = year, currentBalanceType = BalanceType.YEARLY)
        }
        viewModelScope.launch {
            bindTransactionsFromBalanceType()
        }

    }
     suspend fun collectYearlyBalance(){
            transactionRepository.getYearlyTransactions(viewModelUiState.value.selectedYear).collect{ transactions->
                if(viewModelUiState.value.currentBalanceType==BalanceType.YEARLY){
                    bindBalanceData(transactions)
                }
        }
    }

    private fun bindBalanceData(transactions: List<Transaction>) {
        val income = transactions.filter { it.type==TransactionType.Income }.sumOf{ it.amount }.toLong()
        val expense = transactions.filter { it.type==TransactionType.Expense }.sumOf { it.amount }.toLong()
        val sum = income-expense

        _viewModelUiState.update {
            it.copy(
                currentBalance = sum,
                incomeBalance = income,
                expenseBalance = expense,
                transactions = transactions,
                isTransactionsLoading = false
            )
        }
    }

    private suspend fun collectCategories(){
        categoryRepository.getCategories().collect{ categories->
            _viewModelUiState.update { it.copy(categories = categories) }
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

    fun addTransaction(
        transactionCategory: TransactionCategory,
        amount:Int,
        type:Int,
        date:Long,
        note:String?
    ){

        viewModelScope.launch {
            transactionRepository.insert(
                Transaction(
                    unique_id = UUID.randomUUID().toString(),
                    amount = amount,
                    type = if(type==TransactionType.Income.value) TransactionType.Income else TransactionType.Expense,
                    category = transactionCategory,
                    created_at = date,
                    updated_at = System.currentTimeMillis(),
                    note = note
                )
            )
            _viewModelUiState.update { it.copy(currentSnackBar = SnackBarType.ADD_TRANSACTION_SUCCESS) }

            if(token.value.isNotEmpty()) runVersionSync(application,"transactions",token.value)
        }
        viewModelScope.launch {
            bindTransactionsFromBalanceType(_viewModelUiState.value.currentBalanceType)
        }
    }
    fun updateTransaction(
        transactionId:String,
        transactionCategory: TransactionCategory,
        amount:Int,
        type:Int,
        date:Long,
        note:String?
    ){
        viewModelScope.launch {
            transactionRepository.update(
                Transaction(
                    unique_id = transactionId,
                    amount = amount,
                    type = if(type==1) TransactionType.Income else TransactionType.Expense,
                    category = transactionCategory,
                    created_at = date,
                    updated_at = System.currentTimeMillis(),
                    note = note
                )
            )
            _viewModelUiState.update { it.copy(currentSnackBar = SnackBarType.UPDATE_TRANSACTION_SUCCESS) }
            bindTransactionsFromBalanceType(_viewModelUiState.value.currentBalanceType)
            if(token.value.isNotEmpty()) runVersionSync(application,"transactions",token.value)
        }
    }

    fun deleteTransaction(transaction: Transaction){

        viewModelScope.launch {
            transactionRepository.delete(transaction)
            _viewModelUiState.update { it.copy(currentSnackBar = SnackBarType.DELETE_TRANSACTION_SUCCESS) }

            bindTransactionsFromBalanceType(_viewModelUiState.value.currentBalanceType)

            if(token.value.isNotEmpty()) runVersionSync(application,"transactions",token.value)
        }
    }

    fun addCategory(transactionCategory:TransactionCategory){
        viewModelScope.launch {
            categoryRepository.insert(transactionCategory = transactionCategory)
            if(token.value.isNotEmpty()) runVersionSync(application,"categories",token.value)
        }
    }

    fun updateAppIntroStep(showCase: ShowCase){
        _viewModelUiState.update { it.copy(currentAppShowcaseStep = showCase) }
    }
    suspend fun changeIsAppIntroducedToTrue(){
       viewModelScope.launch {
           settingsRepository.updateIsAppIntroduced(true)
       }
    }
}