package lab.justonebyte.moneysubuu.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import lab.justonebyte.moneysubuu.ui.detail.TransactionDetailScreen
import lab.justonebyte.moneysubuu.ui.home.HomeScreen
import lab.justonebyte.moneysubuu.ui.home.HomeViewModel
import lab.justonebyte.moneysubuu.ui.settings.SettingsScreen

/**
 * Destinations used in the ([JetnewsApp]).
 */
object MainDestinations {
    const val HOME_ROUTE = "home"
    const val DETAIL_ROUTE = "detail"
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
                openDrawer = openDrawer,
            )
        }
        composable(MainDestinations.SETTINGS_ROUTE) {
            SettingsScreen(
                openDrawer = openDrawer,
            )
        }
        composable(MainDestinations.DETAIL_ROUTE) {
            TransactionDetailScreen(
                openDrawer = openDrawer
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
    val goToDetail:()->Unit = {
        navController.navigate(MainDestinations.DETAIL_ROUTE)
    }
}