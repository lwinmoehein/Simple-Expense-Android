package lab.justonebyte.moneysubuu.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import lab.justonebyte.moneysubuu.ui.theme.MoneySuBuuTheme

@Composable
fun SuBuuApp(
) {
    MoneySuBuuTheme {
        ProvideWindowInsets {
            val systemUiController = rememberSystemUiController()
            SideEffect {
                systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = false)
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
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                        IconButton(onClick = {
                            if(scaffoldState.drawerState.isOpen){
                                coroutineScope.launch { scaffoldState.drawerState.close() }
                            }else{
                                coroutineScope.launch { scaffoldState.drawerState.open() }
                            }
                        }) {
                            Icon(imageVector = Icons.Filled.Menu, contentDescription = "open menu")
                        }
                    }
                },
                scaffoldState = scaffoldState,
                drawerContent = {
                    AppDrawer(
                        currentRoute = currentRoute,
                        navigateToHome = { navController.navigate(MainDestinations.HOME_ROUTE) },
                        navigateToSettings = { navController.navigate(MainDestinations.SETTINGS_ROUTE) },
                        closeDrawer = { coroutineScope.launch { scaffoldState.drawerState.close() } }
                    )
                }
            ) {
                SuBuuNavGraph(
                    navController = navController,
                    scaffoldState = scaffoldState
                )
            }
        }
    }
}