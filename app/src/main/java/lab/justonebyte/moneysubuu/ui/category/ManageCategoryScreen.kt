package lab.justonebyte.moneysubuu.ui.category

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch
import lab.justonebyte.moneysubuu.model.TransactionCategory
import lab.justonebyte.moneysubuu.model.TransactionType
import lab.justonebyte.moneysubuu.ui.components.*
import lab.justonebyte.moneysubuu.ui.theme.Red300
import lab.justonebyte.moneysubuu.ui.theme.negativeColor
import lab.justonebyte.moneysubuu.ui.theme.positiveColor
import lab.justonebyte.moneysubuu.ui.theme.primary

sealed class CategoryTab(val index:Int,val title:String){
    object  Income:CategoryTab(1,"Income")
    object Expense:CategoryTab(2,"Expense")
}
val tabs = listOf(
    CategoryTab.Income,CategoryTab.Expense
)

@OptIn(ExperimentalMaterialApi::class)
@ExperimentalPagerApi
@Composable
fun CategoryTabs(
    categoryUiState: CategoryUiState,
    categoryViewModel: CategoryViewModel,
    onTabChanged:(CategoryTab)->Unit,
    type:TransactionType
) {

    var tabIndex by remember { mutableStateOf(1) }
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    val currentTab:MutableState<CategoryTab> = remember { mutableStateOf(CategoryTab.Income) }
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )

    LaunchedEffect(pagerState) {
        // Collect from the pager state a snapshotFlow reading the currentPage
        snapshotFlow { pagerState.currentPage }.collect { page ->
            val currentTab = when(pagerState.currentPage+1){
                CategoryTab.Income.index-> CategoryTab.Income
                else-> CategoryTab.Expense
            }
            onTabChanged(currentTab)
        }
    }

    Column(
        Modifier.absolutePadding(top = 20.dp)
    ) {
        TabRow(
            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier
                .absolutePadding(left = 10.dp, right = 10.dp)
                .clip(RoundedCornerShape(5.dp)),
            selectedTabIndex = tabIndex,
            indicator = { tabPositions -> // 3.
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(
                        pagerState,
                        tabPositions
                    ),
                    color = MaterialTheme.colors.secondary,
                    height = 4.dp
                )
            }) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selectedContentColor = MaterialTheme.colors.secondary,
                    unselectedContentColor = MaterialTheme.colors.onPrimary,
                    selected = false,
                    onClick = {
                        currentTab.value = tab
                        onTabChanged(currentTab.value);
                        coroutineScope.launch { pagerState.animateScrollToPage(index) }
                    },
                    text = { Text(text = tab.title) }
                )
            }
        }
        HorizontalPager( // 4.
            count = tabs.size,
            state = pagerState,
            modifier = Modifier.absolutePadding(left = 10.dp, right = 10.dp)
        ) { _ ->
                        CategoryTabItem(
                            uiState = categoryUiState,
                            addCategory = {
                                categoryViewModel.addCategory(it)
                                categoryViewModel.showSnackBar(SnackBarType.ADD_CATEGORY_SUCCESS)
                            },
                            updateCategory = { category,updatedName->
                                categoryViewModel.updateCategory(category,updatedName)
                                categoryViewModel.showSnackBar(SnackBarType.ADD_CATEGORY_SUCCESS)
                            },
                            clearSnackBar = {
                                categoryViewModel.clearSnackBar()
                            },
                            type = type,
                            bottomSheetScaffoldState = scaffoldState
                        )
        }
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun ManageCategoryScreen(
    goBack:()->Unit
){
    val categoryViewModel = hiltViewModel<CategoryViewModel>()
    val categoryUiState by categoryViewModel.viewModelUiState.collectAsState()
    val currentCategoryType = remember { mutableStateOf(TransactionType.Income) }

   CategoryTabs(
       categoryUiState = categoryUiState,
       categoryViewModel = categoryViewModel,
       onTabChanged = {
           if(it.index==1){
               currentCategoryType.value = TransactionType.Income
           }else{
               currentCategoryType.value = TransactionType.Expense
           }
       },
       type = currentCategoryType.value
   )


}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryTabItem(
    uiState: CategoryUiState,
    clearSnackBar:()->Unit,
    addCategory:(transactionCategory:TransactionCategory)->Unit,
    updateCategory:(transactionCategory:TransactionCategory,updatedName:String)->Unit,
    type:TransactionType = TransactionType.Income,
    bottomSheetScaffoldState:BottomSheetScaffoldState
){

    val categories =  uiState.categories.filter { it.transaction_type==type }
    val isReusableInputDialogShown = remember { mutableStateOf(false) }
    val currentEditingCategory = remember { mutableStateOf<TransactionCategory?>(null) }
    val coroutineScope = rememberCoroutineScope()


    AddNameInputDialog(
        initialValue = currentEditingCategory.value?.name?:"",
        title = if(type==TransactionType.Income) "Enter income category name :" else "Enter expense category name :",
        isShown =  isReusableInputDialogShown.value,
        dialogColor = if(type==TransactionType.Income) positiveColor else negativeColor,
        onDialogDismiss = {
                            isReusableInputDialogShown.value = false
                            currentEditingCategory.value = null
                            coroutineScope.launch {
                                bottomSheetScaffoldState.bottomSheetState.collapse()
                            }
                          },
        onConfirmClick = {
            coroutineScope.launch {
                bottomSheetScaffoldState.bottomSheetState.collapse()
            }
            isReusableInputDialogShown.value = false

            if(currentEditingCategory.value==null){
                val category = TransactionCategory(
                    id = 0,
                    name = it,
                    transaction_type = type,
                    created_at =  System.currentTimeMillis()
                )
                addCategory(category)
            }else{
                    updateCategory(currentEditingCategory.value!!,it)
            }

            currentEditingCategory.value = null

        }
    )

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        snackbarHost =  { SuBuuSnackBarHost(hostState = it) },
        floatingActionButton = {
            if(!bottomSheetScaffoldState.bottomSheetState.isAnimationRunning && bottomSheetScaffoldState.bottomSheetState.isCollapsed){
                Box(
                    modifier = Modifier
                        .absolutePadding(bottom = 100.dp, left = 30.dp)
                ) {
                    TextButton(
                        onClick = {
                            currentEditingCategory.value = null
                            isReusableInputDialogShown.value = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .padding(0.dp)
                            .background(if (type == TransactionType.Income) positiveColor else negativeColor)
                    ) {
                        Text(text =  if(type==TransactionType.Income) "Add New Income Category" else "Add New Expense Category", style = MaterialTheme.typography.button, color = MaterialTheme.colors.onPrimary)
                        Icon(
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                            ,
                            imageVector = Icons.Filled.Add, contentDescription = "add category",
                            tint = MaterialTheme.colors.onPrimary
                        )
                    }
                }
            }
        },
        sheetContent = {
           Column(
               modifier= Modifier.height(200.dp).padding(20.dp),
               verticalArrangement = Arrangement.Center,
               horizontalAlignment = Alignment.CenterHorizontally
           ) {
               TextButton(
                   modifier = Modifier
                       .fillMaxWidth()
                       .clip(RoundedCornerShape(5.dp))
                       .padding(0.dp)
                       .background(primary)
                   ,
                   onClick = {
                        isReusableInputDialogShown.value = true
                   }
               ) {
                   Text(text = "Edit Category",color=MaterialTheme.colors.onPrimary)
               }
               Divider(Modifier.height(20.dp), color = Color.Transparent)
               TextButton(
                   modifier = Modifier
                       .fillMaxWidth()
                       .clip(RoundedCornerShape(5.dp))
                       .padding(0.dp)
                       .background(Red300)
                   ,
                   onClick = {

                   }
               ) {
                   Text(text = "Delete Category", color = MaterialTheme.colors.onPrimary)
               }
           }
        }, sheetPeekHeight = 0.dp

    ) {
        Text(text = if(type===TransactionType.Income) "Income Categories List :" else "Expense Categories List :", style = MaterialTheme.typography.h6, modifier = Modifier.absolutePadding(top = 30.dp, bottom = 20.dp))

        LazyColumn(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.absolutePadding(left = 2.dp, right = 2.dp, bottom = 100.dp)
        ) {
            items(categories){
                CategoryItem(
                    category = it,
                    itemColor =  if(type==TransactionType.Income) positiveColor else negativeColor,
                    onClick = {
                        coroutineScope.launch {
                            currentEditingCategory.value = it
                            bottomSheetScaffoldState.bottomSheetState.expand()
                        }
                    }
                )
            }
        }
    }
    SuBuuSnackBar(
        onDismissSnackBar = { clearSnackBar() },
        scaffoldState = bottomSheetScaffoldState,
        snackBarType = uiState.currentSnackBar,
    )

}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryItem(
    modifier: Modifier=Modifier,
    category:TransactionCategory,
    itemColor:Color,
    onClick:()->Unit
){
        Card(
            elevation = 2.dp,
            contentColor = itemColor,
            modifier = modifier,
            onClick = {
                onClick()
            }
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp), verticalArrangement = Arrangement.Center) {
                Text(text = category.name)
            }
        }
    Divider(Modifier.height(10.dp), color = Color.Transparent)

}