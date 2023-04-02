package lab.justonebyte.moneysubuu.ui.account

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.navigation.animation.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import lab.justonebyte.moneysubuu.R
import lab.justonebyte.moneysubuu.model.AppLocale
import lab.justonebyte.moneysubuu.model.Currency
import lab.justonebyte.moneysubuu.ui.MainActivity
import lab.justonebyte.moneysubuu.ui.components.OptionItem
import lab.justonebyte.moneysubuu.ui.home.SectionTitle
import lab.justonebyte.moneysubuu.utils.LocaleHelper

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
