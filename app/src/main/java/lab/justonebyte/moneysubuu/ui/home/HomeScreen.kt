package lab.justonebyte.moneysubuu.ui.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import lab.justonebyte.moneysubuu.model.Transaction
import lab.justonebyte.moneysubuu.model.TransactionCategory
import lab.justonebyte.moneysubuu.model.TransactionType
import lab.justonebyte.moneysubuu.ui.components.SuBuuSnackBar
import lab.justonebyte.moneysubuu.ui.components.SuBuuSnackBarHost
import lab.justonebyte.moneysubuu.utils.dateFormatter
import java.util.*

@OptIn(ExperimentalMaterialApi::class, com.google.accompanist.pager.ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
    openDrawer:()->Unit,
    goToDetails:()->Unit

){
    val calendar = Calendar.getInstance()
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val homeUiState by homeViewModel.viewModelUiState.collectAsState()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()
    val balanceType = remember{ mutableStateOf(BalanceType.DAILY)}

    val isMonthPickerShown = remember { mutableStateOf(false)}
    val selectedMonthYear = remember { mutableStateOf(calendar.get(Calendar.YEAR))}
    val selectedMonthMonth = remember { mutableStateOf(calendar.get(Calendar.MONTH)+1)}
    val currentTransaction = remember {
        mutableStateOf<Transaction?>(null)
    }


    if(isMonthPickerShown.value){
        Dialog(onDismissRequest = { isMonthPickerShown.value=false }) {
                MonthPicker(
                    selectedMonth = selectedMonthMonth.value,
                    selectedYear =selectedMonthYear.value ,
                    onYearSelected ={
                      selectedMonthYear.value=it
                    } ,
                    onMonthSelected = {
                        selectedMonthMonth.value=it

                    },
                    onConfirmPicker = {
                        isMonthPickerShown.value =false
                        if(balanceType.value==BalanceType.MONTHLY){
                            homeViewModel.collectMonthlyBalance("${selectedMonthYear.value}-${if(selectedMonthMonth.value<10) "0"+selectedMonthMonth.value else selectedMonthMonth.value}")
                        }else{
                            homeViewModel.collectYearlyBalance(selectedMonthYear.value.toString())
                        }
                    },
                    isMonthPicker = balanceType.value==BalanceType.MONTHLY
            )
        }
    }

    BottomSheetScaffold(
        floatingActionButton = {
            if(!bottomSheetScaffoldState.bottomSheetState.isAnimationRunning && bottomSheetScaffoldState.bottomSheetState.isCollapsed){
                Box(
                    modifier = Modifier
                        .absolutePadding(bottom = 100.dp, right = 30.dp)
                        .clip(CircleShape)
                        .clickable {
                            currentTransaction.value = null
                            coroutineScope.launch {
                                bottomSheetScaffoldState.bottomSheetState.expand()
                            }
                        }) {
                    Icon(
                        modifier = Modifier
                            .width(60.dp)
                            .height(60.dp)
                            .background(MaterialTheme.colors.onPrimary)
                        ,
                        imageVector = Icons.Filled.AddCircle, contentDescription = "add transaction",
                        tint = MaterialTheme.colors.primary
                    )
                }
            }
        },
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
                          currentTransaction = currentTransaction.value,
                         categories =  homeUiState.categories,
                         onConfirmTransactionForm = { type, amount, category,date->
                             Log.i("on confirm sheet",if(currentTransaction.value!=null) "yes" else "no")
                             if(currentTransaction.value==null)
                                 homeViewModel.addTransaction(
                                     transactionCategory = category,
                                     type = type,
                                     amount = amount,
                                     date = date
                                 )
                             else
                                 currentTransaction.value?.let {
                                     homeViewModel.updateTransaction(
                                         transactionId =  it.id,
                                         transactionCategory = category,
                                         type = type,
                                         amount = amount,
                                         date = date
                                     )
                                 }


                         },
                         onCloseBottomSheet = {
                             coroutineScope.launch {
                                 bottomSheetScaffoldState.bottomSheetState.collapse()
                             }
                         },
                         showIncorrectDataSnack = {
                             homeViewModel.showIncorrectFormDataSnackbar()
                         },
                         onAddCategory = { name,type->
                             homeViewModel.addCategory(
                                 TransactionCategory(
                                     id = 1,
                                     transaction_type = type,
                                     name = name,
                                     created_at = System.currentTimeMillis()
                                 )
                             )
                         }
                     )


            }
        }, sheetPeekHeight = 0.dp
    ) {

        HomeTabs(
            homeUiState = homeUiState,
            onTabChanged = {
                balanceType.value = it
                when(it){
                    BalanceType.DAILY->homeViewModel.collectDailyBalance()
                    BalanceType.MONTHLY->homeViewModel.collectMonthlyBalance()
                    BalanceType.YEARLY->homeViewModel.collectYearlyBalance()
                    else->homeViewModel.collectTotalBalance()
                }
            },
            collectBalanceOfDay = {
                homeViewModel.collectDailyBalance(it)
            },
            selectedBalanceType = balanceType.value,
            onMonthChoose = {
                isMonthPickerShown.value =true
            },
            onTransactionClick = {
                coroutineScope.launch {
                    currentTransaction.value = it
                    bottomSheetScaffoldState.bottomSheetState.expand()
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
    homeUiState: HomeUiState,
    collectBalanceOfDay:(day:String)->Unit,
    balanceType: BalanceType,
    onMonthChoose:()->Unit,
    onTransactionClick:(t:Transaction)->Unit
){

    Scaffold(

    ) {
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
                onMonthChoose = {
                    onMonthChoose()
                }
            )
            SectionTitle(title = "Transaction")
            TransactionsCard(
                transactions = homeUiState.transactions,
                onTransactionClick = {
                    onTransactionClick(it)
                }
            )
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