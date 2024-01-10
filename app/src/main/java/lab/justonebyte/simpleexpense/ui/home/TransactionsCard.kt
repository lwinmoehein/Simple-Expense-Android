package lab.justonebyte.simpleexpense.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lab.justonebyte.simpleexpense.model.BalanceType
import lab.justonebyte.simpleexpense.model.Currency
import lab.justonebyte.simpleexpense.model.Transaction
import lab.justonebyte.simpleexpense.utils.formatDateString
import lab.justonebyte.simpleexpense.utils.formatMonthString
import lab.justonebyte.simpleexpense.utils.formatYearString
import lab.justonebyte.simpleexpense.utils.getFormatedDate
import lab.justonebyte.simpleexpense.utils.getFormatedMonth
import lab.justonebyte.simpleexpense.utils.getFormatedYear

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
    }.map { it.key to it.value.sortedByDescending{ value-> getFormatedDate(value.updated_at,"yyyy-MM-dd HH:mm:ss") } }.sortedByDescending {
        when(transactionGroupType){
            BalanceType.YEARLY-> getFormatedMonth(it.first)
            BalanceType.TOTAL-> getFormatedYear(it.first)
            else-> getFormatedDate(it.first)
        }
    }

   Row(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
   ) {
       if(groupedTransactions.isEmpty()){
           NoData()
       }
       LazyColumn(
           modifier = Modifier
               .fillMaxSize()
       ) {
           groupedTransactions.forEach { (date, transactions) ->
               stickyHeader {
                   Text(
                       text = date,
                       style = MaterialTheme.typography.titleMedium,
                       color= MaterialTheme.colorScheme.onSecondary,
                       modifier = Modifier
                           .fillMaxWidth()
                           .background(MaterialTheme.colorScheme.secondary)
                           .absolutePadding(left = 5.dp)
                   )
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