package lab.justonebyte.simpleexpense.ui.account

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.navigation.animation.*
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.model.AppLocale
import lab.justonebyte.simpleexpense.model.Currency
import lab.justonebyte.simpleexpense.ui.MainActivity
import lab.justonebyte.simpleexpense.ui.components.OptionItem
import lab.justonebyte.simpleexpense.ui.home.SectionTitle
import lab.justonebyte.simpleexpense.utils.LocaleHelper

@Composable
fun SettingsScreen(
    context: Context = LocalContext.current
){
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val settingsUiState by settingsViewModel.viewModelUiState.collectAsState()

    var showSettingsScreen by remember { mutableStateOf(false) }
    val settingCurrencies = listOf<OptionItem>(Currency.Kyat,Currency.Dollar)
    val appLanguages = listOf(AppLocale.English,AppLocale.Myanmar)
    val packageName = context.packageName
    val coroutineScope = rememberCoroutineScope()

    fun changeLocale(localeString: String){
        LocaleHelper().setLocale(context, localeString)
        val i = Intent(context as Activity, MainActivity::class.java)
        context.finish()
        context.startActivity(i)
    }

    Column(
        Modifier.absolutePadding(left = 10.dp, right = 10.dp, top = 5.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Column(
        ) {
            SectionTitle(title = stringResource(id = R.string.feat_setting))

            SettingMenu(
                modifier = Modifier.fillMaxWidth(),
                settingItemLabel =  R.string.select_currency,
                selectedOption = settingsUiState.selectedCurrency,
                menuItems = settingCurrencies,
                onMenuItemChosen = {
                    settingsViewModel.updateCurrency(it as Currency)
                }
            )
        }
        SectionTitle(title = stringResource(id = R.string.sys_setting))

        SettingMenu(
            modifier = Modifier.fillMaxWidth(),
            settingItemLabel =  R.string.select_lang,
            selectedOption = settingsUiState.defaultLanguage,
            menuItems = appLanguages,
            onMenuItemChosen = {
                settingsViewModel.updateLocale(it as AppLocale)
                changeLocale(it.value)
            }
        )
    }
}
