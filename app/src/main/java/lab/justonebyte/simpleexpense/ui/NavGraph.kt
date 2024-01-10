package lab.justonebyte.simpleexpense.ui

import android.content.Intent
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import lab.justonebyte.simpleexpense.ui.account.AccountScreen
import lab.justonebyte.simpleexpense.ui.category.ManageCategoryScreen
import lab.justonebyte.simpleexpense.ui.home.SuBuuAppHomeScreen
import lab.justonebyte.simpleexpense.ui.stats.StatsScreen

object MainDestinations {
    const val HOME_ROUTE = "home"
    const val STATS_ROUTE = "stats"
    const val ACCOUNT_ROUTE = "account"
    const val CATEGORY_ROUTE = "category"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    paddings: PaddingValues,
    navController: NavHostController = rememberNavController(),
    startDestination: String = MainDestinations.HOME_ROUTE,
    chooseDownloadFolderLauncher: ActivityResultLauncher<Intent>
) {

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
                chooseDownloadFolderLauncher = chooseDownloadFolderLauncher
            )
        }
        composable(MainDestinations.STATS_ROUTE) {
            StatsScreen()
        }
        composable(MainDestinations.CATEGORY_ROUTE) {
            ManageCategoryScreen()
        }
    }
}

