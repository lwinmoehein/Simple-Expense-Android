package lab.justonebyte.moneysubuu.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import lab.justonebyte.moneysubuu.ui.theme.SuBuuShapes

@Composable
fun BalanceCard(
    modifier: Modifier = Modifier,
    currentBalance: Double =0.0
){
    Card(
        shape = SuBuuShapes.small,
        modifier = modifier
            .height(100.dp)
            .padding(10.dp),
        backgroundColor = MaterialTheme.colors.primary,
        elevation = 10.dp
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row() {
                Text(
                    text = "Balance : ",
                    style = MaterialTheme.typography.h6
                )
                Text(
                    text = currentBalance.toString(),
                    style = MaterialTheme.typography.h6

                )
            }
            TextButton(onClick = { /*TODO*/ }) {
                Text(text = "Add")
            }
        }
    }
}