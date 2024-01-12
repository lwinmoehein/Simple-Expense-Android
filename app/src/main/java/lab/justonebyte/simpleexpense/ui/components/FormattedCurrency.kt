package lab.justonebyte.simpleexpense.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Currency

@Composable
fun FormattedCurrency(
    modifier: Modifier = Modifier,
    amount: Long,
    color:Color,
    currencyCode: String = "USD"
) {

    Text(
        color = color,
        text =currencyCode+(if(color==Color.Red) " -" else " ")+formatLongWithCommas(amount),
        style = MaterialTheme.typography.titleSmall,
        modifier = modifier.padding(horizontal = 2.dp)
    )
}
fun formatLongWithCommas(number: Long): String {
    val formatter = DecimalFormat("#,###")
    return formatter.format(number)
}