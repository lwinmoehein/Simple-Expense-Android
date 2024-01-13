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

val title1 = "Privacy Policy\n"
val body1 = "Lwin Moe Hein built the SimpleExpense app as a Free app. This SERVICE is provided by Lwin Moe Hein at no cost and is intended for use as is.\n" +
        "\n" +
        "This page is used to inform visitors regarding my policies with the collection, use, and disclosure of Personal Information if anyone decided to use my Service.\n" +
        "\n" +
        "If you choose to use my Service, then you agree to the collection and use of information in relation to this policy. The Personal Information that I collect is used for providing and improving the Service. I will not use or share your information with anyone except as described in this Privacy Policy.\n" +
        "\n" +
        "The terms used in this Privacy Policy have the same meanings as in our Terms and Conditions, which are accessible at SimpleExpense unless otherwise defined in this Privacy Policy.\n\n"

val title2 = "Information Collection and Use\n"
val body2 = "For a better experience, while using our Service, I may require you to provide us with certain personally identifiable information. The information that I request will be retained on your device and is not collected by me in any way.\n" +
        "\n" +
        "The app does use third-party services that may collect information used to identify you.\n" +
        "\n" +
        "Link to the privacy policy of third-party service providers used by the app\n" +
        "\n" +
        "*   [Google Play Services](https://www.google.com/policies/privacy/)\n"

val title3 = "Log Data\n"
val body3 = "I want to inform you that whenever you use my Service, in a case of an error in the app I collect data and information (through third-party products) on your phone called Log Data. This Log Data may include information such as your device Internet Protocol (“IP”) address, device name, operating system version, the configuration of the app when utilizing my Service, the time and date of your use of the Service, and other statistics.\n"

val title4 = "Service Providers\n"
val body4 = "I may employ third-party companies and individuals due to the following reasons:\n" +
        "\n" +
        "To facilitate our Service;\n" +
        "To provide the Service on our behalf;\n" +
        "To perform Service-related services; or\n" +
        "To assist us in analyzing how our Service is used.\n" +
        "I want to inform users of this Service that these third parties have access to their Personal Information. The reason is to perform the tasks assigned to them on our behalf. However, they are obligated not to disclose or use the information for any other purpose.\n\n"

val title5 = "Security\b"

val body5 = "I value your trust in providing us your Personal Information, thus we are striving to use commercially acceptable means of protecting it. But remember that no method of transmission over the internet, or method of electronic storage is 100% secure and reliable, and I cannot guarantee its absolute security.\n" +
        "\n"

val title6 = "Links to Other Sites\n"
val body6 = "This Service may contain links to other sites. If you click on a third-party link, you will be directed to that site. Note that these external sites are not operated by me. Therefore, I strongly advise you to review the Privacy Policy of these websites. I have no control over and assume no responsibility for the content, privacy policies, or practices of any third-party sites or services.\n" +
        "\n"

val title7 = "Children’s Privacy\n"
val body7 = "These Services do not address anyone under the age of 13. I do not knowingly collect personally identifiable information from children under 13 years of age. In the case I discover that a child under 13 has provided me with personal information, I immediately delete this from our servers. If you are a parent or guardian and you are aware that your child has provided us with personal information, please contact me so that I will be able to do the necessary actions.\n" +
        "\n"

val title8 = "Changes to This Privacy Policy\n"
val body8 = "I may update our Privacy Policy from time to time. Thus, you are advised to review this page periodically for any changes. I will notify you of any changes by posting the new Privacy Policy on this page.\n" +
        "\n" +
        "This policy is effective as of 2024-01-13\n\n"

val title9 = "Contact Us\n"
val body9  = "If you have any questions or suggestions about my Privacy Policy, do not hesitate to contact me at dev.lwinmoehein@gmail.com."

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
                    SectionTitle(title = title1,style=MaterialTheme.typography.titleLarge)
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
                item {
                    SectionTitle(title4)
                    Text(text = body4)
                }
                item {
                    SectionTitle(title5)
                    Text(text = body5)
                }
                item {
                    SectionTitle(title6)
                    Text(text = body6)
                }
                item {
                    SectionTitle(title7)
                    Text(text = body7)
                }
                item {
                    SectionTitle(title8)
                    Text(text = body8)
                }
                item {
                    SectionTitle(title9)
                    Text(text = body9)
                }

            }
        }
    }
}
