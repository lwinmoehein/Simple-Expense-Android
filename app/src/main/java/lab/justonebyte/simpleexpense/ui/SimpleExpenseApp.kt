package lab.justonebyte.simpleexpense.ui

import AppTheme
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.canopas.lib.showcase.IntroShowcase
import com.canopas.lib.showcase.component.ShowcaseStyle
import compose.icons.FeatherIcons
import compose.icons.feathericons.Home
import compose.icons.feathericons.List
import compose.icons.feathericons.PieChart
import compose.icons.feathericons.User
import kotlinx.coroutines.launch
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.model.ShowCase
import lab.justonebyte.simpleexpense.ui.account.SettingsViewModel
import lab.justonebyte.simpleexpense.ui.home.HomeViewModel
import lab.justonebyte.simpleexpense.ui.onboarding.LoginScreen
import lab.justonebyte.simpleexpense.ui.onboarding.OnBoardingScreen
import lab.justonebyte.simpleexpense.utils.createIsOnboardDoneFlagFile
import lab.justonebyte.simpleexpense.utils.isOnboardingDoneFlagExist

enum class NavItem(
    val stringResource:Int,
    val imageVector:ImageVector,
    val showCase: ShowCase?=null,
    val route: String
){
    HOME(R.string.home,FeatherIcons.Home,null,MainDestinations.HOME_ROUTE),
    CHARTS(R.string.charts,FeatherIcons.PieChart,ShowCase.CHARTS,MainDestinations.STATS_ROUTE),
    CATEGORIES(R.string.m_categories,FeatherIcons.List,ShowCase.MANAGE_CATEGORIES, MainDestinations.CATEGORY_ROUTE),
    ACCOUNT(R.string.account,FeatherIcons.User,ShowCase.ACCOUNT, MainDestinations.ME_ROUTE)
}

val appContentPadding = 20.dp
@Composable
fun SimpleExpenseApp(
    chooseDownloadFolderLauncher: ActivityResultLauncher<Intent>,
    context: Context = LocalContext.current,
    settingsViewModel: SettingsViewModel
) {
    val navItems = listOf(NavItem.HOME, NavItem.CHARTS, NavItem.CATEGORIES, NavItem.ACCOUNT)
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val homeUiState by homeViewModel.viewModelUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val navController = rememberNavController()
    val isLoginShown = remember { mutableStateOf(false) }
    val isAppOnboardingShowed = remember { mutableStateOf(isOnboardingDoneFlagExist(context)) }

    AppTheme {
        if (!isAppOnboardingShowed.value) {
            if (!isLoginShown.value)
                OnBoardingScreen(onStartClick = {
                    isLoginShown.value = true
                })
            else
                LoginScreen {
                    isAppOnboardingShowed.value = true
                    createIsOnboardDoneFlagFile(context)
                }
        } else {
            Scaffold(
                bottomBar = {
                    NavigationBar {
                        // 2. GET THE CURRENT ROUTE from the NavController
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route

                        navItems.forEachIndexed { index, item ->
                            // 3. DETERMINE if the item is selected based on the current route
                            val isSelected = currentRoute == item.route

                            // 4. DEFINE the correct onClick action
                            val onClickAction = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }

                            if (index != 0) {
                                val showcaseTitle = when (index) {
                                    1 -> stringResource(id = R.string.showcase_chart)
                                    2 -> stringResource(id = R.string.showcase_manage_category)
                                    else -> stringResource(id = R.string.showcase_settings)
                                }
                                val showcaseDescription = when (index) {
                                    1 -> stringResource(id = R.string.showcase_chart_description)
                                    2 -> stringResource(id = R.string.showcase_manage_category_description)
                                    else -> stringResource(id = R.string.showcase_settings_description)
                                }
                                IntroShowcase(
                                    showIntroShowCase = item.showCase == homeUiState.currentAppShowcaseStep,
                                    dismissOnClickOutside = true,
                                    onShowCaseCompleted = {
                                        if (item.showCase != ShowCase.ACCOUNT) {
                                            navItems[index + 1].showCase?.let {
                                                homeViewModel.updateAppIntroStep(
                                                    it
                                                )
                                            }
                                        }
                                        if (item.showCase == ShowCase.ACCOUNT) {
                                            coroutineScope.launch {
                                                homeViewModel.changeIsAppIntroducedToTrue()
                                            }
                                        }
                                    },
                                ) {
                                    NavigationBarItem(
                                        modifier = Modifier.introShowCaseTarget(
                                            index = 0,
                                            style = ShowcaseStyle.Default.copy(
                                                backgroundColor = MaterialTheme.colorScheme.primary,
                                                backgroundAlpha = 0.98f,
                                                targetCircleColor = Color.White
                                            ),
                                            content = {
                                                Column {
                                                    Text(
                                                        text = showcaseTitle,
                                                        color = Color.White,
                                                        fontSize = 24.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    Text(
                                                        text = showcaseDescription,
                                                        color = Color.White,
                                                        fontSize = 16.sp
                                                    )

                                                }
                                            }
                                        ),
                                        icon = {
                                            Icon(
                                                imageVector = item.imageVector,
                                                contentDescription = "",
                                                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = stringResource(id = item.stringResource),
                                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                                            )
                                        },
                                        selected = isSelected,
                                        onClick = onClickAction
                                    )
                                }
                            } else {
                                NavigationBarItem(
                                    icon = {
                                        Icon(
                                            imageVector = item.imageVector,
                                            contentDescription = "",
                                            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = stringResource(id = item.stringResource),
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                                        )
                                    },
                                    selected = isSelected,
                                    onClick = onClickAction
                                )
                            }
                        }
                    }
                }
            ) {
                NavGraph(
                    paddings = it,
                    navController = navController,
                    chooseDownloadFolderLauncher = chooseDownloadFolderLauncher,
                    homeViewModel = homeViewModel,
                    startDestination = MainDestinations.HOME_ROUTE,
                    settingsViewModel = settingsViewModel
                )
            }
        }
    }
}