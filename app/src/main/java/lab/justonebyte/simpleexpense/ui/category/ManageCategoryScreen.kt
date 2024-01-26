package lab.justonebyte.simpleexpense.ui.category
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowDown
import compose.icons.feathericons.ArrowUp
import compose.icons.feathericons.Plus
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.model.TransactionCategory
import lab.justonebyte.simpleexpense.model.TransactionType
import lab.justonebyte.simpleexpense.ui.components.AppAlertDialog
import lab.justonebyte.simpleexpense.ui.components.TransactionTypeTab
import lab.justonebyte.simpleexpense.ui.components.getIconFromName
import java.util.UUID

sealed class CategoryTab(val index:Int,val title:Int,val icon:ImageVector){
    object Expense:CategoryTab(0,R.string.expense,FeatherIcons.ArrowUp)
    object  Income:CategoryTab(1,R.string.income,FeatherIcons.ArrowDown)
}


@ExperimentalPagerApi
@Composable
fun CategoryTabs(
    onTabChanged: (TransactionType) -> Unit
) {
    var transactionTypeTabState by remember { mutableStateOf(0) }
    val transactionTypeTabs = listOf(TransactionTypeTab.EXPENSE, TransactionTypeTab.INCOME)

    TabRow(selectedTabIndex = transactionTypeTabState) {
        transactionTypeTabs.forEachIndexed { index, tab ->
            Tab(
                selected = transactionTypeTabState == index,
                onClick = {
                    transactionTypeTabState = index
                    onTabChanged(tab.transactionType)
                },
                text = { Text(text = stringResource(id = tab.name), maxLines = 2, overflow = TextOverflow.Ellipsis) }
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ManageCategoryScreen(){
    val categoryViewModel = hiltViewModel<CategoryViewModel>()
    val categoryUiState by categoryViewModel.viewModelUiState.collectAsState()
    val currentEditingCategory = remember { mutableStateOf<TransactionCategory?>(null) }
    val isReusableInputDialogShown = remember { mutableStateOf(false) }
    val isChooseCategoryActionDialogOpen = remember { mutableStateOf(false) }
    val isConfirmDeleteCategoryDialogShown = remember { mutableStateOf(false) }
    val currentTransactionType = remember { mutableStateOf(TransactionType.Expense) }

    val categories =  categoryUiState.categories.filter { it.transaction_type==currentTransactionType.value }.sortedBy { it.name }
    
    
    val nonOtherCategories = categories.filter { it.name!="Other" } + categories.filter { it.name=="Other" }

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
        },
        onDismissClicked = {
            isChooseCategoryActionDialogOpen.value = false
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

    AddCategoryInputDialog(
        initialValue = currentEditingCategory.value?.name?:"",
        title = stringResource(id = R.string.category_name),
        isShown =  isReusableInputDialogShown.value,
        onDialogDismiss = {
            clearDialogs()
            currentEditingCategory.value = null
        },
        initialTransactionType = currentTransactionType.value,
        onConfirmClick = { categoryName,transactionType->

            isReusableInputDialogShown.value = false

             val category = TransactionCategory(
                unique_id = UUID.randomUUID().toString(),
                name = categoryName,
                icon_name = "other",
                transaction_type = transactionType,
                created_at =  System.currentTimeMillis(),
                updated_at =  System.currentTimeMillis()
            )
            categoryViewModel.addCategory(category)
            clearDialogs()
            currentEditingCategory.value = null
        }
    )

    Scaffold(
    ) {
         Column(Modifier.padding(it)) {
             Row(
                 modifier  = Modifier.fillMaxWidth(),
                 horizontalArrangement = Arrangement.Center
             ){
                 CategoryTabs(
                     onTabChanged = {
                         currentTransactionType.value = it
                     }
                 )
             }
             Spacer(modifier = Modifier.height(10.dp))

             OutlinedButton(
                 border = BorderStroke(0.2.dp,MaterialTheme.colorScheme.primary),
                 colors = ButtonDefaults.buttonColors(
                     backgroundColor = Color.Transparent,
                     contentColor = MaterialTheme.colorScheme.primary
                 ),
                 modifier = Modifier
                     .padding(horizontal = 10.dp)
                     .fillMaxWidth(),
                 onClick = {
                 currentEditingCategory.value = null
                 isReusableInputDialogShown.value = true },
                 shape = MaterialTheme.shapes.large
                 ) {
                 Text(text = stringResource(id = R.string.add_new))
                 Spacer(modifier = Modifier.width(5.dp))
                 Icon(imageVector = FeatherIcons.Plus, contentDescription = "")
             }
             Spacer(modifier = Modifier.height(10.dp))
             LazyVerticalGrid(
                 columns = GridCells.Fixed(2),
                 modifier = Modifier.padding(5.dp)
             ) {
                 items(nonOtherCategories.size) { index ->
                     CategoryItem(
                         category = nonOtherCategories[index],
                         onClick = {
                             isChooseCategoryActionDialogOpen.value = true
                             currentEditingCategory.value =  nonOtherCategories[index]
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
            modifier = modifier
                .clickable { onClick() }
                .padding(5.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = getIconFromName(name = category.icon_name),
                    contentDescription = "",
                    modifier = Modifier
                        .width(18.dp)
                        .height(18.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = category.name)
            }
        }
    Divider(Modifier.height(3.dp), color = Color.Transparent)
}