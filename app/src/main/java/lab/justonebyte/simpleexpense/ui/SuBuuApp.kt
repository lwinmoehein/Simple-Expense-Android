package lab.justonebyte.simpleexpense.ui

import AppTheme
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import lab.justonebyte.simpleexpense.R

enum class NavItem(val stringResource:Int,val iconResource:Int){
    HOME(R.string.home,R.drawable.ic_round_home_24),
    CHARTS(R.string.charts,R.drawable.ic_round_pie_chart_24),
    CATEGORIES(R.string.m_categories,R.drawable.ic_round_category_24),
    ACCOUNT(R.string.account,R.drawable.ic_baseline_account_circle_24)
}

val appContentPadding = 20.dp
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuBuuApp(chooseDownloadFolderLauncher: ActivityResultLauncher<Intent>) {

    var selectedItem by remember { mutableStateOf(0) }
    val navItems = listOf(NavItem.HOME,NavItem.CHARTS,NavItem.CATEGORIES,NavItem.ACCOUNT)

    AppTheme() {
        ProvideWindowInsets {

            val navController = rememberNavController()
            val coroutineScope = rememberCoroutineScope()
            // This top level scaffold contains the app drawer, which needs to be accessible
            // from multiple screens. An event to open the drawer is passed down to each
            // screen that needs it.
            val scaffoldState = rememberScaffoldState()

            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route ?: MainDestinations.HOME_ROUTE

            Scaffold(

                bottomBar = {
                    NavigationBar {
                        navItems.forEachIndexed { index, item ->
                            NavigationBarItem(
                                icon = { Icon(painterResource(id = item.iconResource), contentDescription = "") },
                                label = { Text(stringResource(id = item.stringResource)) },
                                selected = selectedItem == index,
                                onClick = {
                                    selectedItem = index
                                    when(selectedItem){
                                        0-> navController.navigate(MainDestinations.HOME_ROUTE)
                                        1->navController.navigate(MainDestinations.STATS_ROUTE)
                                        2->navController.navigate(MainDestinations.CATEGORY_ROUTE)
                                        else-> navController.navigate(MainDestinations.ACCOUNT_ROUTE)
                                    }
                                }
                            )
                        }
                    }
                }
            ) {
                SuBuuNavGraph(
                    paddings = it,
                    navController = navController,
                    scaffoldState = scaffoldState,
                    chooseDownloadFolderLauncher = chooseDownloadFolderLauncher
                )
            }
        }
    }
}