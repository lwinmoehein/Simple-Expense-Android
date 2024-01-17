package lab.justonebyte.simpleexpense.ui.account

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.navigation.animation.*
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import kotlinx.coroutines.launch
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.model.AppLocale
import lab.justonebyte.simpleexpense.model.Currency
import lab.justonebyte.simpleexpense.ui.MainActivity
import lab.justonebyte.simpleexpense.ui.MainDestinations
import lab.justonebyte.simpleexpense.ui.components.OptionItem
import lab.justonebyte.simpleexpense.ui.components.SectionTitle
import lab.justonebyte.simpleexpense.utils.LocaleHelper
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsScreen(
    navController: NavController,
    context: Context = LocalContext.current
){
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val settingsUiState by settingsViewModel.viewModelUiState.collectAsState()
    val settingCurrencies = listOf<OptionItem>(Currency.Kyat,Currency.Dollar)
    val appLanguages = listOf(AppLocale.English,AppLocale.Myanmar)
    val coroutineScope = rememberCoroutineScope()

    fun changeLocale(localeString: String){
        LocaleHelper().setLocale(context, localeString)
       val configuration = context.resources.configuration

        configuration.setLocale(Locale(localeString))
        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
    }

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

                SettingMenu(
                    modifier = Modifier.fillMaxWidth(),
                    settingItemLabel = R.string.select_lang,
                    selectedOption = settingsUiState.defaultLanguage,
                    menuItems = appLanguages,
                    onMenuItemChosen = {
                        settingsViewModel.updateLocale(it as AppLocale)
                        changeLocale(it.value)
                    }
                )
            }
        }
    }
}
