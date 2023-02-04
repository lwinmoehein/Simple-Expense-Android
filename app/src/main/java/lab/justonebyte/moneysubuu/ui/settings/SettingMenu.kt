package lab.justonebyte.moneysubuu.ui.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import lab.justonebyte.moneysubuu.model.TransactionType
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
    Card(
        modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(8.dp)) {
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Row() {
                    Text(
                        text = stringResource(id = settingItemLabel),
                        textAlign = TextAlign.Start,style = MaterialTheme.typography.subtitle2
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                AppOption(
                    modifier = Modifier
                        .defaultMinSize(minWidth = 100.dp)
                        .absolutePadding(top = 10.dp, bottom = 5.dp, right = 10.dp),
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
}