package lab.justonebyte.moneysubuu.ui.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import lab.justonebyte.moneysubuu.data.CategoryRepository
import lab.justonebyte.moneysubuu.data.TransactionRepository
import lab.justonebyte.moneysubuu.model.Transaction
import lab.justonebyte.moneysubuu.model.TransactionCategory
import lab.justonebyte.moneysubuu.model.TransactionType
import lab.justonebyte.moneysubuu.ui.components.SnackBarType
import lab.justonebyte.moneysubuu.utils.dateFormatter
import lab.justonebyte.moneysubuu.utils.monthFormatter
import lab.justonebyte.moneysubuu.utils.yearFormatter
import java.time.LocalDate
import javax.inject.Inject

data class HomeUiState(
    val currentBalance:Int,
    val incomeBalance:Int,
    val expenseBalance:Int,
    val totalBalance:Int,
    val categories:List<TransactionCategory>  = emptyList(),
    val transactions:List<Transaction> = emptyList(),
    val currentSnackBar : SnackBarType? = null,
    val selectedDay:String = dateFormatter(System.currentTimeMillis()),
    val selectedMonth:String = monthFormatter(System.currentTimeMillis()),
    val selectedYear:String = yearFormatter(System.currentTimeMillis())

)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
):ViewModel()
{
    private val _viewModelUiState  = MutableStateFlow(
        HomeUiState(currentBalance = 0, incomeBalance = 0, expenseBalance = 0, totalBalance = 0)
    )

    val viewModelUiState: StateFlow<HomeUiState>
        get() =  _viewModelUiState

    init {
        collectDailyBalance()
        viewModelScope.launch {
            launch {
                collectCategories()
            }
        }
    }
    fun collectTotalBalance(){
        viewModelScope.launch {
            transactionRepository.getTotalTransactions().collect{ transactions->
                bindBalanceData(transactions)
            }
        }
    }
     fun collectDailyBalance(dateValue:String = viewModelUiState.value.selectedDay){
        _viewModelUiState.update {
            it.copy(selectedDay = dateValue)
        }

        viewModelScope.launch {
            transactionRepository.getDailyTransactions(dateValue).collect{ transactions->
                bindBalanceData(transactions)
            }
        }
    }
     fun collectMonthlyBalance(dateValue:String=  viewModelUiState.value.selectedMonth){
         _viewModelUiState.update {
             it.copy(selectedMonth = dateValue)
         }
        viewModelScope.launch {
            transactionRepository.getMonthlyTransactions(dateValue).collect{ transactions->
                bindBalanceData(transactions)
            }
        }
    }
     fun collectYearlyBalance(dateValue:String= yearFormatter(System.currentTimeMillis())){
         _viewModelUiState.update {
             it.copy(selectedYear = dateValue)
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
    fun showIncorrectFormDataSnackbar(){
        _viewModelUiState.update {
            it.copy(currentSnackBar = SnackBarType.INCORRECT_DATA)
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
                    created_at = date
                )
            )
        }
    }
    fun addCategory(transactinCategory:TransactionCategory){
        viewModelScope.launch {
            categoryRepository.insert(transactionCategory = transactinCategory)
        }
    }
}