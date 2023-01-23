package lab.justonebyte.moneysubuu.ui.home

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import lab.justonebyte.moneysubuu.ui.MainDestinations

@OptIn(ExperimentalMaterialApi::class, com.google.accompanist.pager.ExperimentalPagerApi::class)
@Composable
fun SuBuuAppHomeScreen(
    navController: NavController
) {
    HomeScreen()
}