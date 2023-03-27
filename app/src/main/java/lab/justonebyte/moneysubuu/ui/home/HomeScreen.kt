package lab.justonebyte.moneysubuu.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import lab.justonebyte.moneysubuu.R
import lab.justonebyte.moneysubuu.model.BalanceType
import lab.justonebyte.moneysubuu.model.Transaction
import lab.justonebyte.moneysubuu.model.TransactionCategory
import lab.justonebyte.moneysubuu.ui.components.*
import lab.justonebyte.moneysubuu.utils.getCurrentDate
import lab.justonebyte.moneysubuu.utils.getCurrentGlobalTime
import lab.justonebyte.moneysubuu.utils.getCurrentMonth
import lab.justonebyte.moneysubuu.utils.getCurrentYear
import java.util.UUID


@SuppressLint("UnrememberedMutableState", "UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun HomeScreen(){
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val homeUiState by homeViewModel.viewModelUiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val currentTransaction = remember {
        mutableStateOf<Transaction?>(null)
    }
    val currentType:MutableState<Int> = remember(currentTransaction) { mutableStateOf(
        currentTransaction.value?.type?.value ?: 1) }

    val isChooseAddTransactionTypeOpen =  remember { mutableStateOf(false)}
    val isAddOrEditTransactionDialogOpen = remember { mutableStateOf(false)}
    val isSelectedTransactionEditMode = remember { mutableStateOf<Boolean?>(null) }
    val isDeleteTransactionDialogOpen = remember { mutableStateOf(false) }

    val currentDay = getCurrentDate()
    val currentMonth = getCurrentMonth()
    val currentYear = getCurrentYear()
    var currentHomeTabIndex: Int by remember { mutableStateOf(0) }
    val homeTabs = listOf<OptionItem>(BalanceType.DAILY,BalanceType.MONTHLY,BalanceType.YEARLY,BalanceType.TOTAL)
    val chosenDateString = when(homeUiState.currentBalanceType){
        BalanceType.DAILY-> if(homeUiState.selectedDay==currentDay) stringResource(id = R.string.this_day) else homeUiState.selectedDay
        BalanceType.MONTHLY->if(homeUiState.selectedMonth==currentMonth) stringResource(id = R.string.this_month) else homeUiState.selectedMonth
        BalanceType.YEARLY->if(homeUiState.selectedYear==currentYear) stringResource(id = R.string.this_year) else homeUiState.selectedYear
        else->stringResource(id = R.string.total) }




    fun clearStates(){
        isAddOrEditTransactionDialogOpen.value = false
        isChooseAddTransactionTypeOpen.value = false
        isSelectedTransactionEditMode.value = null
        isDeleteTransactionDialogOpen.value = false
        currentTransaction.value = null
        currentType.value = 1
    }
    fun onAddOrEditTransaction(
        type:Int,
        amount:Int,
        category:TransactionCategory,
        date:String,
        note:String?
    ){
        if(currentTransaction.value==null) {
            homeViewModel.addTransaction(
                transactionCategory = category,
                type = type,
                amount = amount,
                date = date,
                note = note
            )
        } else {
            currentTransaction.value?.let {
                homeViewModel.updateTransaction(
                    transactionId = it.unique_id,
                    transactionCategory = category,
                    type = type,
                    amount = amount,
                    date = date,
                    note = note
                )
            }
        }
        clearStates()
    }

    DeleteTransactionDialog(
        isOpen = isDeleteTransactionDialogOpen.value,
        onCancelClick = { clearStates() },
        onConfirmClick = {
            currentTransaction.value?.let { homeViewModel.deleteTransaction(it) }
            clearStates()
        }
    )
    ChooseTransactionTypeDialog(
        isOpen = isChooseAddTransactionTypeOpen.value && !isAddOrEditTransactionDialogOpen.value,
        onAddExpense = {
            currentType.value = 2
            isAddOrEditTransactionDialogOpen.value = true
        },
        onAddIncome =  {
            currentType.value = 1
            isAddOrEditTransactionDialogOpen.value = true
        }
    )
    ChooseTransactionActionDialog(
        isOpen = (currentTransaction.value!=null) && (isSelectedTransactionEditMode.value != true && !isDeleteTransactionDialogOpen.value),
        onEdit = {  isSelectedTransactionEditMode.value = true  },
        onDelete = { isDeleteTransactionDialogOpen.value = true }
    )


    if (isAddOrEditTransactionDialogOpen.value || isSelectedTransactionEditMode.value == true) {
        AppAlertDialog(
            title = if(isSelectedTransactionEditMode.value == true) if(currentType.value==2) stringResource(id = R.string.edit_expense_title) else stringResource(id = R.string.edit_income_title)  else (if(currentType.value==2) stringResource(
                id = R.string.add_expense_title
            ) else stringResource(id = R.string.add_income_title)),
            positiveBtnText =null,
            negativeBtnText = null,
            content = {

                Column() {
                            AddTransactionContent(
                                currentType = currentType.value,
                                currentTransaction = currentTransaction.value,
                                categories =  homeUiState.categories,
                                onConfirmTransactionForm = { type, amount, category,date,note->
                                    onAddOrEditTransaction(type,amount,category,date,note)
                                },
                                onCloseDialog = {
                                    clearStates()
                                    coroutineScope.launch {
                                    }
                                },
                                showIncorrectDataSnack = {
                                    homeViewModel.showSnackBar(SnackBarType.INCORRECT_DATA)
                                },
                                onAddCategory = { name,type->
                                    homeViewModel.addCategory(
                                        TransactionCategory(
                                            unique_id = UUID.randomUUID().toString()+"_"+System.currentTimeMillis(),
                                            transaction_type = type,
                                            name = name,
                                            created_at = getCurrentGlobalTime(),
                                            updated_at = getCurrentGlobalTime()
                                        )
                                    )
                                }
                            )
                        }
            },
            onPositiveBtnClicked = {
                currentTransaction.value?.let { homeViewModel.deleteTransaction(it) }

                clearStates()
            },
            onNegativeBtnClicked = {
                clearStates()
            },
            properties = DialogProperties(
                usePlatformDefaultWidth =!isAddOrEditTransactionDialogOpen.value &&
                        ((isSelectedTransactionEditMode.value != true && !isDeleteTransactionDialogOpen.value) && currentTransaction.value!=null)

            )
        )
    }



    Scaffold(
        topBar =  {
                      Column {
                          Row(
                              Modifier
                                  .fillMaxWidth()
                                  .padding(10.dp),
                              horizontalArrangement = Arrangement.SpaceBetween,
                              verticalAlignment = Alignment.CenterVertically
                          ) {
                              Row(
                                  verticalAlignment = Alignment.CenterVertically

                              ) {
                                  Text(
                                      stringResource(id = R.string.app_name),
                                      maxLines = 1,
                                      overflow = TextOverflow.Ellipsis,
                                      style = MaterialTheme.typography.titleLarge
                                  )
                              }
                              TransactionTypePicker(
                                  onDatePicked = { date ->
                                      homeViewModel.collectDailyBalance(date)
                                  },
                                  balanceType = homeUiState.currentBalanceType,
                                  onMonthPicked = { month ->
                                      homeViewModel.collectMonthlyBalance(month)
                                  },
                                  onYearPicked = { year ->
                                      homeViewModel.collectYearlyBalance(year)
                                  },
                                  selectedYear = homeUiState.selectedYear,
                                  selectedMonth = homeUiState.selectedMonth,
                                  selectedDay = homeUiState.selectedDay
                              )

                          }
                      }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    clearStates()
                    isChooseAddTransactionTypeOpen.value = true
                },
            ) {
                Icon(Icons.Filled.Add, "Localized description")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },

        ) {
        Card(Modifier.padding(it)){
            Column {
                ChooseTransactionTypeTab(
                    balanceType =  homeUiState.currentBalanceType,
                    onTypeChanged = { type->
                        when(type){
                            BalanceType.DAILY->homeViewModel.collectDailyBalance()
                            BalanceType.MONTHLY->homeViewModel.collectMonthlyBalance()
                            BalanceType.YEARLY->homeViewModel.collectYearlyBalance()
                            else->homeViewModel.collectTotalBalance()
                        }
                    }
                )
           }
            HomeContent(
                homeUiState = homeUiState,
                onTransactionClick = {
                    currentTransaction.value = it
                }
            )
        }

    }
}