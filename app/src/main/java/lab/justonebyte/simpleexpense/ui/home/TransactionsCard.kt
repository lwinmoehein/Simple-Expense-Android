package lab.justonebyte.simpleexpense.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.model.BalanceType
import lab.justonebyte.simpleexpense.model.Transaction
import lab.justonebyte.simpleexpense.model.TransactionType
import lab.justonebyte.simpleexpense.ui.components.FormattedCurrency
import lab.justonebyte.simpleexpense.ui.components.IndeterminateCircularIndicator
import lab.justonebyte.simpleexpense.ui.components.getIconFromName
import lab.justonebyte.simpleexpense.ui.components.getValidCurrencyCode
import lab.justonebyte.simpleexpense.utils.getFormattedYear
import lab.justonebyte.simpleexpense.utils.getReadableFormattedDay
import lab.justonebyte.simpleexpense.utils.getReadableFormattedMonth
import java.util.Currency


@Composable
fun TransactionsCard(
    modifier:Modifier = Modifier,
    isLoading:Boolean,
    transactions:List<Transaction> = emptyList(),
    currency: Currency,
    onTransactionClick:(transaction:Transaction)->Unit,
    transactionGroupType:BalanceType
){
    val groupedTransactions = transactions.sortedByDescending { it.created_at }.groupBy {
        when(transactionGroupType){
            BalanceType.YEARLY-> getReadableFormattedMonth(it.created_at)
            BalanceType.TOTAL-> getFormattedYear(it.created_at)
            else-> getReadableFormattedDay(it.created_at)
        }
    }.map { it.key to it.value.sortedByDescending{ value-> value.created_at } }


   Row(
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
       verticalAlignment = Alignment.CenterVertically
   ) {
       if(groupedTransactions.isEmpty() && !isLoading){
           NoData()
       }
       if(isLoading && groupedTransactions.isEmpty()){
          Row (
              modifier = modifier.fillMaxSize(),
              horizontalArrangement = Arrangement.Center,
              verticalAlignment = Alignment.CenterVertically
          ){
              CircularProgressIndicator(
                  modifier = Modifier.width(30.dp).height(30.dp),
                  color = MaterialTheme.colorScheme.primary,
                  trackColor = MaterialTheme.colorScheme.surfaceVariant,
                  strokeWidth = 3.dp
              )
          }
       }
       if(!isLoading && groupedTransactions.isNotEmpty()){
           LazyColumn(
               modifier = Modifier
                   .fillMaxSize()
           ) {
               groupedTransactions.forEachIndexed { groupIndex, pair ->
                   item {
                       if (groupIndex!=0) {
                           Divider()
                       }
                   }
                   item {
                       Text(
                           text = pair.first,
                           style = MaterialTheme.typography.titleMedium,
                           color= MaterialTheme.colorScheme.primary,
                           modifier = Modifier
                               .fillMaxWidth()
                               .absolutePadding(left = 5.dp, bottom = 5.dp, top = 15.dp),
                           fontWeight = FontWeight.Bold
                       )
                   }
                   itemsIndexed(pair.second) { _, transaction ->
                       TransactionItem(transaction,currency = currency, onTransactionClick = {onTransactionClick(it)})
                   }
                   item{
                       Spacer(modifier = Modifier.height(15.dp))
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
            .clickable { onTransactionClick(transaction) }
    ) {
        Row(
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 4.dp)
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row (
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .width(25.dp)
                        .height(25.dp)
                        .clip(RoundedCornerShape(100))
                        .background(MaterialTheme.colorScheme.primary)
                ){
                    Icon(
                        imageVector = getIconFromName(name = transaction.category.icon_name),
                        contentDescription = "",
                        modifier = Modifier
                            .width(15.dp)
                            .height(15.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = transaction.category.name,
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FormattedCurrency(
                    color=if(transaction.type==TransactionType.Income) MaterialTheme.colorScheme.primary else Color.Red,
                    amount = transaction.amount.toLong(),
                    currencyCode = getValidCurrencyCode(currency)
                )
            }
        }
    }
}