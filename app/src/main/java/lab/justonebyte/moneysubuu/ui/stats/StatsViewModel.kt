package lab.justonebyte.moneysubuu.ui.stats

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
import lab.justonebyte.moneysubuu.ui.components.SnackBarType
import lab.justonebyte.moneysubuu.utils.dateFormatter
import lab.justonebyte.moneysubuu.utils.monthFormatter
import lab.justonebyte.moneysubuu.utils.yearFormatter
import javax.inject.Inject


data class StatsUiState(
    val categories:List<TransactionCategory>  = emptyList(),
    val transactions:List<Transaction> = emptyList(),
    val currentSnackBar : SnackBarType? = null,
    val selectedDay:String = dateFormatter(System.currentTimeMillis()),
    val selectedMonth:String = monthFormatter(System.currentTimeMillis()),
    val selectedYear:String = yearFormatter(System.currentTimeMillis())
)

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
): ViewModel() {
    private val _viewModelUiState = MutableStateFlow(
        StatsUiState()
    )
    val viewModelUiState: StateFlow<StatsUiState>
        get() = _viewModelUiState


    init {
        viewModelScope.launch {
            launch {
                collectTransactions()
            }
        }
    }

    private suspend fun collectTransactions() {
        transactionRepository.getTotalTransactions().collect { transactions->
            _viewModelUiState.update { it.copy(transactions = transactions) }
        }
    }

}

