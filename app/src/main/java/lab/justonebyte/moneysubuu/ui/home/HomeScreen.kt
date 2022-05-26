package lab.justonebyte.moneysubuu.ui.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
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
import lab.justonebyte.moneysubuu.ui.components.*
import java.util.*

@OptIn(ExperimentalMaterialApi::class, com.google.accompanist.pager.ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
    goToPieChartDetail:(transactionType:TransactionType,balanceType:BalanceType,date:String)->Unit,
){
    val calendar = Calendar.getInstance()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val homeUiState by homeViewModel.viewModelUiState.collectAsState()
    val transactions  = homeUiState.transactions
    val coroutineScope = rememberCoroutineScope()
    val balanceType = remember{ mutableStateOf(BalanceType.DAILY)}

    val isMonthPickerShown = remember { mutableStateOf(false)}
    val showDatePicker = remember { mutableStateOf(false)}

    val selectedDate = remember { mutableStateOf(homeUiState.selectedDay)}
    val selectedMonthYear = remember { mutableStateOf(calendar.get(Calendar.YEAR))}
    val selectedMonthMonth = remember { mutableStateOf(calendar.get(Calendar.MONTH)+1)}

    val tabCalculatedDate = remember(balanceType.value,selectedDate.value,selectedMonthMonth.value,selectedMonthMonth){ mutableStateOf(
        when(balanceType.value){
            BalanceType.DAILY->selectedDate.value
            BalanceType.MONTHLY->selectedMonthYear.value.toString()+"-"+selectedMonthMonth.value.toString()
            else->selectedMonthYear.value.toString()
        }
    )}

    val currentTransaction = remember {
        mutableStateOf<Transaction?>(null)
    }


    if(showDatePicker.value){
        DatePicker(
            onDateChosen = {
                showDatePicker.value = false
                selectedDate.value = it
                homeViewModel.collectDailyBalance(dateValue =selectedDate.value)
            },
            onDismiss = {
                showDatePicker.value = false
            },
            date = selectedDate.value,
        )
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
        sheetGesturesEnabled =  false,
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
                         showSnackBar = {
                             homeViewModel.showSnackBar(it)
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
                         },
                         isBottomSheetOpened = bottomSheetScaffoldState.bottomSheetState.isExpanded
                     )


            }
        }, sheetPeekHeight = 0.dp
    ) {

        HomeTabs(
            onTabChanged = {
                balanceType.value = it
                when(it){
                    BalanceType.DAILY->homeViewModel.collectDailyBalance()
                    BalanceType.MONTHLY->homeViewModel.collectMonthlyBalance()
                    BalanceType.YEARLY->homeViewModel.collectYearlyBalance()
                    else->homeViewModel.collectTotalBalance()
                }
            },
            content = {
                Column(Modifier.padding(it)) {
                    Spacer(modifier = Modifier.height(30.dp))
                    BalanceCard(
                        balanceType = balanceType.value,
                        homeUiState = homeUiState,
                        dateText = tabCalculatedDate.value,
                        onDateTextClicked = {
                            when(balanceType.value){
                                BalanceType.DAILY->showDatePicker.value = true
                                BalanceType.MONTHLY->isMonthPickerShown.value = true
                                BalanceType.YEARLY->isMonthPickerShown.value = true
                            }
                        },
                        goToPiechart = { tType->
                            goToPieChartDetail(tType,balanceType.value,tabCalculatedDate.value)
                        }
                    )
                    SectionTitle(title = "Transaction")
                    TransactionsCard(
                        transactions = transactions,
                        onTransactionClick = {
                            currentTransaction.value = it
                            coroutineScope.launch {
                                bottomSheetScaffoldState.bottomSheetState.expand()
                            }
                        }
                    )
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
