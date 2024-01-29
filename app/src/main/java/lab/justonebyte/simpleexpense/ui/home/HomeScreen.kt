package lab.justonebyte.simpleexpense.ui.home

import android.annotation.SuppressLint
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.canopas.lib.showcase.IntroShowcase
import com.canopas.lib.showcase.component.ShowcaseStyle
import compose.icons.FeatherIcons
import compose.icons.feathericons.Plus
import kotlinx.coroutines.launch
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.model.ShowCase
import lab.justonebyte.simpleexpense.model.Transaction
import lab.justonebyte.simpleexpense.model.TransactionCategory
import lab.justonebyte.simpleexpense.ui.components.AppAlertDialog
import lab.justonebyte.simpleexpense.ui.components.AppDialog
import lab.justonebyte.simpleexpense.ui.components.ChooseTransactionTypeTab
import lab.justonebyte.simpleexpense.ui.components.SnackBarType
import java.util.UUID


@SuppressLint("UnrememberedMutableState", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel
){
    val homeUiState by homeViewModel.viewModelUiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val currentTransaction = remember {
        mutableStateOf<Transaction?>(null)
    }


    val isChooseAddTransactionTypeOpen =  remember { mutableStateOf(false)}
    val isAddTransactionDialogOpen = remember { mutableStateOf(false)}
    val isDeleteTransactionDialogOpen = remember { mutableStateOf(false) }

    fun clearStates(){
        isAddTransactionDialogOpen.value = false
        isChooseAddTransactionTypeOpen.value = false
        isDeleteTransactionDialogOpen.value = false
        currentTransaction.value = null
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
    if (isAddTransactionDialogOpen.value || currentTransaction.value!=null) {
        AppDialog(
            onDismiss = {
                isAddTransactionDialogOpen.value = false
                currentTransaction.value = null
            },
            properties = DialogProperties(decorFitsSystemWindows = true, usePlatformDefaultWidth = false),
            content = {
                Column {
                            AddTransactionContent(
                                currentCurrency = homeUiState.currentCurrency,
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
                                            icon_name = "other",
                                            created_at = System.currentTimeMillis(),
                                            updated_at = System.currentTimeMillis()
                                        )
                                    )
                                },
                                onDeleteTransaction = { transaction ->
                                    homeViewModel.deleteTransaction(transaction)
                                }
                            )
                        }
            }
        )
    }



    Scaffold(
        floatingActionButton = {
            IntroShowcase(
                showIntroShowCase = homeUiState.currentAppShowcaseStep==ShowCase.ADD_TRANSACTION,
                dismissOnClickOutside = true,
                onShowCaseCompleted = {
                    homeViewModel.updateAppIntroStep(ShowCase.BALANCE_CARD)
                },
            ) {
                FloatingActionButton(
                    onClick = {
                        clearStates()
                        isAddTransactionDialogOpen.value = true
                    },
                    shape = MaterialTheme.shapes.extraLarge,
                    modifier =  Modifier.introShowCaseTarget(
                        index = 0,
                        style = ShowcaseStyle.Default.copy(
                            backgroundColor = MaterialTheme.colorScheme.primary, // specify color of background
                            backgroundAlpha = 0.98f, // specify transparency of background
                            targetCircleColor = Color.White // specify color of target circle
                        ),
                        // specify the content to show to introduce app feature
                        content = {
                            Column {
                                Text(
                                    text = stringResource(id = R.string.showcase_add_transaction),
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = stringResource(R.string.showcase_add_transaction_description),
                                    color = Color.White,
                                    fontSize = 16.sp
                                )

                            }
                        }
                    )
                ) {
                    Icon(imageVector = FeatherIcons.Plus, "Localized description")
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        ) {
        Column(Modifier.padding(it)) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .absolutePadding(bottom = 10.dp)
                    .fillMaxWidth()
            ) {
                    ChooseTransactionTypeTab(
                        balanceType =  homeUiState.currentBalanceType,
                        onTypeChanged = { type->
                           coroutineScope.launch {
                               homeViewModel.bindTransactionsFromBalanceType(type)
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