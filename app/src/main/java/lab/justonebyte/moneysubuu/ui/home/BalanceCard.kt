package lab.justonebyte.moneysubuu.ui.home

import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import lab.justonebyte.moneysubuu.model.TransactionType
import lab.justonebyte.moneysubuu.ui.theme.GrayCardBg
import lab.justonebyte.moneysubuu.ui.theme.Green
import lab.justonebyte.moneysubuu.ui.theme.Red900
import lab.justonebyte.moneysubuu.ui.theme.SuBuuShapes
import lab.justonebyte.moneysubuu.utils.dateFormatter
import lab.justonebyte.moneysubuu.utils.getCurrentMonth
import lab.justonebyte.moneysubuu.utils.getToday
import lab.justonebyte.moneysubuu.utils.yearFormatter
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun BalanceCard(
    homeUiState: HomeUiState,
    goToPiechart:(transactionType:TransactionType)->Unit,
    modifier: Modifier = Modifier,
    balanceType: BalanceType,
    dateText:String,
    onDateTextClicked:()->Unit,

){
    val isBalanceTypeExpanded = remember { mutableStateOf(false)}
    Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

        Card(
            modifier = modifier
                .padding(10.dp)
                .wrapContentHeight()
                ,
            backgroundColor = GrayCardBg

        ) {
            Column(
                Modifier.padding(10.dp),

            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Balance Overview",style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.onPrimary)
                    TextButton(onClick = { isBalanceTypeExpanded.value = true }) {
                        Text(text = "Type:")
                    }

                    TextButton(onClick = { onDateTextClicked() },modifier = Modifier.background(Color.Transparent)) {
                        Text(text = dateText)
                    }
                    DropdownMenu(
                        expanded = isBalanceTypeExpanded.value,
                        onDismissRequest = { isBalanceTypeExpanded.value =false
                        }

                    ) {
                        DropdownMenuItem(onClick = { /*TODO*/ }) {
                            Text(text = "Daily")
                        }
                        DropdownMenuItem(onClick = { /*TODO*/ }) {
                            Text(text = "Monthly")
                        }
                        DropdownMenuItem(onClick = { /*TODO*/ }) {
                            Text(text = "Yearly")
                        }
                        DropdownMenuItem(onClick = { /*TODO*/ }) {
                            Text(text = "Total")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))
                Row() {
                    Column(
                        Modifier.weight(1f)
                    ) {
                        Column(

                        ) {
                            Text(
                                text = "Balance ",
                                style = MaterialTheme.typography.subtitle2,
                                color = MaterialTheme.colors.onPrimary
                            )
                            Text(
                                text = homeUiState.currentBalance.toString()+" kyats",
                                color = MaterialTheme.colors.onPrimary
                            )
                        }
                    }
                    Column(
                        Modifier.weight(1f)
                    ) {

                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
                Row() {
                    Column(
                        Modifier.weight(1f)
                    ) {
                        Column(
                            Modifier.clickable {
                                goToPiechart(TransactionType.Income)
                            }
                        ) {
                            Text(
                                text = "Income ",
                                style = MaterialTheme.typography.subtitle2,
                                color = MaterialTheme.colors.onPrimary
                            )
                            Text(
                                text = homeUiState.incomeBalance.toString()+" kyats",
                                color = MaterialTheme.colors.onPrimary

                            )
                        }
                    }
                    Column(
                        Modifier.weight(1f)
                    ) {
                        Column(
                            Modifier.clickable {
                                goToPiechart(TransactionType.Expense)
                            }
                        ) {
                            Text(
                                text = "Expense",
                                style = MaterialTheme.typography.subtitle2,
                                color = MaterialTheme.colors.onPrimary
                            )
                            Text(
                                text = homeUiState.expenseBalance.toString()+" kyats",
                                color = MaterialTheme.colors.onPrimary

                            )
                        }
                    }
                }

            }
        }
        }

}