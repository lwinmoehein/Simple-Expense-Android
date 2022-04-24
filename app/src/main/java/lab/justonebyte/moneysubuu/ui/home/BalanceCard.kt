package lab.justonebyte.moneysubuu.ui.home

import android.widget.Space
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import lab.justonebyte.moneysubuu.ui.theme.Green
import lab.justonebyte.moneysubuu.ui.theme.Red900
import lab.justonebyte.moneysubuu.ui.theme.SuBuuShapes

@Composable
fun BalanceCard(
    modifier: Modifier = Modifier,
    currentBalance: Double =0.0,
    incomeBalance:Int,
    expenseBalance:Int
){

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Balance : ",
                    style = MaterialTheme.typography.h5
                )
                Text(
                    text = currentBalance.toString(),
                    style = MaterialTheme.typography.h6

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