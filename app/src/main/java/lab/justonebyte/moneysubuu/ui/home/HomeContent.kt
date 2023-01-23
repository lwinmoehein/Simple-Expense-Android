package lab.justonebyte.moneysubuu.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import lab.justonebyte.moneysubuu.model.Transaction
import lab.justonebyte.moneysubuu.model.TransactionType


@Composable
fun HomeContent(
    goToPieChart:(type:Int, tab:Int, date:String)->Unit,
    homeUiState: HomeUiState,
    collectBalanceOfDay:(day:String)->Unit,
    balanceType: BalanceType,
    onMonthPicked:(month:String)->Unit,
    onYearPicked:(year:String)->Unit,
    onTransactionClick:(t: Transaction)->Unit,
    onTypeChanged:(type:BalanceType)->Unit
){

    Scaffold {
        Column(Modifier.padding(it)) {
            Spacer(modifier = Modifier.height(30.dp))
            BalanceCard(
                currentBalance = homeUiState.currentBalance,
                incomeBalance = homeUiState.incomeBalance,
                expenseBalance = homeUiState.expenseBalance,
                collectBalaceOfDay = {
                    collectBalanceOfDay(it)
                },
                selectedDay = homeUiState.selectedDay,
                selectedMonth = homeUiState.selectedMonth,
                selectedYear = homeUiState.selectedYear,
                balanceType = balanceType,
                onMonthPicked = {
                    onMonthPicked(it)
                },
                onYearPicked = {
                    onYearPicked(it)
                },
                goToPiechart ={ type, tab, date ->
                    goToPieChart(type,tab,date)
                },
                onTypeChanged = onTypeChanged
            )
            SectionTitle(title = "History")
            TransactionsCard(
                transactions = homeUiState.transactions,
                onTransactionClick = {
                    onTransactionClick(it)
                }
            )
        }
    }
}
