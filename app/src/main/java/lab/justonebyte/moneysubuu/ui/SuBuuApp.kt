package lab.justonebyte.moneysubuu.ui

import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import lab.justonebyte.moneysubuu.ui.theme.MoneySuBuuTheme
//
//@Composable
//fun SuBuuApp(
//) {
//    MoneySuBuuTheme {
//        ProvideWindowInsets {
//            val systemUiController = rememberSystemUiController()
//            SideEffect {
//                systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = false)
//            }
//
//            val navController = rememberNavController()
//            val coroutineScope = rememberCoroutineScope()
//            // This top level scaffold contains the app drawer, which needs to be accessible
//            // from multiple screens. An event to open the drawer is passed down to each
//            // screen that needs it.
//            val scaffoldState = rememberScaffoldState()
//
//            val navBackStackEntry by navController.currentBackStackEntryAsState()
//            val currentRoute = navBackStackEntry?.destination?.route ?: MainDestinations.HOME_ROUTE
//            Scaffold(
//                scaffoldState = scaffoldState,
//                drawerContent = {
//                    AppDrawer(
//                        currentRoute = currentRoute,
//                        navigateToHome = { navController.navigate(MainDestinations.HOME_ROUTE) },
//                        navigateToSettings = { navController.navigate(MainDestinations.SETTINGS_ROUTE) },
//                        closeDrawer = { coroutineScope.launch { scaffoldState.drawerState.close() } }
//                    )
//                }
//            ) {
//                SuBuuNavGraph(
//                    navController = navController,
//                    scaffoldState = scaffoldState
//                )
//            }
//        }
//    }
//}