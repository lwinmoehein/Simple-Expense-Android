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
import lab.justonebyte.moneysubuu.ui.home.BalanceType
import lab.justonebyte.moneysubuu.ui.home.HomeScreen
import lab.justonebyte.moneysubuu.ui.home.HomeTab
import lab.justonebyte.moneysubuu.ui.settings.SettingsScreen
import lab.justonebyte.moneysubuu.utils.dateFormatter

/**
 * Destinations used in the ([JetnewsApp]).
 */
object MainDestinations {
    const val HOME_ROUTE = "home"
    const val DETAIL_ROUTE = "detail/{tType}/{bType}/{date}"
    const val SETTINGS_ROUTE = "settings"
    fun getDetailRoute(tType:Int,bType:Int,date:String):String{
        return "detail/${tType}/${bType}/${date}"
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
                goToPieChartDetail = {transactionType,balanceType, date ->
                    navController.navigate(MainDestinations.getDetailRoute(transactionType.value,balanceType.value,date))
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
                navArgument("tType") { type = NavType.IntType },
                navArgument("bType") { type = NavType.IntType },
                navArgument("date") { type = NavType.StringType },
                )
        ) {
            val transactionType =  when(navController.currentBackStackEntry?.arguments?.getInt("tType")){
                TransactionType.Income.value->TransactionType.Income
                else->TransactionType.Expense
            }
            val balanceType =  when(navController.currentBackStackEntry?.arguments?.getInt("bType")){
                BalanceType.DAILY.value->BalanceType.DAILY
                BalanceType.MONTHLY.value->BalanceType.MONTHLY
                BalanceType.YEARLY.value->BalanceType.YEARLY
                else->BalanceType.TOTAL
            }
            val date =  navController.currentBackStackEntry?.arguments?.getString("date")

            TransactionDetailScreen(
                goBack = { navController.popBackStack() },
                transactionType = transactionType,
                balanceType =balanceType ,
                dateData = date?: dateFormatter(System.currentTimeMillis())
            )
        }
    }
}

