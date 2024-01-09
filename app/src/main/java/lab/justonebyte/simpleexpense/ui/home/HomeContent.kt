package lab.justonebyte.simpleexpense.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.model.Transaction


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    homeUiState: HomeUiState,
    onTransactionClick:(t: Transaction)->Unit,
    homeViewModel: HomeViewModel
){

        Column(Modifier.padding(10.dp)) {
            BalanceCard(
                homeViewModel = homeViewModel,
                homeUiState = homeUiState
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
