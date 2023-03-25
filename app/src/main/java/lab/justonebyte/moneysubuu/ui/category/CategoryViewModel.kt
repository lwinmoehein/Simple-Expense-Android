package lab.justonebyte.moneysubuu.ui.category

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import lab.justonebyte.moneysubuu.data.CategoryRepository
import lab.justonebyte.moneysubuu.model.TransactionCategory
import lab.justonebyte.moneysubuu.ui.components.SnackBarType
import lab.justonebyte.moneysubuu.utils.getCurrentGlobalTime
import lab.justonebyte.moneysubuu.workers.runVersionSync
import javax.inject.Inject


data class CategoryUiState(
    val categories:List<TransactionCategory>  = emptyList(),
    val currentSnackBar : SnackBarType? = null
)

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    @ApplicationContext private val application: Context,
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
        Log.i("addCategory:",transactinCategory.name)
        viewModelScope.launch {
            categoryRepository.insert(transactionCategory = transactinCategory)
            runVersionSync(application,"categories")
        }
    }
    fun updateCategory(transactinCategory:TransactionCategory,name:String){
        val category = TransactionCategory(
            name = name,
            unique_id = transactinCategory.unique_id,
            transaction_type = transactinCategory.transaction_type,
            created_at = transactinCategory.created_at,
            updated_at = getCurrentGlobalTime()
        )
        viewModelScope.launch {
            categoryRepository.update(transactionCategory = category)
            runVersionSync(application,"categories")
        }
    }
    fun removeCategory(transactinCategory:TransactionCategory){
        viewModelScope.launch {
            categoryRepository.delete(id = transactinCategory.unique_id)
            runVersionSync(application,"categories")

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

