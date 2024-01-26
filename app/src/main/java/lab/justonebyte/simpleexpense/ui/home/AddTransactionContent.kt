package lab.justonebyte.simpleexpense.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.model.BalanceType
import lab.justonebyte.simpleexpense.model.Currency
import lab.justonebyte.simpleexpense.model.Transaction
import lab.justonebyte.simpleexpense.model.TransactionCategory
import lab.justonebyte.simpleexpense.model.TransactionType
import lab.justonebyte.simpleexpense.ui.components.NumberKeyboard
import lab.justonebyte.simpleexpense.ui.components.OptionItem
import lab.justonebyte.simpleexpense.utils.getCurrentDayFromTimestamp
import lab.justonebyte.simpleexpense.utils.getCurrentYear

sealed class TransactionTypeTab(override val value:Int, override val name:Int, val transactionType:TransactionType): OptionItem {
    object EXPENSE : TransactionTypeTab(1, R.string.expense,TransactionType.Expense)
    object INCOME : TransactionTypeTab(2, R.string.income,TransactionType.Income)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionContent(
    currentCurrency: Currency,
    onCloseDialog:()->Unit,
    categories:List<TransactionCategory>,
    showIncorrectDataSnack:()->Unit,
    onConfirmTransactionForm:(type:Int,amount:Int,category: TransactionCategory,date:Long,note:String?)->Unit,
    onAddCategory:(categoryName:String,transactionType:TransactionType)->Unit,
    currentTransaction:Transaction?
) {
    val currentTimeInMillis = System.currentTimeMillis()
    val oneDayInMillis = 24 * 60 * 60 * 1000
    val tomorrowInMillis = currentTimeInMillis + oneDayInMillis

    val localFocusManage = LocalFocusManager.current
    val currentAmount = remember(currentTransaction) { mutableStateOf(currentTransaction?.amount?.toString() ?: "") }
    val note = remember(currentTransaction) { mutableStateOf(currentTransaction?.note ?: "") }
    val currentTransactionType: MutableState<TransactionType> = remember(currentTransaction) { mutableStateOf(
        currentTransaction?.type ?: TransactionType.Expense)
    }
    val currentCategory = remember(currentTransactionType) { mutableStateOf(currentTransaction?.category?: categories.first { it.transaction_type == currentTransactionType.value }) }
    val isEditMode = currentTransaction != null
    val isRecordDatePickerShown = remember { mutableStateOf(false) }
    val tempRecordDate =  remember { mutableStateOf(System.currentTimeMillis()) }
    val isNumberKeyboardShown = remember { mutableStateOf(false) }
    val state = rememberDatePickerState(initialSelectedDateMillis = tomorrowInMillis, yearRange = 2020..getCurrentYear().toInt())

    var transactionTypeTabState by remember { mutableStateOf(0) }
    val transactionTypeTabs = listOf(TransactionTypeTab.EXPENSE,TransactionTypeTab.INCOME)


    fun clearTransactionForm() {
        currentAmount.value = ""
        currentTransactionType.value = TransactionType.Expense
        currentCategory.value = categories.first()
        tempRecordDate.value = System.currentTimeMillis()
        localFocusManage.clearFocus()
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

            TabRow(selectedTabIndex = transactionTypeTabState) {
                transactionTypeTabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = transactionTypeTabState == index,
                        onClick = {
                            transactionTypeTabState = index
                            currentTransactionType.value = tab.transactionType
                            currentCategory.value = currentTransaction?.category?: categories.first { it.transaction_type == currentTransactionType.value }
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
                onAddCategory = { categoryName->
                    onAddCategory(
                        categoryName,
                        currentTransactionType.value
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
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 15.dp).fillMaxWidth()
                    ) {
                        Text(
                            text =  stringResource(id = R.string.enter_date),
                            )
                        TextButton(
                            onClick = {
                                isRecordDatePickerShown.value = true
                            },
                         ) {
                            Text(
                                text = getCurrentDayFromTimestamp(tempRecordDate.value),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
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
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .absolutePadding(top = 50.dp)
            ) {
                TextButton(
                    modifier = Modifier.absolutePadding(left = 10.dp, right = 10.dp),
                    onClick = { onCloseDialog() }
                ) {
                    Text(text = stringResource(id = R.string.cancel))
                }
                Button(
                    onClick = {
                        val category = currentCategory.value
                        val isValidCategorySelected =if(category==null) false else !categories.filter { it.transaction_type == currentTransactionType.value && it.unique_id== category.unique_id  }.isEmpty()
                        val amount =
                            if (currentAmount.value.isEmpty()) 0 else currentAmount.value.toInt()
                        if (!isValidCategorySelected || amount <= 0) {
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
                    }
                ) {
                    Text(text = if(isEditMode) stringResource(id = R.string.confirm_edit) else stringResource(id = R.string.confirm))
                }
            }
        }
    }
}
