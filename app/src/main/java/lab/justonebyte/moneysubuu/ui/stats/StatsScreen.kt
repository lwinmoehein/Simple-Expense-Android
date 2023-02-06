package lab.justonebyte.moneysubuu.ui.stats

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import lab.justonebyte.moneysubuu.R
import lab.justonebyte.moneysubuu.model.BalanceType
import lab.justonebyte.moneysubuu.model.TransactionType
import lab.justonebyte.moneysubuu.ui.components.AppOption
import lab.justonebyte.moneysubuu.ui.components.ChooseTransactionTypeTab
import lab.justonebyte.moneysubuu.ui.components.OptionItem
import lab.justonebyte.moneysubuu.ui.components.TransactionTypePicker
import lab.justonebyte.moneysubuu.ui.detail.CustomPieChartWithData
import lab.justonebyte.moneysubuu.ui.home.*
import lab.justonebyte.moneysubuu.utils.getCurrentDate
import lab.justonebyte.moneysubuu.utils.getCurrentMonth
import lab.justonebyte.moneysubuu.utils.getCurrentYear

sealed class TransactionTypeOption(override val name:Int ,override  val value:Any) : OptionItem {
    object INCOME: TransactionTypeOption(R.string.income,TransactionType.Income)
    object EXPENSE: TransactionTypeOption(R.string.expense,TransactionType.Expense)
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun StatsScreen(goBack:()->Unit) {
    val statsViewModel = hiltViewModel<StatsViewModel>()
    val statsUiState by statsViewModel.viewModelUiState.collectAsState()
    val transactions = statsUiState.transactions

    val balanceType = mutableStateOf(statsUiState.currentBalanceType)

    val chosenDateString = when(balanceType.value){
        BalanceType.DAILY-> if(statsUiState.selectedDay== getCurrentDate()) stringResource(id = R.string.this_day) else statsUiState.selectedDay+" "+stringResource(id = R.string.day)
        BalanceType.MONTHLY->if(statsUiState.selectedMonth== getCurrentMonth()) stringResource(id = R.string.this_month) else statsUiState.selectedMonth+" "+stringResource(id = R.string.month)
        BalanceType.YEARLY->if(statsUiState.selectedYear== getCurrentYear()) stringResource(id = R.string.this_year) else statsUiState.selectedYear+" "+stringResource(id = R.string.year)
        else->stringResource(id = R.string.total) }

    val transactionTypeOptions = listOf<OptionItem>(
        TransactionTypeOption.INCOME,
        TransactionTypeOption.EXPENSE
    )
    val selectedTransactionType = remember { mutableStateOf(TransactionType.Expense) }

    Scaffold(
        topBar =  {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    Icon(Icons.Filled.Home, contentDescription = "s")
                    Text(
                        "X Money Tracker",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
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
        }
    ) {
        Column(modifier = Modifier
            .padding(it)) {

                ChooseTransactionTypeTab(
                    balanceType = statsUiState.currentBalanceType,
                    onTypeChanged = { type->
                        balanceType.value = type
                        when(type){
                            BalanceType.DAILY->statsViewModel.collectDailyBalance()
                            BalanceType.MONTHLY->statsViewModel.collectMonthlyBalance()
                            BalanceType.YEARLY->statsViewModel.collectYearlyBalance()
                            else->statsViewModel.collectTotalBalance()
                        }
                    }
                )

                Column(
                    Modifier.padding(20.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .absolutePadding(bottom = 20.dp)
                    ) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End){
                                AppOption(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .absolutePadding(top = 10.dp, bottom = 5.dp, right = 10.dp),
                                    label = "Select",
                                    options = transactionTypeOptions,
                                    onItemSelected = {
                                        selectedTransactionType.value = it.value as TransactionType
                                    },
                                    selectedOption = when(selectedTransactionType.value){
                                        TransactionType.Income->transactionTypeOptions[0]
                                        else->transactionTypeOptions[1]
                                    }
                                )
                            }
                    }
                    CustomPieChartWithData(
                        currency = statsUiState.currentCurrency,
                        transactions = transactions.filter { it.type==(if(selectedTransactionType.value==TransactionType.Expense) TransactionType.Expense else TransactionType.Income) })
                }

        }
    }
}
