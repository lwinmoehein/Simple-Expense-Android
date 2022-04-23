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
import lab.justonebyte.moneysubuu.ui.home.HomeScreen
import lab.justonebyte.moneysubuu.ui.home.HomeViewModel
import lab.justonebyte.moneysubuu.ui.settings.SettingsScreen

/**
 * Destinations used in the ([JetnewsApp]).
 */
object MainDestinations {
    const val HOME_ROUTE = "home"
    const val SETTINGS_ROUTE = "settings"
}

@Composable
fun SuBuuNavGraph(
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
            val homeViewModel = hiltViewModel<HomeViewModel>()
            val homeUiState by homeViewModel.viewModelUiState.collectAsState()

            HomeScreen(
                openDrawer = openDrawer,
                homeUiState = homeUiState
            )
        }
        composable(MainDestinations.SETTINGS_ROUTE) {
            SettingsScreen(
                openDrawer = openDrawer,

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
}