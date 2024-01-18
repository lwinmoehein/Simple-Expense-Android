package lab.justonebyte.simpleexpense.ui.onboarding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import compose.icons.FeatherIcons
import compose.icons.feathericons.ChevronDown
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.model.AppLocale
import lab.justonebyte.simpleexpense.ui.components.AppAlertDialog

@ExperimentalPagerApi
@Composable
fun TopLocaleSection(
    currentLocale: AppLocale,
    onChangeLocale:(locale: AppLocale)->Unit
){
    var isDialogShown by remember { mutableStateOf(false) }

    if(isDialogShown){
        AppAlertDialog(
            title = stringResource(id = R.string.select_language)
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable{
                        onChangeLocale(AppLocale.Myanmar)
                        isDialogShown = false
                    }
                ) {
                    RadioButton(selected = currentLocale== AppLocale.Myanmar, onClick = null)
                    Text(text = stringResource(id = AppLocale.Myanmar.name))
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        onChangeLocale(AppLocale.English)
                        isDialogShown = false
                    }
                ){
                    RadioButton(selected = currentLocale== AppLocale.English, onClick = null)
                    Text(text = stringResource(id = AppLocale.English.name ))
                }
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 50.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Row (
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                isDialogShown = true
            }
        ){
            Text(text = stringResource(id = currentLocale.name), fontWeight = FontWeight.Bold)
            Icon(imageVector = FeatherIcons.ChevronDown, contentDescription ="" )
        }
    }
}