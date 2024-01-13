package lab.justonebyte.simpleexpense.ui

import AppTheme
import android.content.Intent
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.canopas.lib.showcase.IntroShowcase
import com.canopas.lib.showcase.component.ShowcaseStyle
import com.google.accompanist.insets.ProvideWindowInsets
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowRight
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
@Composable
fun SimpleExpenseApp(chooseDownloadFolderLauncher: ActivityResultLauncher<Intent>) {

    var selectedItem by remember { mutableStateOf(0) }
    val navItems = listOf(NavItem.HOME,NavItem.CHARTS,NavItem.CATEGORIES,NavItem.ACCOUNT)

    AppTheme() {
        ProvideWindowInsets {

            val navController = rememberNavController()

            Scaffold(

                bottomBar = {
                    NavigationBar {
                        navItems.forEachIndexed { index, item ->
                            IntroShowcase(
                                showIntroShowCase = index==1,
                                dismissOnClickOutside = false,
                                onShowCaseCompleted = {
                                    //App Intro finished!!
                                },
                            ) {
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
                                    },
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
                                                    text = "Check emails",
                                                    color = Color.White,
                                                    fontSize = 24.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    text = "Click here to check/send emails",
                                                    color = Color.White,
                                                    fontSize = 16.sp
                                                )

                                            }
                                        }
                                    )
                                )
                            }
                        }
                    }
                }
            ) {
                NavGraph(
                    paddings = it,
                    navController = navController,
                    chooseDownloadFolderLauncher = chooseDownloadFolderLauncher
                )
            }
        }
    }
}