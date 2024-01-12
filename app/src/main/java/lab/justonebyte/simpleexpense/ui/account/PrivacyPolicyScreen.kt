package lab.justonebyte.simpleexpense.ui.account

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.ui.MainDestinations
import lab.justonebyte.simpleexpense.ui.components.SectionTitle

val title1 = "Privacy Policy"
val body1 = "Lwin Moe Hein built the X Money Tracker app as a Free app. This SERVICE is provided by Lwin Moe Hein at no cost and is intended for use as is.\n" +
        "\n" +
        "This page is used to inform visitors regarding my policies with the collection, use, and disclosure of Personal Information if anyone decided to use my Service.\n" +
        "\n" +
        "If you choose to use my Service, then you agree to the collection and use of information in relation to this policy. The Personal Information that I collect is used for providing and improving the Service. I will not use or share your information with anyone except as described in this Privacy Policy.\n" +
        "\n" +
        "The terms used in this Privacy Policy have the same meanings as in our Terms and Conditions, which are accessible at X Money Tracker unless otherwise defined in this Privacy Policy.\n"

val title2 = "Information Collection and Use"
val body2 = "For a better experience, while using our Service, I may require you to provide us with certain personally identifiable information. The information that I request will be retained on your device and is not collected by me in any way.\n" +
        "\n" +
        "The app does use third-party services that may collect information used to identify you.\n" +
        "\n" +
        "Link to the privacy policy of third-party service providers used by the app\n" +
        "\n" +
        "*   [Google Play Services](https://www.google.com/policies/privacy/)\n" +
        "*   [AdMob](https://support.google.com/admob/answer/6128543?hl=en)"

val title3 = "Log Data"
val body3 = "I want to inform you that whenever you use my Service, in a case of an error in the app I collect data and information (through third-party products) on your phone called Log Data. This Log Data may include information such as your device Internet Protocol (“IP”) address, device name, operating system version, the configuration of the app when utilizing my Service, the time and date of your use of the Service, and other statistics.\n"
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PrivacyPolicyScreen(
    navController: NavController,
    context: Context = LocalContext.current
){

    Scaffold(
        topBar = {
                IconButton(
                    onClick =  {
                        navController.navigate(MainDestinations.ACCOUNT_ROUTE)
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
            LazyColumn(Modifier.padding(15.dp)) {
                item {
                    SectionTitle(title = title1)
                    Text(text = body1)
                }

               item {
                   SectionTitle(title = title2)
                   Text(body2)
               }

               item {
                   SectionTitle(title3)
                   Text(text = body3)
               }

            }
        }
    }
}
