package lab.justonebyte.simpleexpense.ui.category
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.*
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowDown
import compose.icons.feathericons.ArrowUp
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.model.TransactionCategory
import lab.justonebyte.simpleexpense.model.TransactionType
import lab.justonebyte.simpleexpense.ui.components.*
import lab.justonebyte.simpleexpense.utils.getCurrentGlobalTime
import java.util.UUID

sealed class CategoryTab(val index:Int,val title:Int,val icon:ImageVector){
    object Expense:CategoryTab(0,R.string.expense,FeatherIcons.ArrowUp)
    object  Income:CategoryTab(1,R.string.income,FeatherIcons.ArrowDown)
}


@ExperimentalPagerApi
@Composable
fun CategoryTabs(
    onTabChanged: (CategoryTab) -> Unit,
    currentCategoryTabIndex: Int
) {

    val categoryTabs = listOf(CategoryTab.Expense,CategoryTab.Income)

    TabRow(selectedTabIndex = currentCategoryTabIndex) {
            categoryTabs.forEachIndexed { index, categoryTab ->
                Tab(
                    selected = currentCategoryTabIndex == index,
                    onClick = {
                        onTabChanged(categoryTab)
                    },
                    text = { Text(text = stringResource(id = categoryTab.title), maxLines = 1) }
                )
            }
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ManageCategoryScreen(
    goBack:()->Unit
){
    val categoryViewModel = hiltViewModel<CategoryViewModel>()
    val categoryUiState by categoryViewModel.viewModelUiState.collectAsState()
    val currentCategoryTabIndex = remember { mutableStateOf(0) }
    val currentEditingCategory = remember { mutableStateOf<TransactionCategory?>(null) }
    val isReusableInputDialogShown = remember { mutableStateOf(false) }
    val isChooseCategoryActionDialogOpen = remember { mutableStateOf(false) }
    val isConfirmDeleteCategoryDialogShown = remember { mutableStateOf(false) }

    val categories =  categoryUiState.categories.filter { it.transaction_type==if(currentCategoryTabIndex.value==0) TransactionType.Expense else TransactionType.Income }


    fun clearDialogs(){
        isReusableInputDialogShown.value = false
        isChooseCategoryActionDialogOpen.value = false
        isConfirmDeleteCategoryDialogShown.value = false
    }
    ChooseCategoryActionDialog(
        isOpen = isChooseCategoryActionDialogOpen.value && currentEditingCategory.value!=null,
        onDeleteCategory = {
            isChooseCategoryActionDialogOpen.value = false
            isConfirmDeleteCategoryDialogShown.value = true
        },
        onEditCategory =  {
            isChooseCategoryActionDialogOpen.value = false
            isReusableInputDialogShown.value = true
        }
    )
    if(isConfirmDeleteCategoryDialogShown.value){
        AppAlertDialog(
            title= stringResource(id = R.string.r_u_sure),
            positiveBtnText = stringResource(id = R.string.confirm),
            negativeBtnText = stringResource(id = R.string.cancel),
            onPositiveBtnClicked = {
                currentEditingCategory.value?.let { categoryViewModel.removeCategory(it) }
                clearDialogs()
                currentEditingCategory.value = null
            },
            onNegativeBtnClicked = {
                clearDialogs()
                currentEditingCategory.value = null
            }
        ){
            Text(text = stringResource(id = R.string.r_u_sure_cat_delete) )
        }
    }

    AddNameInputDialog(
        initialValue = currentEditingCategory.value?.name?:"",
        title = if(currentCategoryTabIndex.value==1) stringResource(id = R.string.enter_in_category) else stringResource(R.string.enter_ex_category),
        isShown =  isReusableInputDialogShown.value,
        onDialogDismiss = {
            clearDialogs()
            currentEditingCategory.value = null
        },
        onConfirmClick = {

            isReusableInputDialogShown.value = false

            if(currentEditingCategory.value==null){
                val category = TransactionCategory(
                    unique_id = UUID.randomUUID().toString(),
                    name = it,
                    transaction_type = if(currentCategoryTabIndex.value==1) TransactionType.Income else TransactionType.Expense,
                    created_at =  getCurrentGlobalTime(),
                    updated_at = getCurrentGlobalTime()
                )
                categoryViewModel.addCategory(category)
            }else{
                categoryViewModel.updateCategory(currentEditingCategory.value!!,it)
            }
            clearDialogs()
            currentEditingCategory.value = null
        }
    )

    Scaffold(
        topBar =  {
           Column {
               Row(
                   Modifier
                       .fillMaxWidth()
                       .padding(10.dp),
                   horizontalArrangement = Arrangement.SpaceBetween,
                   verticalAlignment = Alignment.CenterVertically
               ) {
                   Row(
                       verticalAlignment = Alignment.CenterVertically

                   ) {
                       Text(
                           stringResource(id = R.string.m_categories),
                           maxLines = 1,
                           overflow = TextOverflow.Ellipsis,
                           style = MaterialTheme.typography.titleLarge
                       )
                   }
               }
               Divider()
           }
        },
        floatingActionButton = {
            if(currentCategoryTabIndex.value==0){
                FloatingActionButton(
                    onClick = {
                        currentEditingCategory.value = null
                        isReusableInputDialogShown.value = true
                    },
                ) {
                    Icon(Icons.Filled.Add, "Localized description")
                }
            }else{
                FloatingActionButton(
                    onClick = {
                        currentEditingCategory.value = null
                        isReusableInputDialogShown.value = true
                    },
                ) {
                    Icon(Icons.Filled.Add, "Localized description")
                }
            }
        }
    ) {
         Column(Modifier.padding(it)) {
             CategoryTabs(
                 onTabChanged = {
                     currentCategoryTabIndex.value = it.index
                 },
                 currentCategoryTabIndex = currentCategoryTabIndex.value
             )
             LazyColumn(Modifier.padding(10.dp)){
                 items(categories){
                        CategoryItem(
                            category = it,
                            onClick = {
                                    isChooseCategoryActionDialogOpen.value = true
                                    currentEditingCategory.value = it
                            }
                        )
                 }
             }
         }
    }


}

@Composable
fun CategoryItem(
    modifier: Modifier=Modifier,
    category:TransactionCategory,
    onClick:()->Unit
){
        Card(
            modifier = modifier.clickable { onClick() }
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp), verticalArrangement = Arrangement.Center) {
                Text(text = category.name)
            }
        }
          Divider(Modifier.height(3.dp), color = Color.Transparent)
}