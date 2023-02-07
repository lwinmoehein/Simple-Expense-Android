package lab.justonebyte.moneysubuu.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

interface OptionItem {
    val name: Int
    val value: Any
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun AppOption(
    modifier: Modifier = Modifier,
    label:String,
    onItemSelected:(item:OptionItem)->Unit,
    options:List<OptionItem>,
    selectedOption:OptionItem
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem = mutableStateOf(selectedOption)

    Row(modifier = modifier) {
//        Row(Modifier.clickable { expanded = true }) {
//                Text(stringResource(id = selectedOption.name))
//                Icon(Icons.Default.ArrowDropDown, contentDescription = "Localized description")
//        }
        OutlinedButton(onClick = { expanded = true }, modifier = modifier) {
            Text(stringResource(id = selectedOption.name))
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Localized description")
        }
        DropdownMenu(
            modifier = Modifier.padding(0.dp),
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { selectedOption ->
                   DropdownMenuItem(
                       modifier = Modifier.padding(0.dp ),
                       text = { Text(text = stringResource(id = selectedOption.name),modifier = Modifier.padding(0.dp)) },
                       onClick = {
                           selectedItem.value = selectedOption
                           expanded = false
                           onItemSelected(selectedItem.value)
                       })

            }
        }
    }
}
