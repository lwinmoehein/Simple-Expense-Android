package lab.justonebyte.moneysubuu.ui.home

import android.util.Log
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import lab.justonebyte.moneysubuu.model.Transaction
import lab.justonebyte.moneysubuu.model.TransactionCategory
import lab.justonebyte.moneysubuu.ui.components.GeneralDialog
import lab.justonebyte.moneysubuu.ui.components.SnackBarType
import lab.justonebyte.moneysubuu.ui.components.SuBuuSnackBar
import lab.justonebyte.moneysubuu.ui.components.SuBuuSnackBarHost
import java.util.*


@OptIn(ExperimentalMaterialApi::class, com.google.accompanist.pager.ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
    goToPieChartDetail:(type:Int,tab:Int,date:String)->Unit,
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
    val currentType:MutableState<Int> = remember(currentTransaction) { mutableStateOf(
        currentTransaction.value?.type?.value ?: 1) }

    val isChooseAddTransactionTypeOpen =  remember { mutableStateOf(false)}
    val isSelectedTransactionEditMode = remember { mutableStateOf<Boolean?>(null) }
    val isDeleteTransactionDialogOpen = remember { mutableStateOf(false) }


    if (isDeleteTransactionDialogOpen.value) {
        GeneralDialog(
            dialogState = isDeleteTransactionDialogOpen,
            title = "Are you sure?",
            positiveBtnText = "Confirm",
            negativeBtnText = "Cancel",
            message = "Are you sure to delete this transaction?",
            onPositiveBtnClicked = {
                currentTransaction.value?.let { homeViewModel.deleteTransaction(it) }


                isDeleteTransactionDialogOpen.value =false
                currentTransaction.value = null
                isSelectedTransactionEditMode.value = null
            },
            onNegativeBtnClicked = {
                isDeleteTransactionDialogOpen.value =false
                currentTransaction.value = null
                isSelectedTransactionEditMode.value = null
            }
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
        floatingActionButton = {
            if(!bottomSheetScaffoldState.bottomSheetState.isAnimationRunning && bottomSheetScaffoldState.bottomSheetState.isCollapsed){
               NewTransactionButton(
                   onClick = {
                       coroutineScope.launch {
                           bottomSheetScaffoldState.bottomSheetState.expand()
                       }
                       isChooseAddTransactionTypeOpen.value = true
                   }
               )
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
            if(currentTransaction.value!=null && isSelectedTransactionEditMode.value==null){
                ChooseTransactionAction(
                    onDeleteClick = {
                        isDeleteTransactionDialogOpen.value = true
                    },
                    onEditClick = {
                        isSelectedTransactionEditMode.value = true
                    }
                )
            }
            else if(isChooseAddTransactionTypeOpen.value){
                AddTransactionAction(
                    onAddIncome = {
                        currentType.value = 1
                        isChooseAddTransactionTypeOpen.value = false
                    },
                    onAddExpense = {
                        currentType.value = 2
                        isChooseAddTransactionTypeOpen.value = false
                    }
                )
          }else{
              Card(
                  Modifier.heightIn(min = 500.dp, max = 1000.dp),
              ) {
                  AddTransactionSheetContent(
                      currentType = currentType.value,
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
                          homeViewModel.showSnackBar(SnackBarType.INCORRECT_DATA)
                      },
                      onAddCategory = { name,type->
                          homeViewModel.addCategory(
                              TransactionCategory(
                                  id = 0,
                                  transaction_type = type,
                                  name = name,
                                  created_at = System.currentTimeMillis()
                              )
                          )
                      },
                      isBottomSheetOpened = bottomSheetScaffoldState.bottomSheetState.isExpanded
                  )


              }
          }
        }, sheetPeekHeight = 0.dp
    ) {

        HomeTabs(
            goToPieChart = {type, tab, date ->
                goToPieChartDetail(type,tab,date)
            },
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