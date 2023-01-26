package lab.justonebyte.moneysubuu.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import lab.justonebyte.moneysubuu.data.CategoryRepository
import lab.justonebyte.moneysubuu.data.SettingPrefRepository
import lab.justonebyte.moneysubuu.data.TransactionRepository
import lab.justonebyte.moneysubuu.model.BalanceType
import lab.justonebyte.moneysubuu.model.Transaction
import lab.justonebyte.moneysubuu.model.TransactionCategory
import lab.justonebyte.moneysubuu.model.TransactionType
import lab.justonebyte.moneysubuu.ui.components.SnackBarType
import lab.justonebyte.moneysubuu.utils.dateFormatter
import lab.justonebyte.moneysubuu.utils.monthFormatter
import lab.justonebyte.moneysubuu.utils.weekFormatter
import lab.justonebyte.moneysubuu.utils.yearFormatter
import javax.inject.Inject

data class HomeUiState(
    val currentBalance:Int,
    val incomeBalance:Int,
    val expenseBalance:Int,
    val totalBalance:Int,
    val currentBalanceType:BalanceType = BalanceType.MONTHLY,
    val categories:List<TransactionCategory>  = emptyList(),
    val transactions:List<Transaction> = emptyList(),
    val currentSnackBar : SnackBarType? = null,
    val selectedDay:String = dateFormatter(System.currentTimeMillis()),
    val selectedWeek:String = weekFormatter(System.currentTimeMillis()),
    val selectedMonth:String = monthFormatter(System.currentTimeMillis()),
    val selectedYear:String = yearFormatter(System.currentTimeMillis())

)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val settingsRepository: SettingPrefRepository
):ViewModel()
{
    private val _viewModelUiState  = MutableStateFlow(
        HomeUiState(currentBalance = 0, incomeBalance = 0, expenseBalance = 0, totalBalance = 0)
    )

    val viewModelUiState: StateFlow<HomeUiState>
        get() =  _viewModelUiState

    init {
        viewModelScope.launch {
            launch {
                collectCategories()
            }
            launch {
                collectBalanceTypeFromSetting()
            }
        }
    }
    fun collectTotalBalance(){
        _viewModelUiState.update {
            it.copy(currentBalanceType = BalanceType.TOTAL)
        }
        viewModelScope.launch {
            transactionRepository.getTotalTransactions().collect{ transactions->
                bindBalanceData(transactions)
            }
        }
    }
    private suspend fun collectBalanceTypeFromSetting(){
        settingsRepository.defaultBalanceType.collect{
            bindTransactionsFromBalanceType(BalanceType.getFromValue(it))
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
    fun collectWeeklyBalance(dateValue:String =  viewModelUiState.value.selectedWeek){
        _viewModelUiState.update {
            it.copy(selectedWeek = dateValue)
        }
        viewModelScope.launch {
            transactionRepository.getWeeklyTransactions(dateValue).collect{ transactions->
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
        val income = transactions.filter { it.type==TransactionType.Income }.sumOf{ it.amount }
        val expense = transactions.filter { it.type==TransactionType.Expense }.sumOf { it.amount }
        val sum = income-expense

        _viewModelUiState.update {
            it.copy(
                currentBalance = sum,
                incomeBalance = income,
                expenseBalance = expense,
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
    fun addTransaction(transactionCategory: TransactionCategory,amount:Int,type:Int,date:String){
        viewModelScope.launch {
            transactionRepository.insert(
                Transaction(
                    amount = amount,
                    type = if(type==1) TransactionType.Income else TransactionType.Expense,
                    category = transactionCategory,
                    created_at = date,
                    created_timestamp = System.currentTimeMillis()
                )
            )

        }
    }
    fun updateTransaction(transactionId:Int?,transactionCategory: TransactionCategory,amount:Int,type:Int,date:String){
        Log.i("update tran",(transactionId?:1).toString()+":"+amount.toString())
        viewModelScope.launch {
            transactionRepository.update(
                Transaction(
                    id = transactionId,
                    amount = amount,
                    type = if(type==1) TransactionType.Income else TransactionType.Expense,
                    category = transactionCategory,
                    created_at = date,
                    created_timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    fun deleteTransaction(transaction: Transaction){
        Log.i("delete tran", transaction.id.toString())

        viewModelScope.launch {
            transactionRepository.delete(transaction)
        }
    }

    fun addCategory(transactinCategory:TransactionCategory){
        viewModelScope.launch {
            categoryRepository.insert(transactionCategory = transactinCategory)
        }
    }
}