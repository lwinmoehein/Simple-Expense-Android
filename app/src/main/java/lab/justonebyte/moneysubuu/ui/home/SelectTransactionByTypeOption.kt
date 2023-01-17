package lab.justonebyte.moneysubuu.ui.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import lab.justonebyte.moneysubuu.R

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