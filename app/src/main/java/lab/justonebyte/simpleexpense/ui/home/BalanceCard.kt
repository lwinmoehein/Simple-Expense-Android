package lab.justonebyte.simpleexpense.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.canopas.lib.showcase.IntroShowcase
import com.canopas.lib.showcase.component.ShowcaseStyle
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowDown
import compose.icons.feathericons.ArrowUp
import kotlinx.coroutines.launch
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.model.ShowCase
import lab.justonebyte.simpleexpense.ui.home.HomeUiState
import lab.justonebyte.simpleexpense.ui.home.HomeViewModel
import lab.justonebyte.simpleexpense.ui.components.TransactionTypePicker

import java.util.Currency

/**
 * The main, refactored BalanceCard. It uses a Column layout for better
 * visual hierarchy and calls helper composables to keep the code clean.
 */
@Composable
fun BalanceCard(
    modifier: Modifier = Modifier,
    homeUiState: HomeUiState,
    homeViewModel: HomeViewModel
) {
    // Coroutine scope is remembered here to pass events to the ViewModel
    val coroutineScope = rememberCoroutineScope()

    Card(
        modifier = modifier.fillMaxWidth()// Softer corners
    ) {
        Column(
            modifier = Modifier.padding(all = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Section 1: Main Balance Display (The Hero Element)
            MainBalanceDisplay(homeUiState, homeViewModel)

            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))

            // Section 2: Income & Expense Breakdown
            IncomeExpenseRow(homeUiState)

            Spacer(modifier = Modifier.height(20.dp))

            // Section 3: The Date/Time Picker
            TransactionTypePicker(
                onDatePicked = { date ->
                    coroutineScope.launch { homeViewModel.updateCurrentDay(date) }
                },
                balanceType = homeUiState.currentBalanceType,
                onMonthPicked = { month ->
                    coroutineScope.launch { homeViewModel.updateCurrentMonth(month) }
                },
                onYearPicked = { year ->
                    coroutineScope.launch { homeViewModel.updateCurrentYear(year) }
                },
                selectedYear = homeUiState.selectedYear,
                selectedMonth = homeUiState.selectedMonth,
                selectedDay = homeUiState.selectedDay
            )
        }
    }
}

/**
 * A private helper composable for displaying the main "hero" balance.
 * It's wrapped with the IntroShowcase.
 */
@Composable
private fun MainBalanceDisplay(homeUiState: HomeUiState, homeViewModel: HomeViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.your_wallet),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))

        IntroShowcase(
            showIntroShowCase = homeUiState.currentAppShowcaseStep == ShowCase.BALANCE_CARD,
            dismissOnClickOutside = true,
            onShowCaseCompleted = {
                homeViewModel.updateAppIntroStep(ShowCase.CHARTS)
            }
        ) {
            Text(
                text = "${homeUiState.currentCurrency.symbol} ${homeUiState.currentBalance}",
                style = MaterialTheme.typography.displaySmall, // Much larger font
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.introShowCaseTarget(
                    index = 0,
                    style = ShowcaseStyle.Default.copy(
                        backgroundColor = MaterialTheme.colorScheme.primary,
                        backgroundAlpha = 0.98f,
                        targetCircleColor = Color.White
                    ),
                    content = {
                        Column {
                            Text(
                                text = stringResource(id = R.string.showcase_balance_card),
                                color = Color.White,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = stringResource(R.string.showcase_balance_card_description),
                                color = Color.White,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                )
            )
        }
    }
}

/**
 * A private helper composable for the Income and Expense breakdown row.
 */
@Composable
private fun IncomeExpenseRow(homeUiState: HomeUiState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly // Uses even spacing
    ) {
        BalanceDetailItem(
            label = stringResource(R.string.income),
            amount = homeUiState.incomeBalance,
            currency = homeUiState.currentCurrency,
            icon = FeatherIcons.ArrowDown,
            color = Color(0xFF008000) // A standard green color for income
        )
        BalanceDetailItem(
            label = stringResource(R.string.expense),
            amount = homeUiState.expenseBalance,
            currency = homeUiState.currentCurrency,
            icon = FeatherIcons.ArrowUp,
            color = MaterialTheme.colorScheme.error // Use theme's error color
        )
    }
}

/**
 * A private, reusable composable for a single detail item (e.g., Income or Expense).
 */
@Composable
private fun BalanceDetailItem(
    label: String,
    amount: Long,
    currency: Currency,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "${currency.symbol} $amount",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}