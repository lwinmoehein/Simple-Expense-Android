package lab.justonebyte.simpleexpense.ui

import AppTheme
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import compose.icons.FeatherIcons
import compose.icons.feathericons.Home
import compose.icons.feathericons.List
import compose.icons.feathericons.PieChart
import compose.icons.feathericons.User
import lab.justonebyte.simpleexpense.R

enum class NavItem(val stringResource:Int,val imageVector:ImageVector){
    HOME(R.string.home,FeatherIcons.Home),
    CHARTS(R.string.charts,FeatherIcons.PieChart),
    CATEGORIES(R.string.m_categories,FeatherIcons.List),
    ACCOUNT(R.string.account,FeatherIcons.User)
}

val appContentPadding = 20.dp
@RequiresApi(Build.VERSION_CODES.O)
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
                                icon = {
                                    Icon(
                                        imageVector = item.imageVector,
                                        contentDescription = ""
                                    )
                                },
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