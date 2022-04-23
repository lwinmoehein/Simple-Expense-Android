package lab.justonebyte.moneysubuu.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import lab.justonebyte.moneysubuu.data.CategoryRepository
import lab.justonebyte.moneysubuu.data.TransactionRepository
import lab.justonebyte.moneysubuu.model.Transaction
import lab.justonebyte.moneysubuu.model.TransactionCategory
import lab.justonebyte.moneysubuu.model.TransactionType
import javax.inject.Inject

data class HomeUiState(
    val currentBalance:Double,
    val categories:List<TransactionCategory>  = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
):ViewModel()
{
    private val _viewModelUiState  = MutableStateFlow(
        HomeUiState(currentBalance = 0.0)
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
        transactionRepository.getTransactions().collect{
            val income = it.filter { it.type==TransactionType.Income }.sumByDouble { it.amount }
            val expense = it.filter { it.type==TransactionType.Expense }.sumByDouble { it.amount }
            val sum = income-expense
            _viewModelUiState.update { it.copy(currentBalance = sum) }
        }
    }
    private suspend fun collectCategories(){
        categoryRepository.getCategories().collect{ categories->
            _viewModelUiState.update { it.copy(categories = categories) }
        }
    }
    fun addTransaction(transactionCategory: TransactionCategory,amount:Long,type:Int){
        viewModelScope.launch {
            transactionRepository.insert(
                Transaction(
                    amount = amount.toDouble(),
                    type = if(type==1) TransactionType.Income else TransactionType.Expense,
                    category = transactionCategory,
                    created_at = System.currentTimeMillis().toDouble()
                )
            )
        }
    }
}