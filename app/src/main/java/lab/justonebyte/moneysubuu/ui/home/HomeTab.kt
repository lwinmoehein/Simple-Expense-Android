package lab.justonebyte.moneysubuu.ui.home

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

sealed class HomeTab(val index:Int,val title:String){
    object  Daily:HomeTab(0,"Daily")
    object Weekly:HomeTab(1,"Weekly")
    object Monthly:HomeTab(2,"monthly")
    object Yearly:HomeTab(3,"Yearly")
}
val tabs = listOf(
    HomeTab.Daily,HomeTab.Weekly,HomeTab.Monthly,HomeTab.Yearly
)

@ExperimentalPagerApi
@Composable
fun HomeTabs(
    homeUiState:HomeUiState,
    onOpenBottomSheet:()->Unit,
    onTabChanged:(BalanceType)->Unit
) {

    var tabIndex by remember { mutableStateOf(0) }
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState) {
        // Collect from the pager state a snapshotFlow reading the currentPage
        snapshotFlow { pagerState.currentPage }.collect { page ->
            val balanceType = when(pagerState.currentPage){
                HomeTab.Yearly.index->BalanceType.YEARLY
                HomeTab.Weekly.index->BalanceType.WEEKLY
                HomeTab.Monthly.index->BalanceType.MONTHLY
                else->BalanceType.DAILY
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
            HomeContent(
                onOpenBottomSheet = {
                                    onOpenBottomSheet()
                },
                homeUiState = homeUiState,
             )
        }
    }
}