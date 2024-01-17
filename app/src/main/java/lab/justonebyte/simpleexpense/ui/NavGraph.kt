package lab.justonebyte.simpleexpense.ui

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import lab.justonebyte.simpleexpense.ui.account.AccountScreen
import lab.justonebyte.simpleexpense.ui.account.AcknowledgeScreen
import lab.justonebyte.simpleexpense.ui.account.ExportScreen
import lab.justonebyte.simpleexpense.ui.account.PrivacyPolicyScreen
import lab.justonebyte.simpleexpense.ui.account.SettingsScreen
import lab.justonebyte.simpleexpense.ui.account.TermsAndServicesScreen
import lab.justonebyte.simpleexpense.ui.category.ManageCategoryScreen
import lab.justonebyte.simpleexpense.ui.home.HomeScreen
import lab.justonebyte.simpleexpense.ui.home.HomeViewModel
import lab.justonebyte.simpleexpense.ui.onboarding.LoginScreen
import lab.justonebyte.simpleexpense.ui.onboarding.OnBoardingScreen
import lab.justonebyte.simpleexpense.ui.stats.StatsScreen


object MainDestinations {
    const val ONBOARDING = "onboard"
    const val LOGIN = "login"
    const val HOME_ROUTE = "home"
    const val STATS_ROUTE = "stats"
    const val CATEGORY_ROUTE = "category"
    const val ACCOUNT_ROUTE = "account"
    const val ME_ROUTE = "me"
    const val SETTINGS = "settings"
    const val EXPORT = "export"
    const val ACKNOWLEDGEMENT = "acknowledgement"
    const val PRIVACY_POLICY = "privacy_policy"
    const val TERM_AND_SERVICE = "terms_and_services"
}

@Composable
fun NavGraph(
    paddings: PaddingValues,
    navController: NavHostController = rememberNavController(),
    startDestination: String = MainDestinations.ONBOARDING,
    chooseDownloadFolderLauncher: ActivityResultLauncher<Intent>,
    homeViewModel: HomeViewModel,
    onOnboardStartClick:()->Unit
) {

    NavHost(
        modifier = Modifier.padding(paddings) ,
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(MainDestinations.ONBOARDING) {
            OnBoardingScreen(
                onStartClick = {
                               onOnboardStartClick()
                },
            )
        }

        composable(MainDestinations.LOGIN) {
            LoginScreen(
                navController = navController
            )
        }
        composable(MainDestinations.HOME_ROUTE) {
            HomeScreen(
                homeViewModel = homeViewModel
            )
        }
        composable(MainDestinations.STATS_ROUTE) {
            StatsScreen()
        }
        composable(MainDestinations.CATEGORY_ROUTE) {
            ManageCategoryScreen()
        }
        addNestedGraph(navController,chooseDownloadFolderLauncher)
    }
}
fun NavGraphBuilder.addNestedGraph(
    navController: NavController,
    chooseDownloadFolderLauncher: ActivityResultLauncher<Intent>
) {
    navigation(startDestination = MainDestinations.ME_ROUTE, route = MainDestinations.ACCOUNT_ROUTE) {
        composable(MainDestinations.ME_ROUTE) {
            AccountScreen(
                navController = navController
            )
        }
        composable(MainDestinations.SETTINGS) {
            SettingsScreen(
                navController = navController
            )
        }
        composable(MainDestinations.EXPORT) {
            ExportScreen(
                navController = navController,
                chooseDownloadFolderLauncher=chooseDownloadFolderLauncher
            )
        }
        composable(MainDestinations.ACKNOWLEDGEMENT) {
            AcknowledgeScreen(
                navController = navController
            )
        }
        composable(MainDestinations.PRIVACY_POLICY) {
            PrivacyPolicyScreen(
                navController = navController
            )
        }
        composable(MainDestinations.TERM_AND_SERVICE) {
            TermsAndServicesScreen(
                navController = navController
            )
        }
    }
}

