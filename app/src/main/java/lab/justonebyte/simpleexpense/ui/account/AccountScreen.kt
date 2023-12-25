package lab.justonebyte.simpleexpense.ui.account

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import lab.justonebyte.simpleexpense.R


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AccountScreen(
    openDrawer:()->Unit,
    context: Context = LocalContext.current
){
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val settingsUiState by settingsViewModel.viewModelUiState.collectAsState()

    var showSettingsScreen by remember { mutableStateOf(false) }
    var showExportScreen by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()


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
                       if(!showSettingsScreen && !showExportScreen){
                           Icon(painterResource(id =R.drawable.ic_baseline_account_circle_24), contentDescription = "",Modifier.absolutePadding(right = 5.dp))
                           Text(
                               text= stringResource(id = R.string.account),
                               maxLines = 1,
                               overflow = TextOverflow.Ellipsis,
                               style = MaterialTheme.typography.titleLarge
                           )
                       }else{
                           Icon(Icons.Default.ArrowBack, contentDescription = "",
                               Modifier
                                   .absolutePadding(right = 5.dp)
                                   .clickable {
                                       showSettingsScreen = false
                                       showExportScreen = false
                                   })
                       }

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
                if(!showSettingsScreen && !showExportScreen){
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
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showSettingsScreen = true }
                            .padding(10.dp)
                    ) {
                       Row() {
                           Icon(Icons.Default.Settings, contentDescription ="", modifier = Modifier.absolutePadding(right = 10.dp) )
                           Text(text = stringResource(id = R.string.settings))
                       }
                        Icon(Icons.Default.ArrowForward, contentDescription ="" )
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showExportScreen = true }
                            .padding(10.dp)
                    ) {
                        Row() {
                            Icon(Icons.Default.DateRange, contentDescription ="", modifier = Modifier.absolutePadding(right = 10.dp) )
                            Text(text = stringResource(id = R.string.export))
                        }
                        Icon(Icons.Default.ArrowForward, contentDescription ="" )
                    }
                }
                if(showSettingsScreen){
                    SettingsScreen()
                }
                if(showExportScreen){
                    ExportScreen(
                        onExportClicked = { from, to, format ->
                            settingsViewModel.exportDate(from,to,format)
                        },
                        settingsViewModel = settingsViewModel
                    )
                }
            }

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
                            modifier = Modifier
                                .width(50.dp)
                                .height(50.dp)
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