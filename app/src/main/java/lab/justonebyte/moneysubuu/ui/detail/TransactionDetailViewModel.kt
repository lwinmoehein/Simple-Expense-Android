package lab.justonebyte.moneysubuu.ui.detail

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
import javax.inject.Inject


data class DetailUiState(
    val categories:List<TransactionCategory>  = emptyList(),
    val transactions:List<Transaction> = emptyList(),
    val currentSnackBar : SnackBarType? = null,
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

}