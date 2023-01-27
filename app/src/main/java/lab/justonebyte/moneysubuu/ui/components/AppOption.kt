package lab.justonebyte.moneysubuu.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

data class OptionItem(val name:Int,val value:Any)

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppOption(
    modifier: Modifier = Modifier,
    label:String,
    onItemSelected:(item:OptionItem)->Unit,
    options:List<OptionItem>,
    selectedOption:OptionItem
) {


    var selectedItem = mutableStateOf(selectedOption)


    var expanded by remember {
        mutableStateOf(false)
    }

    // the box
    ExposedDropdownMenuBox(
        modifier=modifier,
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {

        // text field
        Row() {
            Text(text = stringResource(id = selectedItem.value.name) )
            Icon(if(expanded) Icons.Filled.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,"drop")
        }

        // menu
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectedOption ->
                // menu item
                DropdownMenuItem(onClick = {
                    selectedItem.value = selectedOption
                    expanded = false
                    onItemSelected(selectedItem.value)
                }) {
                    Text(text = stringResource(id = selectedOption.name), style = MaterialTheme.typography.button)
                }
            }
        }
    }
}
