package lab.justonebyte.moneysubuu.ui.stats

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import lab.justonebyte.moneysubuu.R
import lab.justonebyte.moneysubuu.model.BalanceType
import lab.justonebyte.moneysubuu.model.TransactionType
import lab.justonebyte.moneysubuu.ui.components.AppOption
import lab.justonebyte.moneysubuu.ui.components.ChooseTransactionTypeCard
import lab.justonebyte.moneysubuu.ui.components.OptionItem
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
        topBar = {
        }
    ) {
        Column(modifier = Modifier
            .padding(10.dp)
            .padding(it)) {

                ChooseTransactionTypeCard(
                    modifier = Modifier.absolutePadding(bottom = 30.dp),
                    onDatePicked = { date ->
                       statsViewModel.collectDailyBalance(date)
                    },
                    balanceType =  balanceType.value,
                    onMonthPicked = { month->
                         statsViewModel.collectMonthlyBalance(month)
                    },
                    onYearPicked = { year->
                        statsViewModel.collectYearlyBalance(year)
                    },
                    onTypeChanged = { type->
                        balanceType.value = type
                        when(type){
                            BalanceType.DAILY->statsViewModel.collectDailyBalance()
                            BalanceType.MONTHLY->statsViewModel.collectMonthlyBalance()
                            BalanceType.YEARLY->statsViewModel.collectYearlyBalance()
                            else->statsViewModel.collectTotalBalance()
                        }
                    },
                    selectedYear = statsUiState.selectedYear,
                    selectedMonth = statsUiState.selectedMonth,
                    selectedDay = statsUiState.selectedDay
                )

                Column(
                    Modifier.absolutePadding(bottom = 20.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .absolutePadding(bottom = 20.dp)
                    ) {
                            SectionTitle(title = "$chosenDateString ${if(selectedTransactionType.value==TransactionType.Expense) stringResource(transactionTypeOptions[0].name) else stringResource(transactionTypeOptions[1].name)}")
                            AppOption(
                                modifier = Modifier
                                    .defaultMinSize(minWidth = 100.dp)
                                    .absolutePadding(top = 10.dp, bottom = 5.dp, right = 10.dp),
                                label = "Select",
                                options = transactionTypeOptions,
                                onItemSelected = {
                                   selectedTransactionType.value = it.value as TransactionType
                                },
                                selectedOption = when(selectedTransactionType.value){
                                    TransactionType.Expense->transactionTypeOptions[0]
                                    else->transactionTypeOptions[1]
                                }
                            )
                    }
                    CustomPieChartWithData(
                        modifier = Modifier.fillMaxHeight() ,
                        currency = statsUiState.currentCurrency,
                        transactions = transactions.filter { it.type==(if(selectedTransactionType.value==TransactionType.Expense) TransactionType.Expense else TransactionType.Income) })
                }

        }
    }
}
