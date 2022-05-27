package lab.justonebyte.moneysubuu.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import lab.justonebyte.moneysubuu.model.Transaction
import lab.justonebyte.moneysubuu.ui.theme.GrayCardBg
import lab.justonebyte.moneysubuu.ui.theme.Green
import lab.justonebyte.moneysubuu.ui.theme.Red900
import lab.justonebyte.moneysubuu.ui.theme.SuBuuShapes

@Composable
fun TransactionsCard(
    modifier:Modifier = Modifier,
    transactions:List<Transaction> = emptyList(),
    onTransactionClick:(transaction:Transaction)->Unit
){
       Column(Modifier.fillMaxSize()) {
           Text(
               text = "Transactions",style = MaterialTheme.typography.subtitle1,
               color = MaterialTheme.colors.primary,
               modifier = Modifier.padding(10.dp)
           )
           Row(
               modifier = modifier
                   .fillMaxWidth()
                   .absolutePadding(left = 10.dp, right = 10.dp),
           ) {
               Text(
                   text = "Category",style = MaterialTheme.typography.subtitle1,
                   color = MaterialTheme.colors.primary,
                   modifier = Modifier.padding(10.dp).weight(1f)
               )
               Text(
                   text = "Amount",style = MaterialTheme.typography.subtitle1,
                   color = MaterialTheme.colors.primary,
                   modifier = Modifier.padding(10.dp).weight(1f)
               )
               Text(
                   text = "Date",style = MaterialTheme.typography.subtitle1,
                   color = MaterialTheme.colors.primary,
                   modifier = Modifier.padding(10.dp).weight(1f)
               )
               Text(text = "",modifier = Modifier.weight(1f))
           }
           LazyColumn(Modifier.padding(10.dp)){
               items(transactions){
                   TransactionItem(transaction = it, onTransactionClick = {onTransactionClick(it)})
               }
           }
       }
}
@Composable
fun TransactionItem(transaction:Transaction,modifier: Modifier=Modifier ,onTransactionClick: (transaction: Transaction) -> Unit){
    Column(Modifier.absolutePadding(bottom = 10.dp)){
       Card(
           elevation = 1.dp,
           border = BorderStroke(1.dp, GrayCardBg)
       ) {
           Row(
               modifier = modifier
                   .fillMaxWidth()
                   .absolutePadding(left = 10.dp, right = 10.dp),
               horizontalArrangement = Arrangement.SpaceEvenly,
               verticalAlignment = Alignment.CenterVertically
           ) {
               Text(text = transaction.category.name,modifier = Modifier.weight(1f), color = GrayCardBg)
               Text(
                   text =if (transaction.type.value==1) transaction.amount.toString() else "-"+transaction.amount.toString(),
                   modifier = Modifier.weight(1f),
                   color = GrayCardBg,
               )
               Text(text = transaction.created_at,modifier = Modifier.weight(1f), color = GrayCardBg)
               TextButton(
                   onClick = { onTransactionClick(transaction) },

               ) {
                   Text(text = "Edit")
               }
           }
       }
    }

}