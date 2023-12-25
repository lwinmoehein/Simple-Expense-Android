package lab.justonebyte.simpleexpense.ui

import lab.justonebyte.simpleexpense.ui.stats.StatsScreen
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import lab.justonebyte.simpleexpense.ui.category.ManageCategoryScreen
import lab.justonebyte.simpleexpense.ui.home.SuBuuAppHomeScreen
import lab.justonebyte.simpleexpense.ui.account.AccountScreen

/**
 * Destinations used in the ([JetnewsApp]).
 */
object MainDestinations {
    const val HOME_ROUTE = "home"
    const val STATS_ROUTE = "stats"
    const val ACCOUNT_ROUTE = "account"
    const val CATEGORY_ROUTE = "category"
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SuBuuNavGraph(
    paddings: PaddingValues,
    navController: NavHostController = rememberNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    startDestination: String = MainDestinations.HOME_ROUTE,
) {
    val coroutineScope = rememberCoroutineScope()
    val openDrawer: () -> Unit = { coroutineScope.launch { scaffoldState.drawerState.open() } }

    NavHost(
        modifier = Modifier.padding(paddings) ,
        navController = navController,
        startDestination = startDestination,
    ) {

        composable(MainDestinations.HOME_ROUTE) {
            SuBuuAppHomeScreen(navController=navController)
        }

        composable(MainDestinations.ACCOUNT_ROUTE) {
            AccountScreen(
                openDrawer = openDrawer,
            )
        }
        composable(MainDestinations.STATS_ROUTE) {
            StatsScreen(goBack = {navController.popBackStack()} )
        }
        composable(MainDestinations.CATEGORY_ROUTE) {
            ManageCategoryScreen(goBack = {navController.popBackStack()} )
        }
    }
}
