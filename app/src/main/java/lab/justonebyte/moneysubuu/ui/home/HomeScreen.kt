package lab.justonebyte.moneysubuu.ui.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.launch
import lab.justonebyte.moneysubuu.model.BalanceType
import lab.justonebyte.moneysubuu.model.Transaction
import lab.justonebyte.moneysubuu.model.TransactionCategory
import lab.justonebyte.moneysubuu.ui.components.*


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
    


    fun clearStates(){
        isAddOrEditTransactionDialogOpen.value = false
        isChooseAddTransactionTypeOpen.value = false
        isSelectedTransactionEditMode.value = null
        isDeleteTransactionDialogOpen.value = false
        currentTransaction.value = null
        currentType.value = 1
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
        isOpen = isChooseAddTransactionTypeOpen.value,
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
            title = null,
            positiveBtnText =null,
            negativeBtnText = null,
            content = {

                Column() {

                        Card(
                            Modifier.heightIn(min = 500.dp, max = 1000.dp),
                        ) {
                            AddTransactionSheetContent(
                                currentType = currentType.value,
                                currentTransaction = currentTransaction.value,
                                categories =  homeUiState.categories,
                                onConfirmTransactionForm = { type, amount, category,date->
                                    Log.i("on confirm sheet",if(currentTransaction.value!=null) "yes" else "no")
                                    if(currentTransaction.value==null) {
                                        println("edit:transaction:add")

                                        homeViewModel.addTransaction(
                                            transactionCategory = category,
                                            type = type,
                                            amount = amount,
                                            date = date
                                        )
                                    } else {
                                        println("edit:transaction:update")
                                        currentTransaction.value?.let {
                                            homeViewModel.updateTransaction(
                                                transactionId = it.id,
                                                transactionCategory = category,
                                                type = type,
                                                amount = amount,
                                                date = date
                                            )
                                        }
                                    }
                                    clearStates()

                                },
                                onCloseBottomSheet = {
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
                                            id = 0,
                                            transaction_type = type,
                                            name = name,
                                            created_at = System.currentTimeMillis()
                                        )
                                    )
                                },
                                isBottomSheetOpened = isChooseAddTransactionTypeOpen.value
                            )
                        }
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
                topBar = {
                    TopAppBar(
                        title = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Home, contentDescription = "s")
                                Text(
                                    "X Money Tracker",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        },
                        navigationIcon = {

                        },
                        actions = {

                        }
                    )
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
            HomeContent(
                homeUiState = homeUiState,
                collectBalanceOfDay = {
                    homeViewModel.collectDailyBalance(it)
                                      },
                onMonthPicked = {
                    homeViewModel.collectMonthlyBalance(it)
                },
                onYearPicked = {
                    homeViewModel.collectYearlyBalance(it)
                },
                onTransactionClick = {
                    currentTransaction.value = it
                },
                onTypeChanged = { type->
                    when(type){
                        BalanceType.DAILY->homeViewModel.collectDailyBalance()
                        BalanceType.MONTHLY->homeViewModel.collectMonthlyBalance()
                        BalanceType.YEARLY->homeViewModel.collectYearlyBalance()
                        else->homeViewModel.collectTotalBalance()
                    }
                },
            )
        }

    }

}