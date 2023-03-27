package lab.justonebyte.moneysubuu.ui.account

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Path
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContentScope.SlideDirection.Companion.Right
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Absolute.Right
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
import lab.justonebyte.moneysubuu.ui.settings.SettingsScreen
import lab.justonebyte.moneysubuu.utils.LocaleHelper
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.animation.*




@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AccountScreen(
    openDrawer:()->Unit,
    context: Context = LocalContext.current
){
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


    var showNewPage by remember { mutableStateOf(false) }

//    if (showNewPage) {
//        SettingsScreen(onBackPressed = { showNewPage = false })
//        SlideInHorizontally(
//            modifier = Modifier.fillMaxWidth(),
//            initialOffsetX = { fullWidth -> fullWidth },
//            animationSpec = tween(durationMillis = 500)
//        ) {
//            // This content will slide in from the right
//        }
//    } else {
//        // Content of your main screen goes here
//        SlideOutHorizontally(
//            modifier = Modifier.fillMaxWidth(),
//            targetOffsetX = { fullWidth -> -fullWidth },
//            animationSpec = tween(durationMillis = 500)
//        ) {
//            // This content will slide out to the left
//        }
//    }




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
                       Icon(painterResource(id =R.drawable.ic_baseline_account_circle_24), contentDescription = "",Modifier.absolutePadding(right = 5.dp))
                       Text(
                           text= stringResource(id = R.string.account),
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
                AuthenticatedUser(
                    fetchAndUpdateAccessToken = {
                        coroutineScope.launch {
                            settingsViewModel.fetchAccessTokenByGoogleId(it)
                        }
                    },
                    logOutUser = {
                       coroutineScope.launch {
                           settingsViewModel.logOut()
                       }
                    }
                )
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
            SectionTitle(title = stringResource(id = R.string.settings), modifier = Modifier.absolutePadding(left = 10.dp, top = 15.dp))

//            OtherApps(Modifier.absolutePadding(left = 15.dp, right = 15.dp))
//            Divider(Modifier.absolutePadding(bottom = 15.dp,top=30.dp))


            Divider(Modifier.absolutePadding(bottom = 15.dp, top = 15.dp))

        }
    }
}


@Composable
private fun rememberFirebaseAuthLauncher(
    onAuthComplete: (AuthResult,idToken:String) -> Unit,
    onAuthError: (ApiException) -> Unit
): ManagedActivityResultLauncher<Intent, ActivityResult> {
    val scope = rememberCoroutineScope()
    return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            account.idToken?.let { Log.i("gcredential:", it) }

            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)

            scope.launch {
                val authResult = Firebase.auth.signInWithCredential(credential).await()
                onAuthComplete(authResult, account.idToken!!)
            }
        } catch (e: ApiException) {
            onAuthError(e)
        }
    }
}

@Composable
fun AuthenticatedUser(
    modifier: Modifier=Modifier.absolutePadding(left = 10.dp, right = 10.dp),
    context: Context = LocalContext.current,
    fetchAndUpdateAccessToken:(googleId:String)->Unit,
    logOutUser:()->Unit
) {
    val token = stringResource(R.string.web_client_id)
    var user by remember { mutableStateOf(Firebase.auth.currentUser) }

    val launcher = rememberFirebaseAuthLauncher(
        onAuthComplete = { result,idToken ->
            user = result.user
            result.user?.let { fetchAndUpdateAccessToken(idToken) }

//            uploadTransactions()
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
                        GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut()
                        user = null
                        logOutUser()
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
