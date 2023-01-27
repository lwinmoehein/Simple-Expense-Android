package lab.justonebyte.moneysubuu.ui.settings

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import lab.justonebyte.moneysubuu.R
import lab.justonebyte.moneysubuu.model.AppLocale
import lab.justonebyte.moneysubuu.model.BalanceType
import lab.justonebyte.moneysubuu.model.Currency
import lab.justonebyte.moneysubuu.ui.MainActivity
import lab.justonebyte.moneysubuu.ui.components.SnackBarType
import lab.justonebyte.moneysubuu.ui.components.SuBuuSnackBar
import lab.justonebyte.moneysubuu.ui.components.SuBuuSnackBarHost
import lab.justonebyte.moneysubuu.ui.home.SectionTitle
import lab.justonebyte.moneysubuu.utils.LocaleHelper


@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun SettingsScreen(
    openDrawer:()->Unit,
    context: Context = LocalContext.current,
    bottomSheetScaffoldState:BottomSheetScaffoldState = rememberBottomSheetScaffoldState()
){
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val settingsUiState by settingsViewModel.viewModelUiState.collectAsState()

    val settingCurrencies = listOf<SettingItem>(Currency.Kyat,Currency.Dollar)
    val settingBalanceTypes = listOf<SettingItem>(BalanceType.DAILY,BalanceType.MONTHLY,BalanceType.YEARLY,BalanceType.TOTAL)
    val appLanguages = listOf(AppLocale.English,AppLocale.Myanmar)
    val isLangMenuOpen = mutableStateOf(false)

    fun changeLocale(localeString: String){
        LocaleHelper().setLocale(context, localeString)
        val i = Intent(context as Activity, MainActivity::class.java)
        context.finish()
        context.startActivity(i)
    }


    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        snackbarHost =  { SuBuuSnackBarHost(hostState = it) },
        sheetContent = {

        }, sheetPeekHeight = 0.dp

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
                        currentChosentMenuLabel = settingsUiState.defaultLanguage.name,
                        menuItems = appLanguages,
                        onMenuItemChosen = {
                            isLangMenuOpen.value = false
                            settingsViewModel.updateLocale(it as AppLocale)
                            changeLocale(it.value)
                        },
                        isOpen = isLangMenuOpen.value
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
    SuBuuSnackBar(
        onDismissSnackBar = { settingsViewModel.clearSnackBar() },
        scaffoldState = bottomSheetScaffoldState,
        snackBarType = settingsUiState.currentSnackBar,
    )
}