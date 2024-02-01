package lab.justonebyte.simpleexpense.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import java.util.Currency

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
        Row(
            modifier =modifier.clickable { expanded = true }.absolutePadding(left = 5.dp, right = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text=stringResource(id = selectedOption.name), style = MaterialTheme.typography.labelLarge)
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
                       text = {
                                    Text(
                                        text = stringResource(id = selectedOption.name),
                                        modifier = Modifier.padding(0.dp),
                                        style = MaterialTheme.typography.labelLarge
                                    )
                              },
                       onClick = {
                           selectedItem.value = selectedOption
                           expanded = false
                           onItemSelected(selectedItem.value)
                       })

            }
        }
    }
}


@SuppressLint("UnrememberedMutableState")
@Composable
fun CurrencyOption(
    modifier: Modifier = Modifier,
    label:String,
    onCurrencySelected:(item:Currency)->Unit,
    currencies:List<Currency>,
    selectedCurrency:Currency
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem = mutableStateOf(selectedCurrency)

    Row(modifier = modifier) {
        Row(
            modifier = modifier.clickable { expanded = true }
                .absolutePadding(left = 5.dp, right = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedCurrency.displayName+" "+selectedCurrency.currencyCode,
                style = MaterialTheme.typography.labelLarge
            )
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Localized description")
        }
        DropdownMenu(
            modifier = Modifier.padding(0.dp),
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            currencies.forEach { selectedOption ->
                DropdownMenuItem(
                    modifier = Modifier.padding(0.dp),
                    text = {
                        Text(
                            text = selectedCurrency.displayName+" "+selectedCurrency.currencyCode,
                            modifier = Modifier.padding(0.dp),
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    onClick = {
                        selectedItem.value = selectedOption
                        expanded = false
                        onCurrencySelected(selectedItem.value)
                    })

            }
        }
    }
}