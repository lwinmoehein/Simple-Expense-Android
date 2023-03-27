package lab.justonebyte.moneysubuu.ui.account

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import lab.justonebyte.moneysubuu.ui.components.AppOption
import lab.justonebyte.moneysubuu.ui.components.OptionItem

@SuppressLint("UnrememberedMutableState")
@Composable
fun SettingMenu(
    modifier: Modifier = Modifier,
    settingItemLabel:Int,
    selectedOption:OptionItem,
    menuItems:List<OptionItem>,
    onMenuItemChosen:(chosenMenuItem: OptionItem)->Unit,
) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth().padding(5.dp)) {
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                    Text(
                        text = stringResource(id = settingItemLabel),
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.labelLarge
                    )
            }
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                AppOption(
                    label = "Select",
                    options = menuItems,
                    onItemSelected = {
                        onMenuItemChosen(it)
                    },
                    selectedOption = selectedOption
                )
            }

    }
}