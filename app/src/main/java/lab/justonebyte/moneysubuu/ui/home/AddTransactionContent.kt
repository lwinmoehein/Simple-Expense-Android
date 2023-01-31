package lab.justonebyte.moneysubuu.ui.home

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import lab.justonebyte.moneysubuu.R
import lab.justonebyte.moneysubuu.model.Transaction
import lab.justonebyte.moneysubuu.model.TransactionCategory
import lab.justonebyte.moneysubuu.model.TransactionType
import lab.justonebyte.moneysubuu.ui.appContentPadding
import lab.justonebyte.moneysubuu.ui.components.*
import lab.justonebyte.moneysubuu.ui.theme.*
import lab.justonebyte.moneysubuu.utils.dateFormatter
import java.util.*


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionContent(
    currentType: Int,
    onCloseDialog:()->Unit,
    categories:List<TransactionCategory>,
    showIncorrectDataSnack:()->Unit,
    onConfirmTransactionForm:(type:Int,amount:Int,category: TransactionCategory,date:String)->Unit,
    onAddCategory:(categoryName:String,transactionType:TransactionType)->Unit,
    currentTransaction:Transaction?
) {
    val mContext = LocalContext.current
    val localFocusManage = LocalFocusManager.current

    val mCalendar = Calendar.getInstance()
    mCalendar.time = Date()
    val mYear: Int = mCalendar.get(Calendar.YEAR)
    val mMonth: Int = mCalendar.get(Calendar.MONTH)
    val mDay: Int = mCalendar.get(Calendar.DAY_OF_MONTH)

    //initialize form variables
    val currentType = remember(currentTransaction) { mutableStateOf(currentTransaction?.type?.value ?: currentType) }
    val currentAmount = remember(currentTransaction) { mutableStateOf(currentTransaction?.amount?.toString() ?: "") }
    val currentCategory = remember(currentTransaction) { mutableStateOf(currentTransaction?.category) }
    val mDate = remember (currentTransaction){ mutableStateOf(currentTransaction?.created_at ?: dateFormatter(System.currentTimeMillis())) }
    val isEditMode = currentTransaction != null

    val isAddTransactionConfirmDialogOpen = remember { mutableStateOf(false) }
    val categoryColor = if(currentType.value==1) positiveColor else negativeColor

    fun clearTransactionForm() {
        currentAmount.value = ""
        currentType.value = 1
        currentCategory.value = null
        mDate.value = dateFormatter(System.currentTimeMillis())
        localFocusManage.clearFocus()
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
                val isValidCategorySelected =if(category==null) false else !categories.filter { it.transaction_type.value == currentType.value && it.id== category.id  }.isEmpty()
                val amount =
                    if (currentAmount.value.isEmpty()) 0 else currentAmount.value.toInt()

                if (!isValidCategorySelected || amount <= 0) {
                    showIncorrectDataSnack()
                } else {
                    currentCategory.value?.let {
                        onConfirmTransactionForm(
                            currentType.value,
                            amount,
                            it,
                            mDate.value.replace('/', '-'),

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


    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            var selectedDate = "$mYear-${if (mMonth + 1 >= 10) (mMonth + 1) else ("0" +(mMonth + 1))}-${if (mDayOfMonth + 1 >= 10) mDayOfMonth else ("0$mDayOfMonth")}"
            mDate.value = selectedDate
        }, mYear, mMonth, mDay
    )
    mDatePickerDialog.datePicker.maxDate = Date().time

    Column(
        Modifier
            .fillMaxSize()
    ) {

        Spacer(modifier = Modifier.height(20.dp))
        Column() {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
//                Text(
//                    stringResource(id = R.string.enter_amount),
//                    modifier = Modifier
//                        .weight(1f)
//                        .absolutePadding(top = 10.dp, bottom = 10.dp)
//                )

                TextField(
                    shape = MaterialTheme.shapes.small,
                    singleLine = true,
                    leadingIcon = {
                         Image(painterResource(id = R.drawable.ic_baseline_attach_money_24), contentDescription ="" )
                    } ,
                    modifier = Modifier.fillMaxWidth(),
                    value = currentAmount.value,
                    onValueChange = { currentAmount.value = it },
                    label = { Text(stringResource(id = R.string.enter_amount)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        disabledTextColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )

            }
        }
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
            Column(
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text =  stringResource(id = R.string.enter_date),
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(
                        onClick = { mDatePickerDialog.show() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = mDate.value)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
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
