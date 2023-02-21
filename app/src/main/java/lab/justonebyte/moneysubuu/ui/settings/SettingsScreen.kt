package lab.justonebyte.moneysubuu.ui.settings

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Space
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.lazy.items
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
                AuthenticatedUser()
                Spacer(modifier = Modifier.height(20.dp))
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
private fun rememberFirebaseAuthLauncher(
    onAuthComplete: (AuthResult) -> Unit,
    onAuthError: (ApiException) -> Unit
): ManagedActivityResultLauncher<Intent, ActivityResult> {
    val scope = rememberCoroutineScope()
    return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            Log.i("gcredential:","hi")

            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)

            scope.launch {
                val authResult = Firebase.auth.signInWithCredential(credential).await()
                onAuthComplete(authResult)
            }
        } catch (e: ApiException) {
            onAuthError(e)
        }
    }
}

@Composable
fun AuthenticatedUser(modifier: Modifier=Modifier.absolutePadding(left = 10.dp, right = 10.dp),context: Context = LocalContext.current) {
    val token = stringResource(R.string.web_client_id)
    var user by remember { mutableStateOf(Firebase.auth.currentUser) }

    val launcher = rememberFirebaseAuthLauncher(
        onAuthComplete = { result ->
            user = result.user
        },
        onAuthError = {
            user = null
            Log.i("gerror:", it.localizedMessage)
        }
    )

    Card(modifier = modifier.fillMaxWidth()) {
        Column(Modifier.absolutePadding(left = 10.dp, right = 10.dp, top = 10.dp, bottom = 10.dp)) {
            user?.let {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row() {
                        Image(
                            painter = rememberAsyncImagePainter(user?.photoUrl),
                            contentDescription = null,
                            modifier = Modifier.width(50.dp).height(50.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        user?.displayName?.let { it1 ->
                            Column() {
                                Text(text = "Logged in :", style = MaterialTheme.typography.labelSmall)
                                Text(text = it1, style = MaterialTheme.typography.labelLarge)
                            }
                        }
                    }
                    Button(onClick = {
                        Firebase.auth.signOut()
                        user = null
                    }) {
                        Text(text = "Log Out")
                    }
                }
            }
            if (user == null) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Filled.Info, contentDescription = "")
                        Spacer(modifier = Modifier.width(10.dp))
                        Column() {
                            Text(
                                text = "Not logged in :",
                                style = MaterialTheme.typography.labelSmall
                            )
                            Text(
                                text = "Your data can be lost.",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                    OutlinedButton(onClick = {
                        val gso =
                            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(token)
                                .requestEmail()
                                .build()
                        val googleSignInClient = GoogleSignIn.getClient(context, gso)
                        launcher.launch(googleSignInClient.signInIntent)
                    }) {
                        Text(text = "Login")
                    }
                }
            }
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