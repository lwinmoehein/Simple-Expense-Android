package lab.justonebyte.moneysubuu.ui.home

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lab.justonebyte.moneysubuu.model.BalanceType
import lab.justonebyte.moneysubuu.model.Currency
import lab.justonebyte.moneysubuu.model.Transaction
import lab.justonebyte.moneysubuu.model.TransactionType
import lab.justonebyte.moneysubuu.ui.theme.*
import lab.justonebyte.moneysubuu.utils.dateFormatter
import lab.justonebyte.moneysubuu.utils.formatDateString
import lab.justonebyte.moneysubuu.utils.formatMonthString
import lab.justonebyte.moneysubuu.utils.formatYearString

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionsCard(
    modifier:Modifier = Modifier,
    transactions:List<Transaction> = emptyList(),
    currency: Currency,
    onTransactionClick:(transaction:Transaction)->Unit,
    transactionGroupType:BalanceType
){

    val groupedTransactions = transactions.groupBy {
        when(transactionGroupType){
            BalanceType.YEARLY-> formatMonthString(it.created_at)
            BalanceType.TOTAL-> formatYearString(it.created_at)
            else-> formatDateString(it.created_at)
        }
    }.map { it.key to it.value }
    groupedTransactions.firstOrNull()?.let { Log.i("first:", it.first) }
//    Card(
//        shape = SuBuuShapes.small,
//        modifier = modifier
//            .fillMaxWidth()
//            .fillMaxHeight()
//            .padding(10.dp),
//    ) {
//        if(transactions.isEmpty()){
//            NoData(modifier = Modifier)
//        }
//        LazyColumn(){
//            items(transactions){
//                TransactionItem(transaction = it,currency = currency, onTransactionClick = {onTransactionClick(it)})
//            }
//        }
//    }
   Card(
               shape = SuBuuShapes.small,
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .absolutePadding(left = 10.dp, right = 10.dp)
          ,
   ) {
       LazyColumn(
           modifier = Modifier
               .fillMaxSize()
       ) {
           groupedTransactions.forEach { (date, transactions) ->
               stickyHeader {
                   Text(text = date, style = MaterialTheme.typography.titleMedium,color=MaterialTheme.colorScheme.onPrimary, modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primary).absolutePadding(left = 5.dp))
               }
               itemsIndexed(transactions) { index, transaction ->
                   TransactionItem(transaction,currency = currency, onTransactionClick = {onTransactionClick(it)})
                   if (index < transactions.size - 1) {
                       Divider()
                   }
               }
           }
       }
   }

}
@Composable
fun TransactionItem(transaction:Transaction,modifier: Modifier=Modifier,currency: Currency,onTransactionClick: (transaction: Transaction) -> Unit){
    Row(
        modifier = modifier
            .padding(10.dp)
            .clickable { onTransactionClick(transaction) }
    ) {
        Text(text = transaction.category.name,modifier = Modifier.weight(1f))
        Text(
            text =(if (transaction.type.value==1) transaction.amount.toString() else "-"+transaction.amount.toString() )+" " + stringResource(id = currency.name),
            modifier = Modifier.weight(1f)
        )
    }
}