package lab.justonebyte.moneysubuu.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import lab.justonebyte.moneysubuu.model.BalanceType
import lab.justonebyte.moneysubuu.model.Transaction


@Composable
fun HomeContent(
    homeUiState: HomeUiState,
    collectBalanceOfDay:(day:String)->Unit,
    onMonthPicked:(month:String)->Unit,
    onYearPicked:(year:String)->Unit,
    onTransactionClick:(t: Transaction)->Unit,
    onTypeChanged:(type: BalanceType)->Unit
){

    Scaffold {
        Column(Modifier.padding(it)) {
            Spacer(modifier = Modifier.height(30.dp))
            BalanceCard(
                currency = homeUiState.currentCurrency,
                currentBalance = homeUiState.currentBalance,
                incomeBalance = homeUiState.incomeBalance,
                expenseBalance = homeUiState.expenseBalance,
                selectedDay = homeUiState.selectedDay,
                selectedMonth = homeUiState.selectedMonth,
                selectedYear = homeUiState.selectedYear,
                balanceType = homeUiState.currentBalanceType,
                onDatePicked = { date->
                    collectBalanceOfDay(date)
                },
                onMonthPicked = { month->
                    onMonthPicked(month)
                },
                onYearPicked = { year->
                    onYearPicked(year)
                },
                onTypeChanged = onTypeChanged
            )
            SectionTitle(title = "History")
            TransactionsCard(
                transactions = homeUiState.transactions,
                currency = homeUiState.currentCurrency,
                onTransactionClick = {
                    onTransactionClick(it)
                }
            )
        }
    }
}
