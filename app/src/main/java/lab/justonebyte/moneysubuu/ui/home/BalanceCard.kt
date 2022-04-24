package lab.justonebyte.moneysubuu.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import lab.justonebyte.moneysubuu.ui.theme.Green
import lab.justonebyte.moneysubuu.ui.theme.Red900
import lab.justonebyte.moneysubuu.ui.theme.SuBuuShapes

@Composable
fun BalanceCard(
    modifier: Modifier = Modifier,
    currentBalance: Int ,
    incomeBalance:Int,
    expenseBalance:Int
){

       var isMonthPickerExpanded by remember { mutableStateOf(false) }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row() {
                TextButton(onClick = { isMonthPickerExpanded=true }) {
                    Text(text = "This Month",style = MaterialTheme.typography.button)
                    Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription ="select month" )
                }
                DropdownMenu(
                    expanded = isMonthPickerExpanded,
                    onDismissRequest = { isMonthPickerExpanded = false },

                ) {

                        DropdownMenuItem(onClick = {
                            isMonthPickerExpanded = false
                        }) {
                            Text(text = "This Month",style = MaterialTheme.typography.button)
                        }
                        DropdownMenuItem(onClick = {
                            isMonthPickerExpanded = false
                        }) {
                            Text(text = "Select Month",style = MaterialTheme.typography.button)
                        }
                        DropdownMenuItem(onClick = {
                            isMonthPickerExpanded = false
                        }) {
                            Text(text = "Total",style = MaterialTheme.typography.button)
                        }
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Balance : ",
                    style = MaterialTheme.typography.h5
                )
                Text(
                    text = currentBalance.toString(),
                    style = MaterialTheme.typography.h6,
                    color = if(currentBalance>0) Green else Red900

                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row() {
                Card(
                    shape = SuBuuShapes.small,
                    modifier = modifier
                        .height(100.dp)
                        .padding(10.dp)
                        .weight(1f),
                    elevation = 10.dp
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        Text(
                            text = "Income : ",
                            style = MaterialTheme.typography.subtitle2
                        )
                        Text(
                            color = Green,
                            text = incomeBalance.toString(),
                            style = MaterialTheme.typography.h6

                        )
                    }
                }
                Card(
                    shape = SuBuuShapes.small,
                    modifier = modifier
                        .weight(1f)
                        .height(100.dp)
                        .padding(10.dp),
                    elevation = 10.dp
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        Text(
                            text = "Expense : ",
                            style = MaterialTheme.typography.subtitle2
                        )
                        Text(
                            color = Red900,
                            text = expenseBalance.toString(),
                            style = MaterialTheme.typography.h6

                        )
                    }
                }

            }
        }

}