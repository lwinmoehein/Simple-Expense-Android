package lab.justonebyte.simpleexpense.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.model.Transaction
import lab.justonebyte.simpleexpense.ui.components.SectionTitle
import java.util.Locale


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
            Spacer(modifier = Modifier.height(20.dp))
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
