package lab.justonebyte.simpleexpense.ui.stats

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.model.BalanceType
import lab.justonebyte.simpleexpense.model.TransactionType
import lab.justonebyte.simpleexpense.ui.components.AppOption
import lab.justonebyte.simpleexpense.ui.components.OptionItem
import lab.justonebyte.simpleexpense.ui.components.TransactionTypePicker
import lab.justonebyte.simpleexpense.ui.components.TransactionTypeTab
import lab.justonebyte.simpleexpense.ui.detail.CustomPieChartWithData
import lab.justonebyte.simpleexpense.ui.home.*

sealed class BalanceTypeOption(override val name:Int, override  val value:Any) : OptionItem {
    object MONTHLY: BalanceTypeOption(R.string.monthly,BalanceType.MONTHLY)
    object YEARLY: BalanceTypeOption(R.string.yearly,BalanceType.YEARLY)
    object TOTAL: BalanceTypeOption(R.string.total,BalanceType.TOTAL)
}

@Composable
fun TransactionTypeTabRow(
    onTransactionTypeSelected:(transactionType:TransactionType)->Unit
){
    var transactionTypeTabState by remember { mutableStateOf(0) }
    val transactionTypeTabs = listOf(TransactionTypeTab.EXPENSE,TransactionTypeTab.INCOME)

    TabRow(selectedTabIndex = transactionTypeTabState) {
        transactionTypeTabs.forEachIndexed { index, tab ->
            Tab(
                selected = transactionTypeTabState == index,
                onClick = {
                    transactionTypeTabState = index
                    onTransactionTypeSelected(tab.transactionType)
                },
                text = { Text(text = stringResource(id = tab.name), maxLines = 2, overflow = TextOverflow.Ellipsis) }
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
            TransactionTypeTabRow(
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
