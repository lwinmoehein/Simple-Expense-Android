package lab.justonebyte.simpleexpense.ui.settings

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.model.AppList
import lab.justonebyte.simpleexpense.model.AppLocale
import lab.justonebyte.simpleexpense.model.Currency
import lab.justonebyte.simpleexpense.ui.MainActivity
import lab.justonebyte.simpleexpense.ui.account.*
import lab.justonebyte.simpleexpense.ui.components.OptionItem
import lab.justonebyte.simpleexpense.ui.home.SectionTitle
import lab.justonebyte.simpleexpense.utils.LocaleHelper

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsScreen(
    context: Context = LocalContext.current,
    onBackPressed:()->Unit
) {
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val settingsUiState by settingsViewModel.viewModelUiState.collectAsState()

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


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "New Page") },
                navigationIcon = {
                    IconButton(onClick = { /* Handle back button click */ }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
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
                Modifier.absolutePadding(left = 10.dp, right = 10.dp, top = 5.dp)
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
//            SectionTitle(title = stringResource(id = R.string.other_apps), modifier = Modifier.absolutePadding(left = 10.dp, top = 15.dp))
//            OtherApps(Modifier.absolutePadding(left = 15.dp, right = 15.dp))
//            Divider(Modifier.absolutePadding(bottom = 15.dp,top=30.dp))

            if(settingsUiState.companionApps!=null){
                Column(Modifier.padding(10.dp)) {
                    SectionTitle(title = stringResource(id = R.string.other_apps),modifier = Modifier.absolutePadding(bottom = 10.dp))
                    OtherApps(appList = settingsUiState.companionApps)
                }
            }
            Divider(Modifier.absolutePadding(bottom = 15.dp, top = 15.dp))

            InfoSettingItem(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    try {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
                    } catch (e: ActivityNotFoundException) {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
                    }
                },
                icon = { Icon(Icons.Filled.Star,"") },
                title = stringResource(id = R.string.give_stars)
            )
        }
    }
}
@Composable
fun InfoSettingItem(onClick:()->Unit,modifier:Modifier = Modifier,icon: @Composable () -> Unit,title:String){
    Card(modifier = modifier
        .padding(5.dp)
        .clickable { onClick() }){
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start, modifier = Modifier
            .absolutePadding(left = 5.dp)
            .defaultMinSize(minHeight = 40.dp)) {
            icon()
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = title)
        }
    }
}





@Composable
fun OtherApps(
    modifier: Modifier = Modifier,
    appList: AppList?,
    context: Context = LocalContext.current
) {

    LazyColumn {
        if (appList != null) {
            items(appList.apps) { app ->
                Log.i("package", "p:" + app.id + ",current:" + context.packageName)
                if (app.id != context.packageName) {
                    Card(
                        Modifier
                            .padding(5.dp)
                            .clickable {
                                try {
                                    context.startActivity(
                                        Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("market://details?id=${app.id}")
                                        )
                                    )
                                } catch (e: ActivityNotFoundException) {
                                    context.startActivity(
                                        Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("https://play.google.com/store/apps/details?id=${app.id}")
                                        )
                                    )
                                }
                            }) {
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                modifier = Modifier
                                    .width(50.dp)
                                    .height(50.dp),
                                painter = rememberAsyncImagePainter(app.imageUrl),
                                contentDescription = ""
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = app.name)
                        }
                    }
                }
            }
        }
    }
}