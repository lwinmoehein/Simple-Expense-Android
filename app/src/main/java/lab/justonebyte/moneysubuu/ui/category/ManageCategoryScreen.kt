package lab.justonebyte.moneysubuu.ui.category

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
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
import lab.justonebyte.moneysubuu.ui.theme.positiveColor

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
                        Log.i("tab:",index.toString());
                        Log.i("tab:",tab.title);
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
                        CategoryTab(
                            uiState = categoryUiState,
                            addCategory = {
                                categoryViewModel.addCategory(it)
                                categoryViewModel.showSnackBar(SnackBarType.ADD_CATEGORY_SUCCESS)
                            },
                            clearSnackBar = {
                                categoryViewModel.clearSnackBar()
                            },
                            type = type
                        )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
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
           if(it.index==0){
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
fun CategoryTab(
    uiState: CategoryUiState,
    clearSnackBar:()->Unit,
    addCategory:(transactionCategory:TransactionCategory)->Unit,
    type:TransactionType = TransactionType.Income
){
    val categories =  uiState.categories.filter { it.transaction_type==type }
    val isAddCategoryDialogOpen = remember { mutableStateOf(false) }
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )

    AddCategoryDialog(
        isShown =  isAddCategoryDialogOpen.value,
        onDialogDismiss = { isAddCategoryDialogOpen.value = false },
        onConfirmClick = {
            val category = TransactionCategory(
                id = 1,
                name = it,
                transaction_type = type,
                created_at =  System.currentTimeMillis()
            )
            addCategory(category)
            isAddCategoryDialogOpen.value = false
        } )

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        snackbarHost =  { SuBuuSnackBarHost(hostState = it) },
        floatingActionButton = {
                Box(
                    modifier = Modifier
                        .absolutePadding(bottom = 100.dp, left = 30.dp)
                ) {
                    TextButton(
                        onClick = {
                            isAddCategoryDialogOpen.value = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .padding(0.dp)
                            .background(positiveColor)
                    ) {
                        Text(text = "Add New Income Category", style = MaterialTheme.typography.button, color = MaterialTheme.colors.onPrimary)
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
        },
        sheetContent = {
            Text(text = "")
        }, sheetPeekHeight = 0.dp

    ) {
        Text(text = "Income Categories List :", style = MaterialTheme.typography.h6, modifier = Modifier.absolutePadding(top = 30.dp, bottom = 20.dp))

        LazyColumn(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.absolutePadding(left = 2.dp, right = 2.dp, bottom = 100.dp)
        ) {
            items(categories){
                CategoryItem(category = it, itemColor = positiveColor)
            }
        }
    }
    SuBuuSnackBar(
        onDismissSnackBar = { clearSnackBar() },
        scaffoldState = bottomSheetScaffoldState,
        snackBarType = uiState.currentSnackBar,
    )

}


@Composable
fun CategoryItem(category:TransactionCategory,modifier: Modifier=Modifier,itemColor:Color){
        Card(elevation = 2.dp, contentColor = itemColor, modifier = modifier) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp), verticalArrangement = Arrangement.Center) {
                Text(text = category.name)
            }
        }
    Divider(Modifier.height(10.dp), color = Color.Transparent)

}