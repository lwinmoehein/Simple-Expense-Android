package lab.justonebyte.moneysubuu.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import lab.justonebyte.moneysubuu.model.TransactionType
import lab.justonebyte.moneysubuu.ui.detail.TransactionDetailScreen
import lab.justonebyte.moneysubuu.ui.home.HomeScreen
import lab.justonebyte.moneysubuu.ui.home.HomeTab
import lab.justonebyte.moneysubuu.ui.settings.SettingsScreen
import lab.justonebyte.moneysubuu.utils.dateFormatter

/**
 * Destinations used in the ([JetnewsApp]).
 */
object MainDestinations {
    const val HOME_ROUTE = "home"
    const val DETAIL_ROUTE = "detail/{type}/{tab}/{date}"
    const val SETTINGS_ROUTE = "settings"
    fun getDetailRoute(type:Int,tab:Int,date:String):String{
        return "detail/${type}/${tab}/${date}"
    }
}

@Composable
fun SuBuuNavGraph(
    paddings: PaddingValues,
    navController: NavHostController = rememberNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    startDestination: String = MainDestinations.HOME_ROUTE
) {
    val coroutineScope = rememberCoroutineScope()
    val openDrawer: () -> Unit = { coroutineScope.launch { scaffoldState.drawerState.open() } }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(MainDestinations.HOME_ROUTE) {
            HomeScreen(
                goToPieChartDetail = {type, tab, date ->
                    navController.navigate(MainDestinations.getDetailRoute(type,tab,date))
                }
            )
        }
        composable(MainDestinations.SETTINGS_ROUTE) {
            SettingsScreen(
                openDrawer = openDrawer,
            )
        }
        composable(
            MainDestinations.DETAIL_ROUTE,
            arguments = listOf(
                navArgument("type") { type = NavType.IntType },
                navArgument("tab") { type = NavType.IntType },
                navArgument("date") { type = NavType.StringType },
                )
        ) {
            val transactionType =  when(navController.currentBackStackEntry?.arguments?.getInt("type")){
                TransactionType.Income.value->TransactionType.Income
                else->TransactionType.Expense
            }
            val tabType =  when(navController.currentBackStackEntry?.arguments?.getInt("tab")){
                HomeTab.Daily.index->HomeTab.Daily
                HomeTab.Monthly.index->HomeTab.Monthly
                HomeTab.Yearly.index->HomeTab.Yearly
                else->HomeTab.Total
            }
            val date =  navController.currentBackStackEntry?.arguments?.getString("date")

            TransactionDetailScreen(
                goBack = { navController.popBackStack() },
                transactionType = transactionType,
                tabType = tabType,
                dateData = date?: dateFormatter(System.currentTimeMillis())
            )
        }
    }
}

