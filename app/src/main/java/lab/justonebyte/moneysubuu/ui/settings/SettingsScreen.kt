package lab.justonebyte.moneysubuu.ui.settings

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import lab.justonebyte.moneysubuu.R
import lab.justonebyte.moneysubuu.model.AppLocale
import lab.justonebyte.moneysubuu.model.BalanceType
import lab.justonebyte.moneysubuu.model.Currency
import lab.justonebyte.moneysubuu.ui.MainActivity
import lab.justonebyte.moneysubuu.ui.components.OptionItem
import lab.justonebyte.moneysubuu.ui.components.TransactionTypePicker
import lab.justonebyte.moneysubuu.ui.home.SectionTitle
import lab.justonebyte.moneysubuu.utils.LocaleHelper


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsScreen(
    openDrawer:()->Unit,
    context: Context = LocalContext.current
){
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val settingsUiState by settingsViewModel.viewModelUiState.collectAsState()

    val settingCurrencies = listOf<OptionItem>(Currency.Kyat,Currency.Dollar)
    val appLanguages = listOf(AppLocale.English,AppLocale.Myanmar)

    fun changeLocale(localeString: String){
        LocaleHelper().setLocale(context, localeString)
        val i = Intent(context as Activity, MainActivity::class.java)
        context.finish()
        context.startActivity(i)
    }


    Scaffold(
        topBar = {
           Column {
               Row(
                   Modifier
                       .fillMaxWidth()
                       .padding(15.dp),
                   horizontalArrangement = Arrangement.SpaceBetween,
                   verticalAlignment = Alignment.CenterVertically
               ) {
                   Row(
                       verticalAlignment = Alignment.CenterVertically

                   ) {
                       Icon(painterResource(id =R.drawable.ic_round_settings_24), contentDescription = "",Modifier.absolutePadding(right = 5.dp))
                       Text(
                           text= stringResource(id = R.string.settings),
                           maxLines = 1,
                           overflow = TextOverflow.Ellipsis,
                           style = MaterialTheme.typography.titleLarge
                       )
                   }
               }
               Divider()
           }
        }
    ) {
        Column (
            Modifier
                .padding(it)
                .absolutePadding(top = 20.dp)
        ){
            Column {
                Column(
                    Modifier.absolutePadding(left = 10.dp, right = 10.dp)
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
            }
                Column(
                    Modifier.absolutePadding(left = 10.dp, right = 10.dp)
                ) {
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
    }
}