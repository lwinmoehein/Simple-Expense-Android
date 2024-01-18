package lab.justonebyte.simpleexpense.ui.stats

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowDown
import compose.icons.feathericons.ArrowUp
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.model.BalanceType
import lab.justonebyte.simpleexpense.model.TransactionType
import lab.justonebyte.simpleexpense.ui.components.AppOption
import lab.justonebyte.simpleexpense.ui.components.OptionItem
import lab.justonebyte.simpleexpense.ui.components.TransactionTypePicker
import lab.justonebyte.simpleexpense.ui.detail.CustomPieChartWithData
import lab.justonebyte.simpleexpense.ui.detail.colors
import lab.justonebyte.simpleexpense.ui.home.*

sealed class BalanceTypeOption(override val name:Int, override  val value:Any) : OptionItem {
    object MONTHLY: BalanceTypeOption(R.string.monthly,BalanceType.MONTHLY)
    object YEARLY: BalanceTypeOption(R.string.yearly,BalanceType.YEARLY)
    object TOTAL: BalanceTypeOption(R.string.total,BalanceType.TOTAL)
}

@Composable
fun TransactionTypeTab(
    modifier: Modifier = Modifier,
    selectedTransactionType: TransactionType,
    onTransactionTypeSelected:(transactionType:TransactionType)->Unit
){
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 100.dp)
        ){
            Row(
               modifier = Modifier
                   .weight(1f)
                   .clip(MaterialTheme.shapes.extraLarge)
                   .background(if (selectedTransactionType == TransactionType.Expense) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary)
                   .padding(horizontal = 10.dp, vertical = 5.dp)
                   .clickable {
                       onTransactionTypeSelected(TransactionType.Expense)
                   },
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.expense),
                    color = if (selectedTransactionType == TransactionType.Expense) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary
                )
            }
            Spacer(modifier = Modifier.width(5.dp))
            Row(
               modifier = Modifier
                   .weight(1f)
                   .clip(MaterialTheme.shapes.extraLarge)
                   .background(if (selectedTransactionType == TransactionType.Income) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary)
                   .padding(horizontal = 10.dp, vertical = 5.dp)
                   .clickable {
                       onTransactionTypeSelected(TransactionType.Income)
                   },
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.income),
                    color = if (selectedTransactionType == TransactionType.Income) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary
                )
            }
        }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun StatsScreen() {
    val statsViewModel = hiltViewModel<StatsViewModel>()
    val statsUiState by statsViewModel.viewModelUiState.collectAsState()
    val transactions = statsUiState.transactions

    val balanceTypeOptions = listOf<OptionItem>(
        BalanceTypeOption.MONTHLY,
        BalanceTypeOption.YEARLY,
        BalanceTypeOption.TOTAL
    )
    val selectedBalanceType = remember { mutableStateOf<BalanceType>(BalanceType.MONTHLY) }
    val selectedTransactionType = remember { mutableStateOf(TransactionType.Expense) }

    Scaffold {
        Column(modifier = Modifier
            .padding(it)) {
            TransactionTypeTab(
                selectedTransactionType = selectedTransactionType.value,
                onTransactionTypeSelected = {
                    selectedTransactionType.value = it
                }
            )

            Spacer(modifier = Modifier.height(10.dp))
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                Spacer(modifier = Modifier.height(30.dp))
                Divider(color = MaterialTheme.colorScheme.secondary)
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppOption(
                        label = "Select",
                        options = balanceTypeOptions,
                        onItemSelected = {
                            selectedBalanceType.value = it.value as BalanceType
                            when(selectedBalanceType.value){
                                BalanceType.DAILY->statsViewModel.collectDailyBalance()
                                BalanceType.MONTHLY-> statsViewModel.collectMonthlyBalance()
                                BalanceType.YEARLY->statsViewModel.collectYearlyBalance()
                                else->statsViewModel.collectTotalBalance()
                            }
                        },
                        selectedOption = selectedBalanceType.value
                    )
                    TransactionTypePicker(
                        onDatePicked = { date ->
                            statsViewModel.collectDailyBalance(date)
                        },
                        balanceType = statsUiState.currentBalanceType,
                        onMonthPicked = { month ->
                            statsViewModel.collectMonthlyBalance(month)
                        },
                        onYearPicked = { year ->
                            statsViewModel.collectYearlyBalance(year)
                        },
                        selectedYear = statsUiState.selectedYear,
                        selectedMonth = statsUiState.selectedMonth,
                        selectedDay = statsUiState.selectedDay
                    )

                }
                Spacer(modifier = Modifier.height(10.dp))
                CustomPieChartWithData(
                    transactionType = selectedTransactionType.value,
                    currency = statsUiState.currentCurrency,
                    transactions = transactions.filter { it.type===selectedTransactionType.value }
                )
            }

        }
    }
}
