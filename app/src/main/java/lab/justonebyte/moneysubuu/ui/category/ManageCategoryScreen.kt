package lab.justonebyte.moneysubuu.ui.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import lab.justonebyte.moneysubuu.ui.theme.negativeColor
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
    onTabChanged:(CategoryTab)->Unit
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
        modifier = Modifier.padding(20.dp)
    ) {
        TabRow(
            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier.clip(RoundedCornerShape(10.dp)),
            selectedTabIndex = tabIndex,
            indicator = { tabPositions -> // 3.
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(
                        pagerState,
                        tabPositions
                    ),
                    color = MaterialTheme.colors.secondary,
                    height = 3.dp
                )
            }) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selectedContentColor = MaterialTheme.colors.secondary,
                    unselectedContentColor = MaterialTheme.colors.onPrimary,
                    selected = false,
                    onClick = {
                        currentTab.value = tab
                        coroutineScope.launch { pagerState.animateScrollToPage(index) }
                    },
                    text = { Text(text = tab.title) }
                )
            }
        }
        HorizontalPager( // 4.
            count = tabs.size,
            state = pagerState,
        ) { tabIndex ->



                    if(currentTab.value===CategoryTab.Expense){
                        ExpenseCategoryTab(
                            uiState =  categoryUiState
                        )
                    }else{
                        IncomeCategoryTab(
                            uiState = categoryUiState
                        )
                    }

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

   CategoryTabs(
       categoryUiState = categoryUiState,
       onTabChanged = {}
   )


}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun IncomeCategoryTab(uiState: CategoryUiState){
    val incomeCategories =  uiState.categories.filter { it.transaction_type==TransactionType.Income }
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )

    BottomSheetScaffold(
        floatingActionButton = {
                Box(
                    modifier = Modifier
                        .absolutePadding(bottom = 100.dp, left = 30.dp)
                ) {
                    TextButton(
                        onClick = {
                            coroutineScope.launch {
                                bottomSheetScaffoldState.bottomSheetState.expand()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .padding(0.dp)
                            .background(MaterialTheme.colors.primary)
                    ) {
                        Text(text = "Add New Record", style = MaterialTheme.typography.button, color = MaterialTheme.colors.onPrimary)
                        Icon(
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                            ,
                            imageVector = Icons.Filled.Add, contentDescription = "add transaction",
                            tint = MaterialTheme.colors.onPrimary
                        )
                    }
                }
        },
        scaffoldState = bottomSheetScaffoldState,
        sheetElevation = 8.dp,
        sheetShape = RoundedCornerShape(
            bottomStart = 0.dp,
            bottomEnd = 0.dp,
            topStart = 12.dp,
            topEnd = 12.dp
        ),

        sheetContent = {
            Text(text = "hello")
        }, sheetPeekHeight = 0.dp
    ) {
        Column(verticalArrangement = Arrangement.Top) {
            Text(text = "Income Categories List", style = MaterialTheme.typography.subtitle1, modifier = Modifier.absolutePadding(top = 30.dp, bottom = 20.dp))
            incomeCategories.forEach{
                CategoryItem(category = it, itemColor = positiveColor)
            }
        }
    }

}

@Composable
fun ExpenseCategoryTab(uiState: CategoryUiState){
    val expenseCategories =  uiState.categories.filter { it.transaction_type==TransactionType.Expense }
    Column(verticalArrangement = Arrangement.Top) {
        Text(text = "Expense Categories List", style = MaterialTheme.typography.subtitle1, modifier = Modifier.absolutePadding(top = 30.dp, bottom = 20.dp))
        expenseCategories.forEach{
            CategoryItem(category = it, itemColor = negativeColor)
        }
    }
}
@Composable
fun CategoryItem(category:TransactionCategory,modifier: Modifier=Modifier,itemColor:Color){
        Card(elevation = 5.dp, contentColor = itemColor, modifier = modifier) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp), verticalArrangement = Arrangement.Center) {
                Text(text = category.name)
            }
        }
    Divider(Modifier.height(10.dp), color = Color.Transparent)

}