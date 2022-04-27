package lab.justonebyte.moneysubuu.ui.home

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.util.lerp
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch
import lab.justonebyte.moneysubuu.model.Transaction
import kotlin.math.absoluteValue

sealed class HomeTab(val index:Int,val title:String){
    object  Daily:HomeTab(0,"Daily")
    object Monthly:HomeTab(1,"monthly")
    object Yearly:HomeTab(2,"Yearly")
    object Total:HomeTab(3,"Total")
}
val tabs = listOf(
    HomeTab.Daily,HomeTab.Monthly,HomeTab.Yearly,HomeTab.Total
)

@ExperimentalPagerApi
@Composable
fun HomeTabs(
    goToIncomeDetail:()->Unit,
    goToExpenseDetail:()->Unit,
    homeUiState:HomeUiState,
    onTabChanged:(BalanceType)->Unit,
    collectBalanceOfDay:(day:String)->Unit,
    selectedBalanceType: BalanceType,
    onMonthChoose:()->Unit,
    onTransactionClick:(transaction:Transaction)->Unit
) {

    var tabIndex by remember { mutableStateOf(0) }
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState) {
        // Collect from the pager state a snapshotFlow reading the currentPage
        snapshotFlow { pagerState.currentPage }.collect { page ->
            val balanceType = when(pagerState.currentPage){
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
            backgroundColor = Color.Transparent,
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
                        Log.i("tab:","selected")
                    },
                    text = { Text(text = tab.title) }
                )
            }
        }
        HorizontalPager( // 4.
            count = tabs.size,
            state = pagerState,
        ) { tabIndex ->
            Card(
                Modifier
                    .graphicsLayer {
                        // Calculate the absolute offset for the current page from the
                        // scroll position. We use the absolute value which allows us to mirror
                        // any effects for both directions
                        val pageOffset = calculateCurrentOffsetForPage(tabIndex).absoluteValue

                        // We animate the scaleX + scaleY, between 85% and 100%
                        lerp(
                            start = 0.85f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        ).also { scale ->
                            scaleX = scale
                            scaleY = scale
                        }

                        // We animate the alpha, between 50% and 100%
                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }
            ) {
                HomeContent(
                    goToExpenseDetail={goToExpenseDetail()},
                    goToIncomeDetail = {goToIncomeDetail()},
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