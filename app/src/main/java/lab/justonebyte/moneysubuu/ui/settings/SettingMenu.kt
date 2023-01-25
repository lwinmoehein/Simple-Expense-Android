package lab.justonebyte.moneysubuu.ui.settings

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

@Composable
fun SettingMenu(
    modifier: Modifier = Modifier,
    settingItemLabel:Int,
    currentChosentMenuLabel:Int,
    menuItems:List<SettingItem>,
    onMenuItemChosen:(chosenMenuItem: SettingItem)->Unit
) {
    var isMenuOpen by remember { mutableStateOf(false)}
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

                Row(
                    modifier= Modifier
                        .clickable { isMenuOpen = true }) {
                    Text(
                        text = stringResource(id = currentChosentMenuLabel),
                        textAlign = TextAlign.Start
                    )
                    Icon(if(isMenuOpen) Icons.Filled.KeyboardArrowUp else Icons.Default.ArrowDropDown ,"")
                }

                DropdownMenu(
                    modifier = Modifier.weight(1f),
                    expanded = isMenuOpen ,
                    onDismissRequest = {isMenuOpen =false}
                ) {
                    menuItems.forEach {
                        DropdownMenuItem(
                            onClick = {
                                isMenuOpen = false
                                onMenuItemChosen(it)
                            },
                            modifier = Modifier.height(30.dp).fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(id = it.name),
                                textAlign = TextAlign.Start,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }

    }
}