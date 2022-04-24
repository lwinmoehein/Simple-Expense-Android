package lab.justonebyte.moneysubuu.ui.home

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import lab.justonebyte.moneysubuu.ui.components.SuBuuSnackBar
import lab.justonebyte.moneysubuu.ui.components.SuBuuSnackBarHost

@OptIn(ExperimentalMaterialApi::class, com.google.accompanist.pager.ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
    openDrawer:()->Unit,

){
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val homeUiState by homeViewModel.viewModelUiState.collectAsState()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()


    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        snackbarHost = { SuBuuSnackBarHost(hostState = it) },
        sheetElevation = 8.dp,
        sheetShape = RoundedCornerShape(
            bottomStart = 0.dp,
            bottomEnd = 0.dp,
            topStart = 12.dp,
            topEnd = 12.dp
        ),

        sheetContent = {
            Card(
                        Modifier.heightIn(min = 500.dp, max = 1000.dp),
            ) {
                AddTransactionSheetContent(
                    categories =  homeUiState.categories,
                    onAddTransaction = {type, amount, category ->
                        homeViewModel.addTransaction(
                            transactionCategory = category,
                            type = type,
                            amount = amount
                        )
                    },
                    onCloseBottomSheet = {
                        coroutineScope.launch {
                            Log.i("bottomsheet:", bottomSheetScaffoldState.bottomSheetState.isExpanded.toString())

                            bottomSheetScaffoldState.bottomSheetState.collapse()
                            Log.i("bottomsheet:", bottomSheetScaffoldState.bottomSheetState.isExpanded.toString())
                        }
                    },
                    showIncorrectDataSnack = {
                        homeViewModel.showIncorrectFormDataSnackbar()
                    }
                )
            }
        }, sheetPeekHeight = 0.dp
    ) {

        HomeTabs(
            homeUiState = homeUiState,
            onOpenBottomSheet = {
                coroutineScope.launch {
                    bottomSheetScaffoldState.bottomSheetState.expand()
                }
            },
            onTabChanged = {
                when(it){
                    BalanceType.DAILY->homeViewModel.collectDailyBalance()
                    BalanceType.MONTHLY->homeViewModel.collectMonthlyBalance()
                    BalanceType.WEEKLY->homeViewModel.collectDailyBalance()
                    else->homeViewModel.collectYearlyBalance()
                }
            }
        )
        SuBuuSnackBar(
            onDismissSnackBar = { homeViewModel.clearSnackBar() },
            scaffoldState = bottomSheetScaffoldState,
            snackBarType = homeUiState.currentSnackBar,
        )
    }

}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeContent(
    onOpenBottomSheet:()->Unit,
    homeUiState: HomeUiState,
){

    Scaffold( floatingActionButton = {
        IconButton(
            modifier = Modifier.absolutePadding(bottom=30.dp, right = 30.dp),
            onClick = {
                onOpenBottomSheet()
              }) {
            Icon(              modifier = Modifier
                .width(50.dp)
                .height(50.dp),
                imageVector = Icons.Filled.AddCircle, contentDescription ="add transaction" )
        }

    },) {
        Column(Modifier.padding(it)) {
            Spacer(modifier = Modifier.height(30.dp))
            BalanceCard(currentBalance = homeUiState.currentBalance, incomeBalance = homeUiState.incomeBalance, expenseBalance = homeUiState.expenseBalance)
            SectionTitle(title = "Transaction")
            TransactionsCard(transactions = homeUiState.transactions)
        }
    }
}
@Composable
fun SectionTitle(title:String,modifier: Modifier = Modifier){
    Row(
        modifier = modifier.absolutePadding(top = 10.dp, bottom = 10.dp, left = 20.dp, right = 20.dp)
    ) {
        Text(text = title, style = MaterialTheme.typography.h6, color = MaterialTheme.colors.primary)
    }
}