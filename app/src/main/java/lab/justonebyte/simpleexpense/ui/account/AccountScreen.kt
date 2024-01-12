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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import compose.icons.FeatherIcons
import compose.icons.FontAwesomeIcons
import compose.icons.feathericons.ArrowLeft
import compose.icons.feathericons.ArrowRight
import compose.icons.feathericons.EyeOff
import compose.icons.feathericons.File
import compose.icons.feathericons.Info
import compose.icons.feathericons.LogOut
import compose.icons.feathericons.Settings
import compose.icons.feathericons.Star
import compose.icons.fontawesomeicons.Regular
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.ui.MainDestinations
import lab.justonebyte.simpleexpense.ui.components.AppAlertDialog
import lab.justonebyte.simpleexpense.ui.components.IndeterminateCircularIndicator
import lab.justonebyte.simpleexpense.ui.components.MinimalDialog
import lab.justonebyte.simpleexpense.ui.components.ProgressDialog
import lab.justonebyte.simpleexpense.ui.components.SuBuuSnackBar


@SuppressLint("UnrememberedMutableState", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AccountScreen(
    navController: NavController,
    context: Context = LocalContext.current
){
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val settingsUiState by settingsViewModel.viewModelUiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var isLoginNeededDialogShown by remember { mutableStateOf(false) }

    val isLoggingIn = settingsUiState.isLoggingIn
    var user = settingsUiState.firebaseUser


    if(isLoggingIn){
        ProgressDialog(onDismissRequest = {}, text = stringResource(id = R.string.logging_in) )
    }

    if(isLoginNeededDialogShown){
        AppAlertDialog(
            title = stringResource(id = R.string.please_login),
            negativeBtnText = null,
            positiveBtnText = stringResource(id = R.string.confirm),
            onPositiveBtnClicked = {
                isLoginNeededDialogShown = false
            }
        ){
            Text(text = stringResource(id = R.string.please_login_first))
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
        ) {
        SuBuuSnackBar(
            snackBarType = settingsUiState.currentSnackBar,
            onDismissSnackBar = { settingsViewModel.clearSnackBar() },
            snackbarHostState = snackbarHostState
        )
        Column (
            Modifier
                .padding(it)
        ){
            Column(
                Modifier
                    .padding(10.dp)
                    .absolutePadding(top = 50.dp)) {
                    AuthenticatedUser(
                        user = user,
                        fetchAndUpdateAccessToken = {
                            coroutineScope.launch {
                                settingsViewModel.fetchAccessTokenByGoogleId(it)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(MainDestinations.SETTINGS)
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                       Row(
                           modifier = Modifier.absolutePadding(top = 5.dp, bottom = 5.dp),
                           verticalAlignment = Alignment.CenterVertically
                        ){
                           Icon(
                               imageVector = FeatherIcons.Settings,
                               contentDescription ="",
                               modifier = Modifier.absolutePadding(right = 10.dp),
                               tint = MaterialTheme.colorScheme.primary
                           )
                           Text(text = stringResource(id = R.string.settings))
                       }
                        Icon(imageVector = FeatherIcons.ArrowRight, contentDescription ="", tint = MaterialTheme.colorScheme.primary)
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (user == null) {
                                    isLoginNeededDialogShown = true
                                } else {
                                    navController.navigate(MainDestinations.EXPORT)
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row (
                            modifier = Modifier.absolutePadding(top = 5.dp, bottom = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ){
                            Icon(
                                imageVector = FeatherIcons.File,
                                contentDescription ="",
                                modifier = Modifier.absolutePadding(right = 10.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(text = stringResource(id = R.string.export))
                        }
                        Icon(imageVector = FeatherIcons.ArrowRight, contentDescription ="", tint = MaterialTheme.colorScheme.primary)
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                                navController.navigate(MainDestinations.ACKNOWLEDGEMENT)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row (
                        modifier = Modifier.absolutePadding(top = 5.dp, bottom = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ){
                        Icon(
                            imageVector = FeatherIcons.Info,
                            contentDescription ="",
                            modifier = Modifier.absolutePadding(right = 10.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(text = stringResource(id = R.string.acknowledgements))
                    }
                    Icon(imageVector = FeatherIcons.ArrowRight, contentDescription ="", tint = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(MainDestinations.PRIVACY_POLICY)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row (
                        modifier = Modifier.absolutePadding(top = 5.dp, bottom = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ){
                        Icon(
                            imageVector = FeatherIcons.EyeOff,
                            contentDescription ="",
                            modifier = Modifier.absolutePadding(right = 10.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(text = stringResource(id = R.string.privacy_policy))
                    }
                    Icon(imageVector = FeatherIcons.ArrowRight, contentDescription ="", tint = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(MainDestinations.ACKNOWLEDGEMENT)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row (
                        modifier = Modifier.absolutePadding(top = 5.dp, bottom = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ){
                        Icon(
                            imageVector = FeatherIcons.Star,
                            contentDescription ="",
                            modifier = Modifier.absolutePadding(right = 10.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(text = stringResource(id = R.string.rate_app))
                    }
                }
                Spacer(modifier = Modifier.height(50.dp))

                    user?.let {
                        OutlinedButton(
                            modifier= Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            onClick = {
                                Firebase.auth.signOut()
                                GoogleSignIn
                                    .getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .signOut()
                                user = null
                                coroutineScope.launch {
                                    settingsViewModel.logOut()
                                }
                            }) {
                            Icon(
                                imageVector = FeatherIcons.LogOut,
                                contentDescription = "",
                                modifier = Modifier.absolutePadding(right = 10.dp),
                                tint = Color.Red
                            )
                            Text(text = stringResource(id = R.string.log_out), color = Color.Red)
                        }
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
    context: Context = LocalContext.current,
    fetchAndUpdateAccessToken:(googleId:String)->Unit,
    user:FirebaseUser?
) {
    val token = stringResource(R.string.web_client_id)

    val launcher = rememberFirebaseAuthLauncher(
        onAuthComplete = { result,idToken ->
            result.user?.let { fetchAndUpdateAccessToken(idToken) }
        },
        onAuthError = {
        }
    )


    Card(modifier = Modifier.fillMaxWidth()) {
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
                                Text(text =  stringResource(id = R.string.logged_in), style = MaterialTheme.typography.labelSmall)
                                Text(text = it1, style = MaterialTheme.typography.labelLarge)
                            }
                        }
                    }
                }
            }
            if (user == null) {
                Column(
                    Modifier.fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = FeatherIcons.Info,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column() {
                            Text(
                                text = stringResource(id = R.string.not_logged_in),
                                style = MaterialTheme.typography.labelSmall
                            )
                            Text(
                                text = stringResource(id = R.string.data_can_be_lost),
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
                        Text(text = stringResource(id = R.string.log_in))
                    }
                }
            }
        }
    }
}
