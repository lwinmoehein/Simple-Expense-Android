package lab.justonebyte.moneysubuu.ui.detail

import android.util.Log
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
import lab.justonebyte.moneysubuu.ui.home.HomeTab
import lab.justonebyte.moneysubuu.ui.home.HomeUiState
import lab.justonebyte.moneysubuu.utils.dateFormatter
import lab.justonebyte.moneysubuu.utils.monthFormatter
import lab.justonebyte.moneysubuu.utils.yearFormatter
import javax.inject.Inject


data class DetailUiState(
    val categories:List<TransactionCategory>  = emptyList(),
    val transactions:List<Transaction> = emptyList(),
    val currentSnackBar : SnackBarType? = null,
    val selectedDay:String = dateFormatter(System.currentTimeMillis()),
    val selectedMonth:String = monthFormatter(System.currentTimeMillis()),
    val selectedYear:String = yearFormatter(System.currentTimeMillis())
)

@HiltViewModel
class TransactionDetailViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
): ViewModel()
{
    private val _viewModelUiState  = MutableStateFlow(
        DetailUiState()
    )
    val viewModelUiState: StateFlow<DetailUiState>
        get() =  _viewModelUiState


    init {
        viewModelScope.launch {
            launch {
                collectCategories()
            }
            launch {
                collectTransactions()
            }
        }
    }

    fun bindPieChartData(tabType:HomeTab,dateData:String){
        Log.i("bindchart",tabType.title+":"+dateData)
        when(tabType){
            HomeTab.Daily->collectDailyBalance(dateValue = dateData)
            HomeTab.Monthly->collectMonthlyBalance(dateValue = dateData)
            HomeTab.Yearly->collectYearlyBalance(dateValue = dateData)
            else->collectTotalBalance()
        }
    }

    private suspend fun collectTransactions() {
        transactionRepository.getTotalTransactions().collect { transactions->
            _viewModelUiState.update { it.copy(transactions = transactions) }
        }
    }

    private suspend fun collectCategories() {
        categoryRepository.getCategories().collect { categories->
            _viewModelUiState.update { it.copy(categories = categories) }
        }
    }

    fun clearSnackBar(){
        _viewModelUiState.update {
            it.copy(currentSnackBar = null)
        }
    }
    private fun bindBalanceData(transactions: List<Transaction>) {
        _viewModelUiState.update {
            it.copy(
                transactions = transactions
            )
        }
    }

    fun collectTotalBalance(){
        viewModelScope.launch {
            transactionRepository.getTotalTransactions().collect{ transactions->
                bindBalanceData(transactions)
            }
        }
    }
    fun collectDailyBalance(dateValue:String = _viewModelUiState.value.selectedDay){
        _viewModelUiState.update {
            it.copy(selectedDay = dateValue)
        }

        viewModelScope.launch {
            transactionRepository.getDailyTransactions(dateValue).collect{ transactions->
                bindBalanceData(transactions)
            }
        }
    }
    fun collectMonthlyBalance(dateValue:String=  _viewModelUiState.value.selectedMonth){
        _viewModelUiState.update {
            it.copy(selectedMonth = dateValue)
        }
        viewModelScope.launch {
            transactionRepository.getMonthlyTransactions(dateValue).collect{ transactions->
                bindBalanceData(transactions)
            }
        }
    }
    fun collectYearlyBalance(dateValue:String= _viewModelUiState.value.selectedYear){
        _viewModelUiState.update {
            it.copy(selectedYear = dateValue)
        }
        viewModelScope.launch {
            transactionRepository.getYearlyTransactions(dateValue).collect{ transactions->
                bindBalanceData(transactions)
            }
        }
    }

}