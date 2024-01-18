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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Brands
import compose.icons.fontawesomeicons.brands.Google
import kotlinx.coroutines.launch
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.ui.MainDestinations
import lab.justonebyte.simpleexpense.ui.account.rememberFirebaseAuthLauncher
import lab.justonebyte.simpleexpense.ui.components.ProgressDialog

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
){
    val token = stringResource(R.string.web_client_id)

    val loginViewModel = hiltViewModel<LoginViewModel>()
    val loginUiState = loginViewModel.viewModelUiState.collectAsState()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()


    if(loginUiState.value.firebaseUser!=null){
        navController.navigate(MainDestinations.HOME_ROUTE)
    }

    val launcher = rememberFirebaseAuthLauncher(
        onAuthComplete = { result,idToken ->
            result.user?.let {
                scope.launch {
                    loginViewModel.fetchAccessTokenByGoogleId(idToken)
                }
            }
        },
        onAuthError = {
        }
    )
    if(loginUiState.value.isLoggingIn){
        ProgressDialog(onDismissRequest = {}, text = stringResource(id = R.string.logging_in) )
    }

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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ... (logo and spacing as before)

                // Google sign-in button
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
                    ) { Text(text = "Login with Google", color = MaterialTheme.colorScheme.primary)
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
                        onClick = { /* Handle skip action */ },
                        modifier = Modifier.fillMaxWidth(), // Occupy full row width
                        enabled = true
                    ) {
                        Text("Skip", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }
        }
}