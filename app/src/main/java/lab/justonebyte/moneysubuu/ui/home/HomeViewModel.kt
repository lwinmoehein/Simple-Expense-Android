package lab.justonebyte.moneysubuu.ui.home

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
import javax.inject.Inject

data class HomeUiState(
    val currentBalance:Int,
    val incomeBalance:Int,
    val expenseBalance:Int,
    val categories:List<TransactionCategory>  = emptyList(),
    val transactions:List<Transaction> = emptyList(),
    val currentSnackBar : SnackBarType? = null,
    )

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
):ViewModel()
{
    private val _viewModelUiState  = MutableStateFlow(
        HomeUiState(currentBalance = 0, incomeBalance = 0, expenseBalance = 0)
    )
    val viewModelUiState: StateFlow<HomeUiState>
        get() =  _viewModelUiState

    init {
        viewModelScope.launch {
           launch {
               collectBalance()
           }
            launch {
                collectCategories()
            }
        }
    }
    private suspend fun collectBalance(){
        transactionRepository.getTransactions().collect{ transactions->
            val income = transactions.filter { it.type==TransactionType.Income }.sumOf{ it.amount }
            val expense = transactions.filter { it.type==TransactionType.Expense }.sumOf { it.amount }
            val sum = income-expense
            _viewModelUiState.update {
                it.copy(
                    currentBalance = sum,
                    incomeBalance = income.toInt(),
                    expenseBalance = expense.toInt(),
                    transactions = transactions
                ) }
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
    fun addTransaction(transactionCategory: TransactionCategory,amount:Int,type:Int){
        viewModelScope.launch {
            transactionRepository.insert(
                Transaction(
                    amount = amount,
                    type = if(type==1) TransactionType.Income else TransactionType.Expense,
                    category = transactionCategory,
                    created_at = System.currentTimeMillis().toDouble()
                )
            )
        }
    }
}