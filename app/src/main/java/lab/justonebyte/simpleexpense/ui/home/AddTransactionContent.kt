package lab.justonebyte.simpleexpense.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Check
import compose.icons.feathericons.Trash
import compose.icons.feathericons.X
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.model.Currency
import lab.justonebyte.simpleexpense.model.Transaction
import lab.justonebyte.simpleexpense.model.TransactionCategory
import lab.justonebyte.simpleexpense.model.TransactionType
import lab.justonebyte.simpleexpense.ui.components.AppAlertDialog
import lab.justonebyte.simpleexpense.ui.components.NumberKeyboard
import lab.justonebyte.simpleexpense.ui.components.SimpleExpenseSnackBar
import lab.justonebyte.simpleexpense.ui.components.TransactionTypeTab
import lab.justonebyte.simpleexpense.utils.getCurrentDayFromTimestamp
import lab.justonebyte.simpleexpense.utils.getCurrentYear

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionContent(
    homeUiState:HomeUiState,
    homeViewModel:HomeViewModel,
    onCloseDialog:()->Unit,
    showIncorrectDataSnack:()->Unit,
    onConfirmTransactionForm:(type:Int,amount:Int,category: TransactionCategory,date:Long,note:String?)->Unit,
    onAddCategory:(categoryName:String,transactionType:TransactionType)->Unit,
    currentTransaction:Transaction?,
    onDeleteTransaction:(transaction:Transaction)->Unit
) {
    val currentTimeInMillis = System.currentTimeMillis()
    val oneDayInMillis = 24 * 60 * 60 * 1000
    val tomorrowInMillis = currentTimeInMillis + oneDayInMillis

    val currentAmount = remember(currentTransaction) { mutableStateOf(currentTransaction?.amount?.toString() ?: "") }
    val note = remember(currentTransaction) { mutableStateOf(currentTransaction?.note ?: "") }
    val currentTransactionType: MutableState<TransactionType> = remember(currentTransaction) { mutableStateOf(
        currentTransaction?.type ?: TransactionType.Expense)
    }
    val categories = homeUiState.categories
    val currentCurrency = homeUiState.currentCurrency
    val currentCategory = remember(currentTransactionType) { mutableStateOf<TransactionCategory?>(currentTransaction?.category?: categories.firstOrNull { it.transaction_type == currentTransactionType.value }) }
    val isEditMode = currentTransaction != null
    val isRecordDatePickerShown = remember { mutableStateOf(false) }
    val tempRecordDate =  remember { mutableStateOf(currentTransaction?.created_at ?: System.currentTimeMillis()) }
    val isNumberKeyboardShown = remember { mutableStateOf(false) }
    val state = rememberDatePickerState(initialSelectedDateMillis = tomorrowInMillis, yearRange = 2020..getCurrentYear().toInt())

    var transactionTypeTabState by remember { mutableStateOf(0) }
    val transactionTypeTabs = listOf(TransactionTypeTab.EXPENSE, TransactionTypeTab.INCOME)
    val isDeleteConfirmDialogOpen = remember { mutableStateOf(false) }
    val snackBarHostState = remember { SnackbarHostState() }



    fun clearTransactionForm() {
        currentAmount.value = ""
        currentTransactionType.value = TransactionType.Expense
        currentCategory.value = categories.firstOrNull()
        tempRecordDate.value = System.currentTimeMillis()
        isDeleteConfirmDialogOpen.value = false
    }

    if(isDeleteConfirmDialogOpen.value){
        AppAlertDialog(
            title = stringResource(id = R.string.r_u_sure ),
            positiveBtnText = stringResource(id = R.string.confirm),
            negativeBtnText = stringResource(id = R.string.cancel),
            onNegativeBtnClicked = {
                isDeleteConfirmDialogOpen.value = false
            },
            onPositiveBtnClicked = {
                if(currentTransaction!=null){
                    onDeleteTransaction(currentTransaction)
                }
                clearTransactionForm()
                onCloseDialog()
            }
        ) {
            Text(text = stringResource(id = R.string.r_u_sure_tran_delete))
        }
    }

    if(isRecordDatePickerShown.value){
            DatePickerDialog(
                onDismissRequest = {
                    isRecordDatePickerShown.value = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                           tempRecordDate.value = state.selectedDateMillis!!
                           isRecordDatePickerShown.value = false
                        }
                    ) {
                        Text(text=stringResource(id = R.string.confirm))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                           isRecordDatePickerShown.value = false
                        }
                    ) {
                        Text(stringResource(id = R.string.cancel))
                    }
                }
            ) {
                DatePicker(
                    state = state
                )
        }
    }

    Column(
        Modifier
            .fillMaxSize()
    ) {

        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { onCloseDialog() },
                            modifier = Modifier.size(35.dp),
                        ) {
                            Icon(modifier = Modifier
                                .width(22.dp)
                                .height(22.dp),imageVector = FeatherIcons.X, contentDescription ="" )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = if(currentTransaction!=null) stringResource(id = R.string.edit_tran) else stringResource(
                            id = R.string.add_tran
                        ), style = MaterialTheme.typography.titleMedium)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if(currentTransaction!=null){
                            IconButton(
                                modifier = Modifier.size(35.dp),
                                onClick = {
                                    isDeleteConfirmDialogOpen.value = true
                                }
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .width(22.dp)
                                        .height(22.dp),
                                    imageVector = FeatherIcons.Trash, contentDescription ="",
                                    tint = Color.Red
                                )
                            }
                        }
                        IconButton(
                            modifier = Modifier.size(35.dp),
                            onClick = {
                                val amount =
                                    if (currentAmount.value.isEmpty()) 0 else currentAmount.value.toInt()
                                if (amount <= 0) {
                                    showIncorrectDataSnack()
                                } else {
                                    currentCategory.value?.let {
                                        onConfirmTransactionForm(
                                            currentTransactionType.value.value,
                                            amount,
                                            it,
                                            tempRecordDate.value,
                                            note.value
                                        )
                                        onCloseDialog()
                                        clearTransactionForm()
                                    }
                                }
                            }) {
                            Icon(modifier = Modifier
                                .width(22.dp)
                                .height(22.dp),imageVector = FeatherIcons.Check, contentDescription ="" )
                        }
                    }
                }
            },
            snackbarHost = { SnackbarHost(snackBarHostState) },
        ) {
            Column(modifier = Modifier
                .padding(it)
                .padding(horizontal = 10.dp)) {
                SimpleExpenseSnackBar(
                    snackBarType = homeUiState.currentSnackBar,
                    onDismissSnackBar = { homeViewModel.clearSnackBar() },
                    snackbarHostState = snackBarHostState
                )


                TabRow(selectedTabIndex = transactionTypeTabState) {
                    transactionTypeTabs.forEachIndexed { index, tab ->
                        Tab(
                            selected = transactionTypeTabState == index,
                            onClick = {
                                transactionTypeTabState = index
                                currentTransactionType.value = tab.transactionType
                                currentCategory.value = currentTransaction?.category?: categories.firstOrNull { it.transaction_type == currentTransactionType.value }
                            },
                            text = { Text(text = stringResource(id = tab.name), maxLines = 2, overflow = TextOverflow.Ellipsis) }
                        )
                    } }
                Spacer(modifier = Modifier.height(20.dp))
                Column {
                    NumberKeyboard(
                        currency = currentCurrency,
                        initialNumber = currentAmount.value,
                        isKeyboardShown=isNumberKeyboardShown.value,
                        onNumberConfirm = {
                            isNumberKeyboardShown.value = false
                            if(it<0){
                                currentAmount.value = ""
                            }else{
                                currentAmount.value = it.toString()
                            } },
                        onKeyboardToggled = {
                            isNumberKeyboardShown.value = it
                        }
                    )
                }

                if(!isNumberKeyboardShown.value){
                    AddCategoriesCard(
                        onAddCategory = { categoryName,transactionType->
                            onAddCategory(
                                categoryName,
                                transactionType
                            )
                        },
                        categories = categories,
                        currentCategory = currentCategory.value,
                        onCategoryChosen = {
                            currentCategory.value = it
                        },
                        currentTransactionType = currentTransactionType.value
                    )
                    if(!isEditMode){
                        Card(
                            modifier = Modifier.padding(bottom=20.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .padding(vertical = 10.dp, horizontal = 10.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text =  stringResource(id = R.string.enter_date),
                                )

                                Text(
                                    modifier = Modifier.clickable {  isRecordDatePickerShown.value = true },
                                    text = getCurrentDayFromTimestamp(tempRecordDate.value),
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.small,
                            singleLine = false,
                            value = note.value,
                            label = { Text(stringResource(id = R.string.enter_note)) },
                            onValueChange = { note.value = it },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text
                            )
                        )
                    }

                }
            }
        }
    }
}
