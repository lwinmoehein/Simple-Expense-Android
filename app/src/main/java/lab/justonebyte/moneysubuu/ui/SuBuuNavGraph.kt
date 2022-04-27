package lab.justonebyte.moneysubuu.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import lab.justonebyte.moneysubuu.model.TransactionType
import lab.justonebyte.moneysubuu.ui.detail.TransactionDetailScreen
import lab.justonebyte.moneysubuu.ui.home.HomeScreen
import lab.justonebyte.moneysubuu.ui.settings.SettingsScreen

/**
 * Destinations used in the ([JetnewsApp]).
 */
object MainDestinations {
    const val HOME_ROUTE = "home"
    const val INCOME_DETAIL_ROUTE = "income_detail"
    const val EXPENSE_DETAIL_ROUTE = "income_detail"
    const val SETTINGS_ROUTE = "settings"
}

@Composable
fun SuBuuNavGraph(
    paddings: PaddingValues,
    navController: NavHostController = rememberNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    startDestination: String = MainDestinations.HOME_ROUTE
) {
    val actions = remember(navController) { MainActions(navController) }
    val coroutineScope = rememberCoroutineScope()
    val openDrawer: () -> Unit = { coroutineScope.launch { scaffoldState.drawerState.open() } }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(MainDestinations.HOME_ROUTE) {
            HomeScreen(
                actions = actions,
                openDrawer = openDrawer,
                goToDetails = {
                    actions.goToIncomeDetail()
                }
            )
        }
        composable(MainDestinations.SETTINGS_ROUTE) {
            SettingsScreen(
                openDrawer = openDrawer,
            )
        }
        composable(MainDestinations.INCOME_DETAIL_ROUTE) {
            TransactionDetailScreen(
                openDrawer = openDrawer
            )
        }
        composable(MainDestinations.EXPENSE_DETAIL_ROUTE) {
            TransactionDetailScreen(
                openDrawer = openDrawer,
                transactionType = TransactionType.Expense
            )
        }
    }
}

/**
 * Models the navigation actions in the app.
 */
class MainActions(navController: NavHostController) {
    val upPress: () -> Unit = {
        navController.navigateUp()
    }
    val goToIncomeDetail:()->Unit = {
        navController.navigate(MainDestinations.INCOME_DETAIL_ROUTE){
            launchSingleTop = true
            restoreState = true
        }
    }
    val goToExpenseDetail:()->Unit = {
        navController.navigate(MainDestinations.EXPENSE_DETAIL_ROUTE){
            launchSingleTop = true
            restoreState = true
        }
    }
}