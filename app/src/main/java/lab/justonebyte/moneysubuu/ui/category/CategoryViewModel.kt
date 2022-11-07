package lab.justonebyte.moneysubuu.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import lab.justonebyte.moneysubuu.data.CategoryRepository
import lab.justonebyte.moneysubuu.model.TransactionCategory
import lab.justonebyte.moneysubuu.ui.components.SnackBarType
import javax.inject.Inject


data class CategoryUiState(
    val categories:List<TransactionCategory>  = emptyList(),
    val currentSnackBar : SnackBarType? = null
)

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
): ViewModel() {
    private val _viewModelUiState = MutableStateFlow(
        CategoryUiState()
    )
    val viewModelUiState: StateFlow<CategoryUiState>
        get() = _viewModelUiState


    init {
        viewModelScope.launch {
            launch {
                collectCategories()
            }
        }
    }

    private suspend fun collectCategories() {
        categoryRepository.getCategories().collect { categories->
            _viewModelUiState.update { it.copy(categories = categories) }
        }
    }
    fun addCategory(transactinCategory:TransactionCategory){
        viewModelScope.launch {
            categoryRepository.insert(transactionCategory = transactinCategory)
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

