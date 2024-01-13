package lab.justonebyte.simpleexpense.ui.account

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.navigation.animation.*
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import kotlinx.coroutines.launch
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.ui.MainDestinations
import lab.justonebyte.simpleexpense.ui.components.SectionTitle

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AcknowledgeScreen(
    navController: NavController,
    context: Context = LocalContext.current
){

    val  coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
                IconButton(
                    onClick =  {
                        coroutineScope.launch {
                            navController.popBackStack()
                        }
                    }
                ) {
                    Icon(
                        imageVector = FeatherIcons.ArrowLeft,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
        }
    ) {
        Column(Modifier.padding(it)) {
            Divider()
            Column(Modifier.padding(15.dp)) {
                SectionTitle(title = stringResource(id = R.string.acknowledgements), style = MaterialTheme.typography.titleLarge)
                Text(modifier = Modifier.absolutePadding(top = 10.dp),text="We would like to express our gratitude to the following individuals and organizations whose contributions have played a significant role in the development of Simple Expense:\n" +
                        "\n")
                Column {
                    Text(text = "* Dagger dependency injection by square")
                    Text(text = "* Compose charts by bytebeats")
                    Text("* number-picker by charge-map")
                    Text("* wheel-picker-compose by commandiron")
                    Text(text = "* retrofit by square")
                    Text("* compose-feather-icons devsrsouza")
                }
            }
        }
    }
}
