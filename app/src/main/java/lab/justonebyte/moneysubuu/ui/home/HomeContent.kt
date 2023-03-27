package lab.justonebyte.moneysubuu.ui.home

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
import lab.justonebyte.moneysubuu.R
import lab.justonebyte.moneysubuu.model.BalanceType
import lab.justonebyte.moneysubuu.model.Transaction
import lab.justonebyte.moneysubuu.ui.components.OptionItem
import lab.justonebyte.moneysubuu.utils.getCurrentDate
import lab.justonebyte.moneysubuu.utils.getCurrentMonth
import lab.justonebyte.moneysubuu.utils.getCurrentYear


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    homeUiState: HomeUiState,
    onTransactionClick:(t: Transaction)->Unit,
){


    Scaffold{
        Column(Modifier.padding(it)) {
            BalanceCard(
                currency = homeUiState.currentCurrency,
                currentBalance = homeUiState.currentBalance,
                incomeBalance = homeUiState.incomeBalance,
                expenseBalance = homeUiState.expenseBalance
            )
            SectionTitle(title = stringResource(id = R.string.history),modifier = Modifier.absolutePadding(top = 15.dp, left = 10.dp, bottom = 15.dp, right = 10.dp))
            TransactionsCard(
                transactions = homeUiState.transactions,
                currency = homeUiState.currentCurrency,
                onTransactionClick = {
                    onTransactionClick(it)
                },
                transactionGroupType = homeUiState.currentBalanceType
            )
        }
    }
}
