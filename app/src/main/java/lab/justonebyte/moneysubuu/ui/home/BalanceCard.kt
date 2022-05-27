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

    Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Text(
//                    text = "Balance : ",
//                    style = MaterialTheme.typography.h5
//                )
//                Text(
//                    text = homeUiState.currentBalance.toString(),
//                    style = MaterialTheme.typography.h6,
//                    color = if(homeUiState.currentBalance>0) Green else Red900
//                )
//            }
//            Row(horizontalArrangement = Arrangement.Center){
//                TextButton(onClick = {
//                    onDateTextClicked()
//                }) {
//                    Text(text =dateText)
//                }
//            }
//            Spacer(modifier = Modifier.height(20.dp))
//            Row() {
//                Card(
//                    shape = SuBuuShapes.small,
//                    modifier = modifier
//                        .height(100.dp)
//                        .padding(10.dp)
//                        .weight(1f)
//                        .clickable {
//                            goToPiechart(
//                               TransactionType.Income
//                            )
//                        },
//                    elevation = 10.dp
//                ) {
//                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
//                        Text(
//                            text = "Income : ",
//                            style = MaterialTheme.typography.subtitle2
//                        )
//                        Text(
//                            color = Green,
//                            text = homeUiState.incomeBalance.toString(),
//                            style = MaterialTheme.typography.h6
//
//                        )
//                    }
//                }
//                Card(
//                    shape = SuBuuShapes.small,
//                    modifier = modifier
//                        .weight(1f)
//                        .height(100.dp)
//                        .padding(10.dp)
//                        .clickable {
//                            goToPiechart(
//                                TransactionType.Expense,
//                            )
//                        },
//                    elevation = 10.dp
//                ) {
//                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
//                        Text(
//                            text = "Expense : ",
//                            style = MaterialTheme.typography.subtitle2
//                        )
//                        Text(
//                            color = Red900,
//                            text = homeUiState.expenseBalance.toString(),
//                            style = MaterialTheme.typography.h6
//
//                        )
//                    }
//                }
//
//            }
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
                    Button(onClick = { /*TODO*/ },modifier = Modifier.background(Color.Transparent)) {
                        Text(text = "Today")
                        Icon(imageVector = Icons.Default.DateRange, contentDescription ="calendar" )
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
//                        Column(
//                        ) {
//                            Text(
//                                text = "Income",
//                                style = MaterialTheme.typography.subtitle2,
//                                color = MaterialTheme.colors.onPrimary
//                            )
//                            Text(
//                                text = homeUiState.incomeBalance.toString(),
//                                color = MaterialTheme.colors.onPrimary
//
//                            )
//                        }
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