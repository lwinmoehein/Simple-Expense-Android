package lab.justonebyte.moneysubuu.ui.category

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import lab.justonebyte.moneysubuu.ui.theme.positiveColor

sealed class CategoryTab(val index:Int,val title:String){
    object  Income:CategoryTab(1,"Income")
    object Expense:CategoryTab(2,"Expense")
}
val tabs = listOf(
    CategoryTab.Income,CategoryTab.Expense
)

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

@Composable
fun IncomeCategoryTab(uiState: CategoryUiState){
    val incomeCategories =  uiState.categories.filter { it.transaction_type==TransactionType.Income }
    Column(verticalArrangement = Arrangement.Top) {
        Text(text = "income")
        incomeCategories.forEach{
            CategoryItem(category = it)
        }
    }
}

@Composable
fun ExpenseCategoryTab(uiState: CategoryUiState){
    val expenseCategories =  uiState.categories.filter { it.transaction_type==TransactionType.Expense }
    Column(verticalArrangement = Arrangement.Top) {
        Text(text = "ex")

        expenseCategories.forEach{
            CategoryItem(category = it)
        }
    }
}
@Composable
fun CategoryItem(category:TransactionCategory){
        Card(elevation = 2.dp, backgroundColor = positiveColor) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(5.dp), verticalArrangement = Arrangement.Center) {
                Text(text = category.name, color = MaterialTheme.colors.onPrimary)
            }
        }
    Divider(Modifier.height(10.dp), color = Color.Transparent)

}