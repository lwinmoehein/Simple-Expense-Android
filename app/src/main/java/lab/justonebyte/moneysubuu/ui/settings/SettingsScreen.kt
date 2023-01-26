package lab.justonebyte.moneysubuu.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import lab.justonebyte.moneysubuu.R
import lab.justonebyte.moneysubuu.model.AppLocale
import lab.justonebyte.moneysubuu.model.BalanceType
import lab.justonebyte.moneysubuu.model.Currency
import lab.justonebyte.moneysubuu.ui.appContentPadding
import lab.justonebyte.moneysubuu.ui.category.CategoryViewModel
import lab.justonebyte.moneysubuu.ui.home.SectionTitle


@Composable
fun SettingsScreen(
    openDrawer:()->Unit,
){
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val settingsUiState by settingsViewModel.viewModelUiState.collectAsState()

    val settingCurrencies = listOf<SettingItem>(Currency.Kyat,Currency.Dollar)
    val settingBalanceTypes = listOf<SettingItem>(BalanceType.DAILY,BalanceType.MONTHLY,BalanceType.YEARLY,BalanceType.TOTAL)
    val appLanguages = listOf(AppLocale.English,AppLocale.Myanmar)

    Column (){
        Column {
            SectionTitle(title = "App Settings")
            Row(
                Modifier.absolutePadding(left = 10.dp, right = 10.dp)
            ) {
                SettingMenu(
                    modifier = Modifier.fillMaxWidth(),
                    settingItemLabel =  R.string.select_lang,
                    currentChosentMenuLabel = settingsUiState.defaultLanguage.name,
                    menuItems = appLanguages,
                    onMenuItemChosen = {
                        settingsViewModel.updateLocale(it as AppLocale)
                    }
                )
            }
            Row(
                Modifier.absolutePadding(left = 10.dp, right = 10.dp, top = 5.dp)
            ) {
                SettingMenu(
                    modifier = Modifier.fillMaxWidth(),
                    settingItemLabel =  R.string.select_balance,
                    currentChosentMenuLabel = settingsUiState.defaultBalanceType.name,
                    menuItems = settingBalanceTypes,
                    onMenuItemChosen = {
                        settingsViewModel.updateDefaultBalanceType(it as BalanceType)
                    }
                )
            }
        }
       Column {
           SectionTitle(title = "Feature Settings")
           Row(
               Modifier.absolutePadding(left = 10.dp, right = 10.dp)
           ) {
               SettingMenu(
                   modifier = Modifier.fillMaxWidth(),
                   settingItemLabel =  R.string.select_currency,
                   currentChosentMenuLabel = settingsUiState.selectedCurrency.name,
                   menuItems = settingCurrencies,
                   onMenuItemChosen = {
                        settingsViewModel.updateCurrency(it as Currency)
                   }
               )
           }
           Row(
               Modifier.absolutePadding(left = 10.dp, right = 10.dp, top = 5.dp)
           ) {
               SettingMenu(
                   modifier = Modifier.fillMaxWidth(),
                   settingItemLabel =  R.string.select_balance,
                   currentChosentMenuLabel = settingsUiState.defaultBalanceType.name,
                   menuItems = settingBalanceTypes,
                   onMenuItemChosen = {
                       settingsViewModel.updateDefaultBalanceType(it as BalanceType)
                   }
               )
           }
       }
   }
}