package lab.justonebyte.moneysubuu.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        LazyColumn(){
            item {
                Text(
                    text = "Transactions",style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.padding(10.dp)
                )
            }
            items(transactions){
                TransactionItem(transaction = it, onTransactionClick = {onTransactionClick(it)})
            }
        }
}
@Composable
fun TransactionItem(transaction:Transaction,modifier: Modifier=Modifier ,onTransactionClick: (transaction: Transaction) -> Unit){
    Row(
        modifier = modifier.padding(10.dp).fillMaxWidth().clickable { onTransactionClick(transaction)  }
    ) {
        Text(text = transaction.category.name,modifier = Modifier.weight(1f))
        Text(
            text =if (transaction.type.value==1) transaction.amount.toString() else "-"+transaction.amount.toString(),
            modifier = Modifier.weight(1f),
            color = if(transaction.type.value==1) Green else Red900
        )
        Text(text = transaction.created_at,modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
    }
}