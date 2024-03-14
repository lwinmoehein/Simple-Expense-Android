package lab.justonebyte.simpleexpense.ui.category

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import lab.justonebyte.simpleexpense.data.CategoryRepository
import lab.justonebyte.simpleexpense.data.SettingPrefRepository
import lab.justonebyte.simpleexpense.model.TransactionCategory
import lab.justonebyte.simpleexpense.ui.components.SnackBarType
import lab.justonebyte.simpleexpense.workers.runVersionSync
import javax.inject.Inject


data class CategoryUiState(
    val categories:List<TransactionCategory>  = emptyList(),
    val currentSnackBar : SnackBarType? = null
)

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    @ApplicationContext private val application: Context,
    private val settingsRepository: SettingPrefRepository
    ): ViewModel() {
    private val _viewModelUiState = MutableStateFlow(
        CategoryUiState()
    )
    val viewModelUiState: StateFlow<CategoryUiState>
        get() = _viewModelUiState

    private val token = mutableStateOf("")


    init {
        viewModelScope.launch {
            launch {
                collectToken()
            }
            launch {
                collectCategories()
            }
        }
    }

    private suspend fun collectToken() {
        settingsRepository.accessToken.collect{
            token.value = it
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
            if(token.value.isNotEmpty()) runVersionSync(application,"categories",token.value)
        }
    }
    fun updateCategory(transactionCategory:TransactionCategory, name:String){
        val category = TransactionCategory(
            name = name,
            icon_name = transactionCategory.icon_name,
            unique_id = transactionCategory.unique_id,
            transaction_type = transactionCategory.transaction_type,
            created_at = transactionCategory.created_at,
            updated_at = System.currentTimeMillis()
        )
        viewModelScope.launch {
            categoryRepository.update(transactionCategory = category)
            if(token.value.isNotEmpty()) runVersionSync(application,"categories",token.value)
        }
    }
    fun removeCategory(transactinCategory:TransactionCategory){
        viewModelScope.launch {
            categoryRepository.delete(id = transactinCategory.unique_id)
            if(token.value.isNotEmpty()) runVersionSync(application,"categories",token.value)
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

