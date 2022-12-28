package lab.justonebyte.moneysubuu.ui.home

import android.app.Activity
import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import lab.justonebyte.moneysubuu.model.Transaction
import lab.justonebyte.moneysubuu.model.TransactionCategory
import lab.justonebyte.moneysubuu.model.TransactionType
import lab.justonebyte.moneysubuu.ui.appContentPadding
import lab.justonebyte.moneysubuu.ui.components.*
import lab.justonebyte.moneysubuu.ui.theme.*
import lab.justonebyte.moneysubuu.utils.dateFormatter
import java.util.*


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddTransactionSheetContent(
    currentType: Int,
    onCloseBottomSheet:()->Unit,
    categories:List<TransactionCategory>,
    showIncorrectDataSnack:()->Unit,
    onConfirmTransactionForm:(type:Int,amount:Int,category: TransactionCategory,date:String)->Unit,
    onAddCategory:(categoryName:String,transactionType:TransactionType)->Unit,
    currentTransaction:Transaction?,
    isBottomSheetOpened:Boolean
) {
    val mContext = LocalContext.current
    val localFocusManage = LocalFocusManager.current
    val focusRequester = remember() {
        FocusRequester()
    }
    val isAmountInputFocused = remember{ mutableStateOf(false)}

    BackHandler(enabled = true) {
        if (isAmountInputFocused.value) {
            localFocusManage.clearFocus()
            if(isBottomSheetOpened){
                onCloseBottomSheet()
            }
        }else{
           if(!isBottomSheetOpened){
               (mContext as? Activity)?.finish()
           }else{
               onCloseBottomSheet()
           }
        }


    }

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
        GeneralDialog(
            dialogState = isAddTransactionConfirmDialogOpen,
            title = "Are you sure?",
            positiveBtnText = "Confirm",
            negativeBtnText = "Cancel",
            message = "Are you sure adding this record to your money database?",
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
                        onCloseBottomSheet()
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
            .padding(appContentPadding)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
        ) {
            Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxHeight()) {
                Text(text = if(isEditMode) "Edit transaction" else "Add New Transaction", style = MaterialTheme.typography.subtitle1)
            }
            IconButton(onClick = { onCloseBottomSheet() }) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = "close sheet")
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Card(elevation = 8.dp) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .absolutePadding(left = 10.dp, right = 10.dp)

            ) {
                Text(
                    "Amount in Kyat: ",
                    modifier = Modifier.weight(1f).absolutePadding(top = 10.dp, bottom = 10.dp),
                    style = MaterialTheme.typography.subtitle2
                )
//                CustomTextField(
//                    focusRequester = focusRequester,
//                    onFocusChanged = {
//                        isAmountInputFocused.value= it.isFocused
//                    },
//                    text = currentAmount.value,
//                    modifier = Modifier
//                        .weight(1f)
//                        .padding(4.dp)
//                        .height(50.dp),
//                    placeholderText = "Amount in Kyat",
//                    onValueChange = {
//                        currentAmount.value = it.filter { it.isDigit() }
//                    },
//                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
//
//                )
                TextInputFieldOne(
                    background = Color.Transparent,
                    textFieldValue = remember { mutableStateOf(TextFieldValue(currentAmount.value)) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(0.dp),
                    onValueChange = {
                        currentAmount.value = it.text
                    },
                    placeholder = "Enter amount",
                    keyboardType = KeyboardType.Number,
                    textColor = categoryColor
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
            Card(
                elevation = 8.dp
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Date : ",
                        style = MaterialTheme.typography.subtitle2,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(
                        onClick = { mDatePickerDialog.show() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = mDate.value, color = categoryColor)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedButton(
                onClick = { onCloseBottomSheet() },
                modifier = Modifier
                    .absolutePadding(left = 10.dp, right = 10.dp)
                    .weight(1f)
                    .absolutePadding(top = 20.dp, bottom = 20.dp)
                    .clip(RoundedCornerShape(10.dp))
            ) {
                Text(text = "Cancel", style = MaterialTheme.typography.button, color = categoryColor)
            }
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = categoryColor),
                onClick = { isAddTransactionConfirmDialogOpen.value = true },
                modifier = Modifier
                    .absolutePadding(left = 10.dp, right = 10.dp)
                    .weight(1f)
                    .absolutePadding(top = 20.dp, bottom = 20.dp)
                    .clip(RoundedCornerShape(10.dp))
            ) {
                Text(text = if(isEditMode) "Confirm Edit" else "Confirm", style = MaterialTheme.typography.button, color = MaterialTheme.colors.onPrimary)
            }
        }
    }
}
