package lab.justonebyte.simpleexpense.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Brands
import compose.icons.fontawesomeicons.brands.Google
import kotlinx.coroutines.launch
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.ui.account.rememberFirebaseAuthLauncher
import lab.justonebyte.simpleexpense.ui.components.ProgressDialog
import lab.justonebyte.simpleexpense.ui.components.SuBuuSnackBar

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onOnboardDone:()->Unit
){
    val token = stringResource(R.string.web_client_id)

    val onBoardViewModel = hiltViewModel<OnBoardViewModel>()
    val onBoardUiState by onBoardViewModel.viewModelUiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }


    val context = LocalContext.current

    val scope = rememberCoroutineScope()


    if(onBoardUiState.firebaseUser!=null){
       onOnboardDone()
    }

    val launcher = rememberFirebaseAuthLauncher(
        onAuthComplete = { result,idToken ->
            result.user?.let {
                scope.launch {
                    onBoardViewModel.fetchAccessTokenByGoogleId(idToken)
                }
            }
        },
        onAuthError = {
        }
    )
    if(onBoardUiState.isLoggingIn){
        ProgressDialog(onDismissRequest = {}, text = stringResource(id = R.string.logging_in) )
    }

    Scaffold(
        snackbarHost =  { SnackbarHost(snackbarHostState) }
    ) {
        SuBuuSnackBar(
            snackBarType = onBoardUiState.currentSnackBar,
            onDismissSnackBar = { onBoardViewModel.clearSnackBar() },
            snackbarHostState = snackbarHostState
        )
        Box(modifier = Modifier
            .padding(it)
            .fillMaxSize()) {
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(painter = painterResource(id = R.drawable.ic_launcher), contentDescription = "")
                Spacer(modifier = Modifier.height(50.dp))
                
                Text(modifier = Modifier.padding(horizontal = 16.dp),text = stringResource(id = R.string.please_sign_in_with_google),style=MaterialTheme.typography.labelMedium,color = MaterialTheme.colorScheme.onPrimary)
                // Google sign-in button
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Button(
                        onClick = {
                            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(token)
                                .requestEmail()
                                .build()
                            val googleSignInClient = GoogleSignIn.getClient(context, gso)
                            launcher.launch(googleSignInClient.signInIntent)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = true,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) { Text(text = stringResource(id = R.string.login_with_google), color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(5.dp))
                        Icon(tint = MaterialTheme.colorScheme.primary,imageVector = FontAwesomeIcons.Brands.Google,modifier = Modifier
                            .width(20.dp)
                            .height(20.dp), contentDescription ="" )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Skip button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            onOnboardDone()
                        },
                        modifier = Modifier.fillMaxWidth(), // Occupy full row width
                        enabled = true
                    ) {
                        Text(stringResource(id = R.string.skip), color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }
        }
    }
}