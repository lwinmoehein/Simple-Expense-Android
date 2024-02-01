package lab.justonebyte.simpleexpense.ui.stats

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import lab.justonebyte.simpleexpense.data.CategoryRepository
import lab.justonebyte.simpleexpense.data.SettingPrefRepository
import lab.justonebyte.simpleexpense.data.TransactionRepository
import lab.justonebyte.simpleexpense.model.BalanceType
import lab.justonebyte.simpleexpense.model.Transaction
import lab.justonebyte.simpleexpense.model.TransactionCategory
import lab.justonebyte.simpleexpense.model.TransactionType
import lab.justonebyte.simpleexpense.ui.components.SnackBarType
import lab.justonebyte.simpleexpense.utils.getCurrentDay
import lab.justonebyte.simpleexpense.utils.getCurrentMonth
import lab.justonebyte.simpleexpense.utils.getCurrentYear
import java.util.Currency
import javax.inject.Inject


data class StatsUiState(
    val categories:List<TransactionCategory>  = emptyList(),
    val transactions:List<Transaction> = emptyList(),
    val currentBalanceType: BalanceType = BalanceType.MONTHLY,
    val currentCurrency: Currency = Currency.getInstance("USD"),
    val currentSnackBar : SnackBarType? = null,
    val selectedDay:String = getCurrentDay(),
    val selectedMonth:String = getCurrentMonth(),
    val selectedYear:String = getCurrentYear()
)

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val settingsRepository: SettingPrefRepository
): ViewModel() {
    private val _viewModelUiState = MutableStateFlow(
        StatsUiState()
    )
    val viewModelUiState: StateFlow<StatsUiState>
        get() = _viewModelUiState


    init {

        viewModelScope.launch {
            launch {
                collectCategories()
            }
            launch {
                bindTransactionsFromBalanceType(_viewModelUiState.value.currentBalanceType)
            }
            launch {
                collectCurrencyFromSetting()
            }
        }
    }
    fun collectTotalBalance(){
        _viewModelUiState.update {
            it.copy(currentBalanceType = BalanceType.TOTAL)
        }
        viewModelScope.launch {
            transactionRepository.getTotalTransactions().collect{ transactions->
                Log.i("stats:trans",transactions.size.toString())
                bindBalanceData(transactions)
            }
        }
    }
    private suspend fun collectBalanceTypeFromSetting(){
        settingsRepository.defaultBalanceType.collect{
            bindTransactionsFromBalanceType(BalanceType.getFromValue(it))
        }
    }
    private suspend fun collectCurrencyFromSetting(){
        settingsRepository.selectedCurrency.collect{
            _viewModelUiState.update { homeUiState ->
                homeUiState.copy(currentCurrency = Currency.getInstance(it))
            }
        }
    }
    private fun bindTransactionsFromBalanceType(balanceType: BalanceType){
        viewModelScope.launch {
            when(balanceType){
                BalanceType.DAILY->collectDailyBalance()
                BalanceType.MONTHLY->collectMonthlyBalance()
                BalanceType.YEARLY->collectYearlyBalance()
                else->collectTotalBalance()
            }
        }
    }
    fun collectDailyBalance(dateValue:String = viewModelUiState.value.selectedDay){
        _viewModelUiState.update {
            it.copy(selectedDay = dateValue, currentBalanceType = BalanceType.DAILY)
        }

        viewModelScope.launch {
            transactionRepository.getDailyTransactions(dateValue).collect{ transactions->
                bindBalanceData(transactions)
            }
        }
    }

    fun collectMonthlyBalance(dateValue:String=  viewModelUiState.value.selectedMonth){
        _viewModelUiState.update {
            it.copy(selectedMonth = dateValue, currentBalanceType = BalanceType.MONTHLY)
        }
        viewModelScope.launch {
            transactionRepository.getMonthlyTransactions(dateValue).collect{ transactions->
                bindBalanceData(transactions)
            }
        }
    }

    fun collectYearlyBalance(dateValue:String= viewModelUiState.value.selectedYear){
        _viewModelUiState.update {
            it.copy(selectedYear = dateValue, currentBalanceType = BalanceType.YEARLY)
        }
        viewModelScope.launch {
            transactionRepository.getYearlyTransactions(dateValue).collect{ transactions->
                bindBalanceData(transactions)
            }
        }
    }

    private fun bindBalanceData(transactions: List<Transaction>) {
        val income = transactions.filter { it.type== TransactionType.Income }.sumOf{ it.amount }
        val expense = transactions.filter { it.type== TransactionType.Expense }.sumOf { it.amount }
        val sum = income-expense

        _viewModelUiState.update {
            it.copy(
                transactions = transactions
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
}

