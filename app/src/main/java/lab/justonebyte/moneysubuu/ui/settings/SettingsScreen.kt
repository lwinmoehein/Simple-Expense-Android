package lab.justonebyte.moneysubuu.ui.settings

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.*
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
import coil.compose.rememberAsyncImagePainter
import lab.justonebyte.moneysubuu.R
import lab.justonebyte.moneysubuu.model.AppList
import lab.justonebyte.moneysubuu.model.AppLocale
import lab.justonebyte.moneysubuu.model.Currency
import lab.justonebyte.moneysubuu.ui.MainActivity
import lab.justonebyte.moneysubuu.ui.components.OptionItem
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
    val packageName = context.packageName

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
            Divider(Modifier.absolutePadding(bottom = 15.dp, top = 15.dp))

            InfoSettingItem(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    try {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
                    } catch (e: ActivityNotFoundException) {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
                    } },
                icon = { Icon(Icons.Filled.Star,"") },
                title = stringResource(id = R.string.give_stars)
            )

            if(settingsUiState.companionApps!=null){
                Column(Modifier.padding(10.dp)) {
                    SectionTitle(title = stringResource(id = R.string.other_apps))
                    OtherApps(appList = settingsUiState.companionApps)
                }
            }
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
fun OtherApps(modifier: Modifier = Modifier,appList: AppList?,context: Context = LocalContext.current){

    LazyColumn {
        if (appList != null) {
            items(appList.apps) { app ->
                Log.i("package","p:"+app.id+",current:"+context.packageName)
              if(app.id!=context.packageName){
                  Card(Modifier.padding(5.dp).clickable {
                      try {
                          context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${app.id}")))
                      } catch (e: ActivityNotFoundException) {
                          context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=${app.id}")))
                      }
                  }) {
                      Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                          Image(modifier= Modifier
                              .width(50.dp)
                              .height(50.dp),painter =  rememberAsyncImagePainter(app.imageUrl), contentDescription = "")
                          Spacer(modifier = Modifier.width(10.dp))
                          Text(text = app.name)
                      }
                  }
              }
            }
        }
    }
}