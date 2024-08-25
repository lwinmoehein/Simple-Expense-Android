package lab.justonebyte.simpleexpense.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import lab.justonebyte.simpleexpense.ui.components.SimpleExpenseSnackBar

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onOnboardDone:()->Unit
){
    val token = stringResource(R.string.web_client_id)

    val onBoardViewModel = hiltViewModel<OnBoardViewModel>()
    val onBoardUiState by onBoardViewModel.viewModelUiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val isLoginInProgress = remember { mutableStateOf(false) }


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
            isLoginInProgress.value = false
        },
        onAuthError = {
            isLoginInProgress.value = false
        }
    )
    if(onBoardUiState.isLoggingIn){
        ProgressDialog(onDismissRequest = {}, text = stringResource(id = R.string.logging_in) )
    }

    Scaffold(
        snackbarHost =  { SnackbarHost(snackbarHostState) }
    ) {
        SimpleExpenseSnackBar(
            snackBarType = onBoardUiState.currentSnackBar,
            onDismissSnackBar = { onBoardViewModel.clearSnackBar() },
            snackbarHostState = snackbarHostState
        )
        Box(modifier = Modifier
            .padding(it)
            .fillMaxSize().background(Color.White)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment =  Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Surface(
                       shape = MaterialTheme.shapes.large,
                       color = Color.Transparent
                    ) {
                        Image(
                            painter = painterResource(id = R.mipmap.ic_launcher), contentDescription = "",
                            modifier = Modifier
                                .height(80.dp)
                                .width(80.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = stringResource(id = R.string.app_name), style = MaterialTheme.typography.headlineSmall,
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(80.dp))
                
                Text(modifier = Modifier.padding(horizontal = 16.dp),text = stringResource(id = R.string.please_sign_in_with_google),style=MaterialTheme.typography.labelLarge,color = MaterialTheme.colorScheme.onPrimary)
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
                            isLoginInProgress.value = true
                            launcher.launch(googleSignInClient.signInIntent)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoginInProgress.value,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.login_with_google),
                            color = Color.White,
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Icon(tint = Color.White,imageVector = FontAwesomeIcons.Brands.Google,modifier = Modifier
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
                        Text("Skip", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }
        }
    }
}