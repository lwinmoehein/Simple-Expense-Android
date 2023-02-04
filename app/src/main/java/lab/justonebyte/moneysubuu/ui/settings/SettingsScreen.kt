package lab.justonebyte.moneysubuu.ui.settings

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import lab.justonebyte.moneysubuu.R
import lab.justonebyte.moneysubuu.model.AppLocale
import lab.justonebyte.moneysubuu.model.BalanceType
import lab.justonebyte.moneysubuu.model.Currency
import lab.justonebyte.moneysubuu.ui.MainActivity
import lab.justonebyte.moneysubuu.ui.components.OptionItem
import lab.justonebyte.moneysubuu.ui.components.SnackBarType
import lab.justonebyte.moneysubuu.ui.components.SuBuuSnackBar
import lab.justonebyte.moneysubuu.ui.components.SuBuuSnackBarHost
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
    val settingBalanceTypes = listOf<OptionItem>(BalanceType.DAILY,BalanceType.MONTHLY,BalanceType.YEARLY,BalanceType.TOTAL)
    val appLanguages = listOf(AppLocale.English,AppLocale.Myanmar)

    fun changeLocale(localeString: String){
        LocaleHelper().setLocale(context, localeString)
        val i = Intent(context as Activity, MainActivity::class.java)
        context.finish()
        context.startActivity(i)
    }


    Scaffold(
    ) {
        Column (){
            Column {
                SectionTitle(title = stringResource(id = R.string.sys_setting))
                Row(
                    Modifier.absolutePadding(left = 10.dp, right = 10.dp)
                ) {
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
            Column {
                SectionTitle(title = stringResource(id = R.string.feat_setting))
                Row(
                    Modifier.absolutePadding(left = 10.dp, right = 10.dp)
                ) {
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
                Row(
                    Modifier.absolutePadding(left = 10.dp, right = 10.dp, top = 5.dp)
                ) {
                    SettingMenu(
                        modifier = Modifier.fillMaxWidth(),
                        settingItemLabel =  R.string.select_balance,
                        selectedOption = settingsUiState.defaultBalanceType,
                        menuItems = settingBalanceTypes,
                        onMenuItemChosen = {
                            settingsViewModel.updateDefaultBalanceType(it as BalanceType)
                        }
                    )
                }
            }
        }
    }
//    SuBuuSnackBar(
//        onDismissSnackBar = { settingsViewModel.clearSnackBar() },
//        scaffoldState = bottomSheetScaffoldState,
//        snackBarType = settingsUiState.currentSnackBar,
//    )
}