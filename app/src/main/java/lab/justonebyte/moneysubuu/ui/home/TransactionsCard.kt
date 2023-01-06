package lab.justonebyte.moneysubuu.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import lab.justonebyte.moneysubuu.model.Transaction
import lab.justonebyte.moneysubuu.ui.theme.*
import lab.justonebyte.moneysubuu.utils.dateFormatter

@Composable
fun TransactionsCard(
    modifier:Modifier = Modifier,
    transactions:List<Transaction> = emptyList(),
    onTransactionClick:(transaction:Transaction)->Unit
){
    Card(
        shape = SuBuuShapes.small,
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(10.dp)
            .absolutePadding(bottom = 100.dp),
        elevation = 10.dp
    ) {
        LazyColumn(){
            items(transactions){
                TransactionItem(transaction = it, onTransactionClick = {onTransactionClick(it)})
            }
        }
    }

}
@Composable
fun TransactionItem(transaction:Transaction,modifier: Modifier=Modifier ,onTransactionClick: (transaction: Transaction) -> Unit){
    Row(
        modifier = modifier.padding(10.dp).clickable { onTransactionClick(transaction)  }
    ) {
        Text(text = transaction.category.name,modifier = Modifier.weight(1f))
        Text(
            text =if (transaction.type.value==1) transaction.amount.toString() else "-"+transaction.amount.toString(),
            modifier = Modifier.weight(1f),
            color = if(transaction.type.value==1) positiveColor else negativeColor
        )
        Text(text = transaction.created_at,modifier = Modifier.weight(1f))
    }
}