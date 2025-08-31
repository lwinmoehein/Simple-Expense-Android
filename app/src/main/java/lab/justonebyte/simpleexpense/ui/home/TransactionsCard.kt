package lab.justonebyte.simpleexpense.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.model.BalanceType
import lab.justonebyte.simpleexpense.model.Transaction
import lab.justonebyte.simpleexpense.model.TransactionType
import lab.justonebyte.simpleexpense.ui.components.FormattedCurrency
import lab.justonebyte.simpleexpense.ui.components.getIconFromName
import lab.justonebyte.simpleexpense.ui.components.getValidCurrencyCode
import lab.justonebyte.simpleexpense.utils.getFormattedYear
import lab.justonebyte.simpleexpense.utils.getReadableFormattedDay
import lab.justonebyte.simpleexpense.utils.getReadableFormattedMonth
import java.util.Currency

/**
 * Displays a list of transactions, grouped by date, with states for loading and empty.
 * This is the main composable for the transaction list.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionsCard(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    transactions: List<Transaction> = emptyList(),
    currency: Currency,
    onTransactionClick: (transaction: Transaction) -> Unit,
    transactionGroupType: BalanceType
) {
    // Grouping logic remains the same, it's very effective.
    val groupedTransactions = transactions.sortedByDescending { it.created_at }.groupBy {
        when (transactionGroupType) {
            BalanceType.YEARLY -> getReadableFormattedMonth(it.created_at)
            BalanceType.TOTAL -> getFormattedYear(it.created_at)
            else -> getReadableFormattedDay(it.created_at)
        }
    }.map { it.key to it.value.sortedByDescending { value -> value.created_at } }

    // Using a Box is cleaner for managing different UI states (loading, empty, data).
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading && groupedTransactions.isEmpty() -> {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
            groupedTransactions.isEmpty() -> {
                NoData()
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp) // Consistent spacing between items
                ) {
                    groupedTransactions.forEach { (dateHeader, transactionsInGroup) ->
                        // Sticky headers are great for long lists, they stay visible while scrolling.
                        stickyHeader {
                            TransactionDateHeader(text = dateHeader)
                        }
                        items(items = transactionsInGroup, key = { it.unique_id }) { transaction ->
                            TransactionItem(
                                transaction = transaction,
                                currency = currency,
                                onTransactionClick = onTransactionClick
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * A styled header for each date group in the transaction list.
 */
@Composable
fun TransactionDateHeader(text: String) {
    Text(
        text = text.uppercase(), // Uppercase makes headers stand out
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant, // More subtle color
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface) // Ensures header is opaque
            .padding(vertical = 8.dp, horizontal = 16.dp)
    )
}

/**
 * A completely redesigned, more informative composable for a single transaction item.
 */
@Composable
fun TransactionItem(
    transaction: Transaction,
    modifier: Modifier = Modifier,
    currency: Currency,
    onTransactionClick: (transaction: Transaction) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onTransactionClick(transaction) }
            .padding(horizontal = 16.dp, vertical = 12.dp), // More generous padding
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon with a larger, more prominent background
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp)) // A squircle looks more modern
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = getIconFromName(name = transaction.category.icon_name),
                contentDescription = transaction.category.name,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Column for Category and Note (takes up available space)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.category.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            // Display the transaction note if it exists
                transaction.note?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1 // Ensure it doesn't take up too much space
                    )
                }

        }

        Spacer(modifier = Modifier.width(8.dp))

        // Column for Amount and Time (fixed width, right-aligned)
        Column(horizontalAlignment = Alignment.End) {
            FormattedCurrency(
                color = if (transaction.type == TransactionType.Income) Color(0xFF008000) else MaterialTheme.colorScheme.error,
                amount = transaction.amount.toLong(),
                currencyCode = getValidCurrencyCode(currency),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                // TODO: Replace this placeholder with a call to your own time formatting utility
                // Example: text = getFormattedTime(transaction.created_at),
                text = "10:30 AM",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * A placeholder composable for when there is no data to display.
 * Customize this to match your app's design.
 */
@Composable
fun NoData(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.cat), // Example: Add an icon to your drawables
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.no_data),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.no_data),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}