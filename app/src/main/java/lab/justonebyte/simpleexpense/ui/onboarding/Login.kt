package lab.justonebyte.simpleexpense.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.launch
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.ui.MainDestinations
import lab.justonebyte.simpleexpense.ui.account.SettingsViewModel
import lab.justonebyte.simpleexpense.ui.account.rememberFirebaseAuthLauncher
import lab.justonebyte.simpleexpense.ui.components.ProgressDialog
import lab.justonebyte.simpleexpense.ui.home.HomeViewModel
import lab.justonebyte.simpleexpense.utils.createFile

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
){
    val token = stringResource(R.string.web_client_id)

    val loginViewModel = hiltViewModel<LoginViewModel>()

    val settingsViewModel = hiltViewModel<SettingsViewModel>()


    val context = LocalContext.current
    val scope = rememberCoroutineScope()


    val launcher = rememberFirebaseAuthLauncher(
        onAuthComplete = { result,idToken ->
            result.user?.let {
                scope.launch {
                    settingsViewModel.fetchAccessTokenByGoogleId(idToken)
                }
            }
        },
        onAuthError = {
        }
    )

//
//    if(settingUiState.isLoggingIn){
//        ProgressDialog(onDismissRequest = {}, text = stringResource(id = R.string.logging_in) )
//    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.onboard_bg),
            contentDescription = null, // You can provide a description here
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    val gso =
                        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(token)
                            .requestEmail()
                            .build()
                    val googleSignInClient = GoogleSignIn.getClient(context, gso)
                    launcher.launch(googleSignInClient.signInIntent)
                },
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                Text(text = "Login with Google")
            }

        }
    }
}