package lab.justonebyte.simpleexpense.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowDown
import compose.icons.feathericons.ArrowUp
import compose.icons.feathericons.DollarSign
import compose.icons.feathericons.TrendingDown
import compose.icons.feathericons.TrendingUp
import lab.justonebyte.simpleexpense.model.Currency
import lab.justonebyte.simpleexpense.ui.theme.SuBuuShapes
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.ui.components.TransactionTypePicker

@Composable
fun BalanceCard(
    modifier: Modifier = Modifier,
    homeUiState:HomeUiState,
    homeViewModel: HomeViewModel
){
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.absolutePadding(top=0.dp, bottom = 10.dp, right = 10.dp, left = 10.dp)
        ) {
            // Title with current balance

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Your Wallet",
                    style = MaterialTheme.typography.labelLarge,
                    color = LocalContentColor.current
                )
                TransactionTypePicker(
                    onDatePicked = { date ->
                        homeViewModel.collectDailyBalance(date)
                    },
                    balanceType = homeUiState.currentBalanceType,
                    onMonthPicked = { month ->
                        homeViewModel.collectMonthlyBalance(month)
                    },
                    onYearPicked = { year ->
                        homeViewModel.collectYearlyBalance(year)
                    },
                    selectedYear = homeUiState.selectedYear,
                    selectedMonth = homeUiState.selectedMonth,
                    selectedDay = homeUiState.selectedDay
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            // Balance row with icons and colors
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BalanceItem(
                    icon = FeatherIcons.DollarSign,
                    text = "",
                    amount = homeUiState.currentBalance.toFloat(),
                    color = MaterialTheme.colorScheme.primary
                )
                BalanceItem(
                    icon = FeatherIcons.ArrowUp,
                    text = "",
                    amount = homeUiState.expenseBalance.toFloat(),
                    color = Color.Red
                )
                BalanceItem(
                    icon = FeatherIcons.ArrowDown,
                    text = "",
                    amount = homeUiState.incomeBalance.toFloat(),
                    color = Color.Green
                )
            }
        }
    }
}

@Composable
private fun BalanceItem(
    icon: ImageVector,
    text: String,
    amount: Float,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Icon(
            imageVector = icon,
            tint = color,
            contentDescription = text,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = amount.toString(),
            color = LocalContentColor.current,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}
