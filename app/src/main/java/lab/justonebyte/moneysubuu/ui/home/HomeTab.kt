package lab.justonebyte.moneysubuu.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import lab.justonebyte.moneysubuu.model.Transaction

sealed class HomeTab(val index:Int,val title:String){
    object  Daily:HomeTab(1,"Daily")
    object Monthly:HomeTab(2,"monthly")
    object Yearly:HomeTab(3,"Yearly")
    object Total:HomeTab(4,"Total")
}
val tabs = listOf(
    HomeTab.Daily,HomeTab.Monthly,HomeTab.Yearly,HomeTab.Total
)

@ExperimentalPagerApi
@Composable
fun HomeTabs(
    goToPieChart:(type:Int,tab:Int,date:String)->Unit,
    homeUiState:HomeUiState,
    onTabChanged:(BalanceType)->Unit,
    collectBalanceOfDay:(day:String)->Unit,
    selectedBalanceType: BalanceType,
    onMonthChoose:()->Unit,
    onTransactionClick:(transaction:Transaction)->Unit
) {

    var tabIndex by remember { mutableStateOf(1) }
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState) {
        // Collect from the pager state a snapshotFlow reading the currentPage
        snapshotFlow { pagerState.currentPage }.collect { page ->
            val balanceType = when(pagerState.currentPage+1){
                HomeTab.Daily.index->BalanceType.DAILY
                HomeTab.Monthly.index->BalanceType.MONTHLY
                HomeTab.Yearly.index->BalanceType.YEARLY
                else->BalanceType.TOTAL
            }
            onTabChanged(balanceType)
        }
    }

    Column {
        TabRow(
            backgroundColor = MaterialTheme.colors.primary,
            selectedTabIndex = tabIndex,
            indicator = { tabPositions -> // 3.
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(
                        pagerState,
                        tabPositions
                    ),
                    color = MaterialTheme.colors.primary
                )
            }) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = tabIndex == index,
                    onClick = {
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
            Card {
                HomeContent(
                    goToPieChart = { type, tab, date ->
                        goToPieChart(type,tab,date)
                    },
                    homeUiState = homeUiState,
                    collectBalanceOfDay = {
                        collectBalanceOfDay(it)
                    },
                    balanceType = selectedBalanceType,
                    onMonthChoose = {
                        onMonthChoose()
                    },
                    onTransactionClick = {
                        onTransactionClick(it)
                    }
                )
            }

        }
    }
}