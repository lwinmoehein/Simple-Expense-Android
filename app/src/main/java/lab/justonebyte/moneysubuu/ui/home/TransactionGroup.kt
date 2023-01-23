package lab.justonebyte.moneysubuu.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import lab.justonebyte.moneysubuu.model.Transaction
import lab.justonebyte.moneysubuu.ui.MainDestinations
import lab.justonebyte.moneysubuu.ui.budget.BudgetScreen
import lab.justonebyte.moneysubuu.ui.loan.LoanScreen

sealed class HomeTab(val index:Int, val title:String){
    object  Home:HomeTab(1,"Home")
    object Loans:HomeTab(2,"Loans")
    object Budgets:HomeTab(3,"Budgets")
}
val tabs = listOf(
    HomeTab.Home,HomeTab.Loans,HomeTab.Budgets
)

@ExperimentalPagerApi
@Composable
fun HomeTabs(
    homeUiState:HomeUiState,
    navController: NavController
) {

    var tabIndex by remember { mutableStateOf(1) }
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState) {
        // Collect from the pager state a snapshotFlow reading the currentPage
        snapshotFlow { pagerState.currentPage }.collect { page ->

        }
    }

    Column {
//        TabRow(
//            backgroundColor = MaterialTheme.colors.primary,
//            selectedTabIndex = tabIndex,
//            indicator = { tabPositions -> // 3.
//                TabRowDefaults.Indicator(
//                    Modifier.pagerTabIndicatorOffset(
//                        pagerState,
//                        tabPositions
//                    ),
//                    color = MaterialTheme.colors.primary
//                )
//            }) {
//            tabs.forEachIndexed { index, tab ->
//                Tab(
//                    selected = tabIndex == index,
//                    onClick = {
//                        coroutineScope.launch { pagerState.animateScrollToPage(index) }
//                    },
//                    text = { Text(text = tab.title) }
//                )
//            }
//        }
        HorizontalPager( // 4.
            count = tabs.size,
            state = pagerState,
        ) { tabIndex ->
            Card {
                when(tabIndex+1){
                    HomeTab.Home.index-> HomeScreen()
                    HomeTab.Loans.index -> LoanScreen()
                    else -> BudgetScreen()
                }
            }
        }
    }
}