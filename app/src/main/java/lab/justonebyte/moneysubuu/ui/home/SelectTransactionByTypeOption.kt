package lab.justonebyte.moneysubuu.ui.home

import android.widget.Toast
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

data class OptionItem(val name:String,val value:Any)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppOption(
    modifier: Modifier = Modifier,
    label:String,
    onItemSelected:(item:OptionItem)->Unit,
    options:List<OptionItem>
) {


    var selectedItem by remember {
        mutableStateOf(options[0])
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    // the box
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {

        // text field
        TextField(
            value = selectedItem.name,
            onValueChange = {
            },
            readOnly = true,
            label = { Text(text = label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

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
                    Text(text = selectedOption.name)
                }
            }
        }
    }
}
@Preview
@Composable
fun PreviewMenu(){
    val options = listOf<OptionItem>(OptionItem("Day",1),OptionItem("Week",2),OptionItem("Month",3))
    AppOption(
        label = "Select Type",
        options = options,
        onItemSelected = {}
    )
}