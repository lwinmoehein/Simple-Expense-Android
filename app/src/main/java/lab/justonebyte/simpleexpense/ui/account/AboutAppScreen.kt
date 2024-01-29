package lab.justonebyte.simpleexpense.ui.account

import android.content.Context
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import lab.justonebyte.simpleexpense.BuildConfig
import lab.justonebyte.simpleexpense.R

@Composable
fun AboutAppScreen(
    navController: NavController,
    context: Context = LocalContext.current
) {

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            IconButton(
                onClick = {
                    navController.popBackStack()
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
        Column(
            Modifier
                .padding(it)
                .padding(15.dp)) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Animated logo with fade-in effect
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher),
                    contentDescription = "App logo"
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        fontSize = 36.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(text = "Version: v"+ BuildConfig.VERSION_NAME, fontSize = 14.sp,color = MaterialTheme.colorScheme.onSurface)
                }

                Text(
                    text = stringResource(id = R.string.app_info),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Developed by: Lwin Moe Hein", fontSize = 10.sp,color = MaterialTheme.colorScheme.onSurface)
                    Text(text = "Tested by: Ei Zin Kyaw", fontSize = 10.sp,color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
    }
}