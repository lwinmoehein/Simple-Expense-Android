package lab.justonebyte.simpleexpense.ui

import AppTheme
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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.canopas.lib.showcase.IntroShowcase
import com.canopas.lib.showcase.component.ShowcaseStyle
import com.google.accompanist.insets.ProvideWindowInsets
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
import lab.justonebyte.simpleexpense.ui.onboarding.OnBoarding
import java.util.Locale

enum class NavItem(val stringResource:Int,val imageVector:ImageVector,val showCase: ShowCase?=null){
    HOME(R.string.home,FeatherIcons.Home),
    CHARTS(R.string.charts,FeatherIcons.PieChart,ShowCase.CHARTS),
    CATEGORIES(R.string.m_categories,FeatherIcons.List,ShowCase.MANAGE_CATEGORIES),
    ACCOUNT(R.string.account,FeatherIcons.User,ShowCase.ACCOUNT)
}

val appContentPadding = 20.dp
@Composable
fun SimpleExpenseApp(
    chooseDownloadFolderLauncher: ActivityResultLauncher<Intent>,

) {
    val configuration = LocalConfiguration.current
    var selectedItem by remember { mutableStateOf(0) }
    val navItems = listOf(NavItem.HOME,NavItem.CHARTS,NavItem.CATEGORIES,NavItem.ACCOUNT)
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val homeUiState by homeViewModel.viewModelUiState.collectAsState()

    val coroutineScope = rememberCoroutineScope()


    AppTheme() {
        ProvideWindowInsets {

            val navController = rememberNavController()

            if(homeUiState.isAppAlreadyIntroduced != true){
                OnBoarding()
            }else{
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            navItems.forEachIndexed { index, item ->
                                if(index!=0){
                                    val showcaseTitle = when(index){
                                        1-> stringResource(id = R.string.showcase_chart)
                                        2-> stringResource(id = R.string.showcase_manage_category)
                                        else -> stringResource(id = R.string.showcase_settings)
                                    }
                                    val showcaseDescription = when(index){
                                        1-> stringResource(id = R.string.showcase_chart_description)
                                        2-> stringResource(id = R.string.showcase_manage_category_description)
                                        else -> stringResource(id = R.string.showcase_settings_description)
                                    }
                                    IntroShowcase(
                                        showIntroShowCase = item.showCase==homeUiState.currentAppShowcaseStep,
                                        dismissOnClickOutside = true,
                                        onShowCaseCompleted = {
                                            if(item.showCase!=ShowCase.ACCOUNT){
                                                navItems[index+1].showCase?.let {
                                                    homeViewModel.updateAppIntroStep(
                                                        it
                                                    )
                                                }
                                            }
                                            if(item.showCase==ShowCase.ACCOUNT){
                                                coroutineScope.launch {
                                                    homeViewModel.changeIsAppIntroducedToTrue()
                                                }
                                            }
                                        },
                                    ){
                                        NavigationBarItem(
                                            modifier = Modifier.introShowCaseTarget(
                                                index = 0,
                                                style = ShowcaseStyle.Default.copy(
                                                    backgroundColor = MaterialTheme.colorScheme.primary, // specify color of background
                                                    backgroundAlpha = 0.98f, // specify transparency of background
                                                    targetCircleColor = Color.White // specify color of target circle
                                                ),
                                                // specify the content to show to introduce app feature
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
                                                    tint = if (selectedItem == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                                                )
                                            },
                                            label = {
                                                Text(
                                                    text = stringResource(id = item.stringResource),
                                                    color = if (selectedItem == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                                                )
                                            },
                                            selected = selectedItem == index,
                                            onClick = {
                                                selectedItem = index
                                                when (selectedItem) {
                                                    0 -> navController.navigate(MainDestinations.HOME_ROUTE)
                                                    1 -> navController.navigate(MainDestinations.STATS_ROUTE)
                                                    2 -> navController.navigate(MainDestinations.CATEGORY_ROUTE)
                                                    else -> navController.navigate(MainDestinations.ACCOUNT_ROUTE)
                                                }
                                            }
                                        )
                                    }
                                }else{
                                    NavigationBarItem(
                                        icon = {
                                            Icon(
                                                imageVector = item.imageVector,
                                                contentDescription = "",
                                                tint = if (selectedItem == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = stringResource(id = item.stringResource),
                                                color = if (selectedItem == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                                            )
                                        },
                                        selected = selectedItem == index,
                                        onClick = {
                                            selectedItem = index
                                            when (selectedItem) {
                                                0 -> navController.navigate(MainDestinations.HOME_ROUTE)
                                                1 -> navController.navigate(MainDestinations.STATS_ROUTE)
                                                2 -> navController.navigate(MainDestinations.CATEGORY_ROUTE)
                                                else -> navController.navigate(MainDestinations.ACCOUNT_ROUTE)
                                            }
                                        }
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
                            homeViewModel = homeViewModel
                        )
                }
            }
        }
    }
}