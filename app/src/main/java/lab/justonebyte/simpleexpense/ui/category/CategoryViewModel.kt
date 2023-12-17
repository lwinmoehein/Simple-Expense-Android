package lab.justonebyte.simpleexpense.ui.category

import android.content.Context
import android.util.Log
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
import lab.justonebyte.simpleexpense.utils.getCurrentGlobalTime
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
        Log.i("addCategory:",transactinCategory.name)
        viewModelScope.launch {
            categoryRepository.insert(transactionCategory = transactinCategory)
            runVersionSync(application,"categories",token.value)
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
            runVersionSync(application,"categories",token.value)
        }
    }
    fun removeCategory(transactinCategory:TransactionCategory){
        viewModelScope.launch {
            categoryRepository.delete(id = transactinCategory.unique_id)
            runVersionSync(application,"categories",token.value)

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

