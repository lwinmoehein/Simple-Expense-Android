package lab.justonebyte.moneysubuu.ui.category
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch
import lab.justonebyte.moneysubuu.R
import lab.justonebyte.moneysubuu.model.TransactionCategory
import lab.justonebyte.moneysubuu.model.TransactionType
import lab.justonebyte.moneysubuu.ui.components.*
import lab.justonebyte.moneysubuu.ui.home.ChooseTransactionTypeDialog
import lab.justonebyte.moneysubuu.ui.theme.negativeColor
import lab.justonebyte.moneysubuu.ui.theme.positiveColor
import lab.justonebyte.moneysubuu.utils.getCurrentGlobalTime
import java.util.UUID

sealed class CategoryTab(val index:Int,val title:Int){
    object  Income:CategoryTab(0,R.string.income)
    object Expense:CategoryTab(1,R.string.expense)
}


@ExperimentalPagerApi
@Composable
fun CategoryTabs(
    onTabChanged: (CategoryTab) -> Unit,
    currentCategoryTabIndex: Int
) {

    val categoryTabs = listOf(CategoryTab.Income,CategoryTab.Expense)

    TabRow(selectedTabIndex = currentCategoryTabIndex) {
            categoryTabs.forEachIndexed { index, categoryTab ->
                Tab(
                    selected = currentCategoryTabIndex == index,
                    onClick = {
                        onTabChanged(categoryTab)
                    },
                    text = { Text(text = stringResource(id = categoryTab.title), maxLines = 2, overflow = TextOverflow.Ellipsis) }
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
    var currentCategoryTabIndex = remember { mutableStateOf(0) }
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
        title = if(currentCategoryTabIndex.value==0) stringResource(id = R.string.enter_in_category) else stringResource(R.string.enter_ex_category),
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
                    transaction_type = if(currentCategoryTabIndex.value==0) TransactionType.Expense else TransactionType.Income,
                    created_at =  getCurrentGlobalTime()
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
                       Icon(painterResource(id = R.drawable.ic_round_category_24), contentDescription = "",Modifier.absolutePadding(right = 5.dp))
                       Text(
                           stringResource(id = R.string.m_categories),
                           maxLines = 1,
                           overflow = TextOverflow.Ellipsis,
                           style = MaterialTheme.typography.titleLarge
                       )
                   }
                   if(currentCategoryTabIndex.value==0){
                       OutlinedButton(onClick = {
                           currentEditingCategory.value = null
                           isReusableInputDialogShown.value = true
                       }) {
                           Text(text = stringResource(id = R.string.add_income_cat))
                           Icon(imageVector = Icons.Filled.Add, contentDescription ="" )
                       }
                   }else{
                       OutlinedButton(onClick = {
                           currentEditingCategory.value = null
                           isReusableInputDialogShown.value = true
                       }) {
                           Text(text = stringResource(id = R.string.add_expense_cat))
                           Icon(imageVector = Icons.Filled.Add, contentDescription ="" )
                       }
                   }
               }
               Divider()
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