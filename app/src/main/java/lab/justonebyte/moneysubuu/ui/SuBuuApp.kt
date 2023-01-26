package lab.justonebyte.moneysubuu.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import lab.justonebyte.moneysubuu.R
import lab.justonebyte.moneysubuu.ui.theme.MoneySuBuuTheme
import lab.justonebyte.moneysubuu.ui.theme.primaryVariant

val appContentPadding = 20.dp
@Composable
fun SuBuuApp() {
    MoneySuBuuTheme {
        ProvideWindowInsets {
            val systemUiController = rememberSystemUiController()
            SideEffect {
                systemUiController.setSystemBarsColor(primaryVariant, darkIcons = false)
            }

            val navController = rememberNavController()
            val coroutineScope = rememberCoroutineScope()
            // This top level scaffold contains the app drawer, which needs to be accessible
            // from multiple screens. An event to open the drawer is passed down to each
            // screen that needs it.
            val scaffoldState = rememberScaffoldState()

            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route ?: MainDestinations.HOME_ROUTE
            Scaffold(
                topBar = {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colors.primary)
                            .absolutePadding(
                                right = 20.dp,
                                left = 20.dp),
                        horizontalArrangement = Arrangement.Start,

                    ) {
                        IconButton(onClick = {

                            if(scaffoldState.drawerState.isOpen){
                                coroutineScope.launch { scaffoldState.drawerState.close() }
                            }else{
                                coroutineScope.launch { scaffoldState.drawerState.open() }
                            }
                        }) {
                            Image(
                                painterResource(id = R.drawable.ic_round_menu_24),
                                contentDescription = null
                            )
                        }
                    }
                },
                scaffoldState = scaffoldState,
                drawerContent = {
                    AppDrawer(
                        currentRoute = currentRoute,
                        navigateToHome = { navController.navigate(MainDestinations.HOME_ROUTE) },
                        navigateToStats = { navController.navigate(MainDestinations.STATS_ROUTE) },
                        navigateToCategory={navController.navigate(MainDestinations.CATEGORY_ROUTE)},
                        navigateToSettings = { navController.navigate(MainDestinations.SETTINGS_ROUTE) },
                        closeDrawer = { coroutineScope.launch { scaffoldState.drawerState.close() } },
                    )
                }
            ) {
                SuBuuNavGraph(
                    paddings = it,
                    navController = navController,
                    scaffoldState = scaffoldState,
                )
            }
        }
    }
}