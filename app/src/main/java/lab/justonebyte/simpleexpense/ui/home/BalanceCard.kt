package lab.justonebyte.simpleexpense.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canopas.lib.showcase.IntroShowcase
import com.canopas.lib.showcase.component.ShowcaseStyle
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowDown
import compose.icons.feathericons.ArrowUp
import compose.icons.feathericons.DollarSign
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.model.Currency
import lab.justonebyte.simpleexpense.ui.components.FormattedCurrency
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
                modifier = Modifier.absolutePadding(
                    top = 0.dp,
                    bottom = 10.dp,
                    right = 10.dp,
                    left = 10.dp
                )
            ) {
                // Title with current balance

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.your_wallet),
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
                    IntroShowcase(
                        showIntroShowCase = true,
                        dismissOnClickOutside = true,
                        onShowCaseCompleted = {
                            //App Intro finished!!
                        },
                    ) {
                        BalanceItem(
                            modifier = Modifier.introShowCaseTarget(
                                index = 0,
                                style = ShowcaseStyle.Default.copy(
                                    backgroundColor = MaterialTheme.colorScheme.primary, // specify color of background
                                    backgroundAlpha = 0.98f, // specify transparency of background
                                    targetCircleColor = Color.White // specify color of target circle
                                ),
                                // specify the content to show to introduce app feature
                                content = {
                                    Column {
                                        Text(
                                            text = stringResource(id = R.string.showcase_balance_card),
                                            color = Color.White,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = stringResource(R.string.showcase_balance_card_description),
                                            color = Color.White,
                                            fontSize = 16.sp
                                        )

                                    }
                                }
                            ),
                            icon = FeatherIcons.DollarSign,
                            text = "",
                            amount = homeUiState.currentBalance,
                            color = MaterialTheme.colorScheme.secondary,
                            currency = homeUiState.currentCurrency
                        )
                    }

                    BalanceItem(
                        icon = FeatherIcons.ArrowUp,
                        text = "",
                        amount = homeUiState.expenseBalance,
                        color = Color.Red,
                        currency = homeUiState.currentCurrency
                    )
                    BalanceItem(
                        icon = FeatherIcons.ArrowDown,
                        text = "",
                        amount = homeUiState.incomeBalance,
                        color = MaterialTheme.colorScheme.primary,
                        currency = homeUiState.currentCurrency
                    )
                }
            }
        }

}

@Composable
private fun BalanceItem(
    icon: ImageVector,
    text: String,
    amount: Long,
    color: Color,
    modifier: Modifier = Modifier,
    currency: Currency
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            tint = color,
            contentDescription = text,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(text = if(currency==Currency.Kyat) stringResource(id = R.string.kyat) else stringResource(
            id = R.string.dollar
        ))

        FormattedCurrency(
            amount = amount,
            color = color,
            currencyCode = "")
    }
}
