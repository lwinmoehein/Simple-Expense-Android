package lab.justonebyte.simpleexpense.ui.account

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.navigation.animation.*
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import kotlinx.coroutines.launch
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.model.Currency
import lab.justonebyte.simpleexpense.ui.components.OptionItem
import lab.justonebyte.simpleexpense.ui.components.SectionTitle

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsScreen(
    navController: NavController,
){
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val settingsUiState by settingsViewModel.viewModelUiState.collectAsState()
    val settingCurrencies = listOf<OptionItem>(Currency.Kyat,Currency.Dollar)
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
                IconButton(
                    onClick =  {
                        coroutineScope.launch {
                            navController.popBackStack()
                        }
                    }
                ) {
                    Icon(
                        imageVector = FeatherIcons.ArrowLeft,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
        }
    ) {
        Column(Modifier.padding(it)) {
            Divider()
            Column(Modifier.padding(15.dp)) {
                SectionTitle(title = stringResource(id = R.string.feat_setting))

                SettingMenu(
                    modifier = Modifier.fillMaxWidth(),
                    settingItemLabel = R.string.select_currency,
                    selectedOption = settingsUiState.selectedCurrency,
                    menuItems = settingCurrencies,
                    onMenuItemChosen = {
                        settingsViewModel.updateCurrency(it as Currency)
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))

                SectionTitle(title = stringResource(id = R.string.sys_setting))
            }
        }
    }
}
