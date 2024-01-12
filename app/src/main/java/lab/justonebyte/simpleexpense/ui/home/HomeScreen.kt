package lab.justonebyte.simpleexpense.ui.home

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import compose.icons.FeatherIcons
import compose.icons.feathericons.Plus
import kotlinx.coroutines.launch
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.model.BalanceType
import lab.justonebyte.simpleexpense.model.Transaction
import lab.justonebyte.simpleexpense.model.TransactionCategory
import lab.justonebyte.simpleexpense.ui.components.AppAlertDialog
import lab.justonebyte.simpleexpense.ui.components.ChooseTransactionTypeTab
import lab.justonebyte.simpleexpense.ui.components.SnackBarType
import java.util.UUID


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState", "UnusedMaterial3ScaffoldPaddingParameter")
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
    fun onAddOrEditTransaction(
        type:Int,
        amount:Int,
        category:TransactionCategory,
        date:Long,
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
        },
        onDismiss = {
            isChooseAddTransactionTypeOpen.value = false
        }
    )
    ChooseTransactionActionDialog(
        isOpen = (currentTransaction.value!=null) && (isSelectedTransactionEditMode.value != true && !isDeleteTransactionDialogOpen.value),
        onEdit = {  isSelectedTransactionEditMode.value = true  },
        onDelete = { isDeleteTransactionDialogOpen.value = true },
        onDismiss = {
            currentTransaction.value = null
        }
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
                                            created_at = System.currentTimeMillis(),
                                            updated_at = System.currentTimeMillis()
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    clearStates()
                    isChooseAddTransactionTypeOpen.value = true
                },
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Icon(imageVector = FeatherIcons.Plus, "Localized description")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },

        ) {
        Column(Modifier.padding(it)) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.absolutePadding(top = 30.dp, bottom = 20.dp).fillMaxWidth()
            ) {
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
                },
                homeViewModel = homeViewModel
            )
        }

    }
}