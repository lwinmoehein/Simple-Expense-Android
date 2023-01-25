package lab.justonebyte.moneysubuu.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

data class OptionItem(val name:String,val value:Any)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppOption(
    modifier: Modifier = Modifier,
    label:String,
    onItemSelected:(item:OptionItem)->Unit,
    options:List<OptionItem>,
    selectedOption:OptionItem
) {


    var selectedItem by remember {
        mutableStateOf(selectedOption)
    }

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
            Text(text = selectedItem.name )
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
                    selectedItem = selectedOption
                    expanded = false
                    onItemSelected(selectedItem)
                }) {
                    Text(text = selectedOption.name, style = MaterialTheme.typography.button)
                }
            }
        }
    }
}
