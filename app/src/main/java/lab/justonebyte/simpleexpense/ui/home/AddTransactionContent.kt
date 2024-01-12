package lab.justonebyte.simpleexpense.ui.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.commandiron.wheel_picker_compose.WheelDatePicker
import compose.icons.FeatherIcons
import compose.icons.feathericons.DollarSign
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.model.Currency
import lab.justonebyte.simpleexpense.model.Transaction
import lab.justonebyte.simpleexpense.model.TransactionCategory
import lab.justonebyte.simpleexpense.model.TransactionType
import lab.justonebyte.simpleexpense.ui.components.AppAlertDialog
import lab.justonebyte.simpleexpense.ui.components.NumberKeyboard
import lab.justonebyte.simpleexpense.utils.convertDateStringToTimestamp
import lab.justonebyte.simpleexpense.utils.getCurrentDayTimeMillisecond
import java.time.LocalDate


@Composable
fun AddTransactionContent(
    currentCurrency: Currency,
    currentType: Int,
    onCloseDialog:()->Unit,
    categories:List<TransactionCategory>,
    showIncorrectDataSnack:()->Unit,
    onConfirmTransactionForm:(type:Int,amount:Int,category: TransactionCategory,date:Long,note:String?)->Unit,
    onAddCategory:(categoryName:String,transactionType:TransactionType)->Unit,
    currentTransaction:Transaction?
) {
    val localFocusManage = LocalFocusManager.current
    val currentType = remember(currentTransaction) { mutableStateOf(currentTransaction?.type?.value ?: currentType) }
    val currentAmount = remember(currentTransaction) { mutableStateOf(currentTransaction?.amount?.toString() ?: "") }
    val note = remember(currentTransaction) { mutableStateOf(currentTransaction?.note ?: "") }
    val currentCategory = remember(currentTransaction) { mutableStateOf(currentTransaction?.category) }
    val isEditMode = currentTransaction != null
    val isAddTransactionConfirmDialogOpen = remember { mutableStateOf(false) }

    val isRecordDatePickerShown = remember { mutableStateOf(false) }
    val tempRecordDate =  remember { mutableStateOf<LocalDate?>(null)}
    val isNumberKeyboardShown = remember { mutableStateOf(false) }

    fun clearTransactionForm() {
        currentAmount.value = ""
        currentType.value = 1
        currentCategory.value = null
        tempRecordDate.value = null
        localFocusManage.clearFocus()
    }

    if(isRecordDatePickerShown.value){
        AppAlertDialog (
            onPositiveBtnClicked = {
                isRecordDatePickerShown.value = false
                //onFromDateChosen(tempRecordDate.value.toString())
            },
            positiveBtnText = stringResource(id = R.string.confirm)
        ){
            WheelDatePicker(
                onSnappedDate = {
                    tempRecordDate.value = it
                }
            )
        }
    }

    if (isAddTransactionConfirmDialogOpen.value) {
        AppAlertDialog(
            title = stringResource(R.string.r_u_sure),
            positiveBtnText = stringResource(id = R.string.confirm),
            negativeBtnText = stringResource(id = R.string.cancel),
            content = {
                      Text(stringResource(id = if(isEditMode) R.string.r_u_sure_tran_edit else R.string.r_u_sure_tran_add))
            },
            onPositiveBtnClicked = {
                isAddTransactionConfirmDialogOpen.value = false
                val category = currentCategory.value
                val isValidCategorySelected =if(category==null) false else !categories.filter { it.transaction_type.value == currentType.value && it.unique_id== category.unique_id  }.isEmpty()
                val amount =
                    if (currentAmount.value.isEmpty()) 0 else currentAmount.value.toInt()
                if (!isValidCategorySelected || amount <= 0) {
                    showIncorrectDataSnack()
                } else {
                    Log.i("date:temp",tempRecordDate.value.toString())
                    Log.i("date:converted", convertDateStringToTimestamp(tempRecordDate.value.toString()).toString())
                    Log.i("date:cdaytime",getCurrentDayTimeMillisecond().toString())

                    val transactionCreatedDate  = convertDateStringToTimestamp(tempRecordDate.value.toString())+getCurrentDayTimeMillisecond()
                    currentCategory.value?.let {
                        onConfirmTransactionForm(
                                currentType.value,
                                amount,
                                it,
                                transactionCreatedDate,
                                note.value
                            )
                        onCloseDialog()
                        clearTransactionForm()
                    }
                }
            },
            onNegativeBtnClicked = {
                isAddTransactionConfirmDialogOpen.value =false
            }
        )
    }

    Column(
        Modifier
            .fillMaxSize()
    ) {

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
                onAddCategory = {
                    onAddCategory(
                        it,
                        if (currentType.value == TransactionType.Income.value) TransactionType.Income else TransactionType.Expense
                    )
                },
                categories = categories,
                currentCategory = currentCategory.value,
                onCategoryChosen = {
                    currentCategory.value = it
                },
                currentTransactionType = if (currentType.value == TransactionType.Income.value) TransactionType.Income else TransactionType.Expense
            )
            if(!isEditMode){
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text =  stringResource(id = R.string.enter_date),
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(
                        onClick = {
                            isRecordDatePickerShown.value = true
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = tempRecordDate.value.toString(),
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
                    onClick = { isAddTransactionConfirmDialogOpen.value = true }
                ) {
                    Text(text = if(isEditMode) stringResource(id = R.string.confirm_edit) else stringResource(id = R.string.confirm))
                }
            }
        }
    }
}
