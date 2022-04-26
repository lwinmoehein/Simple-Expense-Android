package lab.justonebyte.moneysubuu.ui.home

import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import lab.justonebyte.moneysubuu.model.TransactionCategory
import lab.justonebyte.moneysubuu.model.TransactionType
import lab.justonebyte.moneysubuu.ui.appContentPadding
import lab.justonebyte.moneysubuu.utils.dateFormatter
import java.util.*
import lab.justonebyte.moneysubuu.ui.components.*


@Composable
fun AddTransactionSheetContent(
    onCloseBottomSheet:()->Unit,
    categories:List<TransactionCategory>,
    showIncorrectDataSnack:()->Unit,
    onConfirmTransactionForm:(type:Int,amount:Int,category: TransactionCategory,date:String)->Unit,
    onAddCategory:(categoryName:String,transactionType:TransactionType)->Unit,
    initialType:TransactionType = TransactionType.Income,
    initialAmount:String = "",
    initialCategory:TransactionCategory? = null,
    initialDate:String = dateFormatter(System.currentTimeMillis()),
    isEditMode:Boolean = false
) {
    val mContext = LocalContext.current
    val localFocusManage = LocalFocusManager.current
    val mCalendar = Calendar.getInstance()
    mCalendar.time = Date()
    val mYear: Int = mCalendar.get(Calendar.YEAR)
    val mMonth: Int = mCalendar.get(Calendar.MONTH)
    val mDay: Int = mCalendar.get(Calendar.DAY_OF_MONTH)

    //initialize form variables
    val currentType = remember { mutableStateOf(initialType.value) }
    val currentAmount = remember { mutableStateOf(initialAmount) }
    val currentCategory = remember { mutableStateOf(initialCategory) }
    val mDate = remember { mutableStateOf(initialDate) }



    fun clearTransactionForm() {
        currentAmount.value = ""
        currentCategory.value = null
        currentType.value = 1
        mDate.value = dateFormatter(System.currentTimeMillis())
        localFocusManage.clearFocus()
    }


    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate.value =
                "$mYear-${if (mMonth + 1 >= 10) mMonth + 1 else "0" + (mMonth + 1)}-$mDayOfMonth"
        }, mYear, mMonth, mDay
    )

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
                Text(text = "Add transaction", style = MaterialTheme.typography.subtitle1)
            }
            IconButton(onClick = { onCloseBottomSheet() }) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = "close sheet")
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TextButton(
                modifier = Modifier.weight(1f),
                border = BorderStroke(
                    2.dp,
                    if (currentType.value == TransactionType.Income.value) MaterialTheme.colors.primary else Color.Transparent
                ),
                onClick = { currentType.value = TransactionType.Income.value }
            ) {
                Text(
                    text = "Income",
                    style = MaterialTheme.typography.subtitle1,
                    color = if (currentType.value == TransactionType.Income.value) MaterialTheme.colors.primary else Color.Black
                )
            }
            TextButton(
                modifier = Modifier.weight(1f),
                border = BorderStroke(
                    2.dp,
                    if (currentType.value == TransactionType.Expense.value) MaterialTheme.colors.primary else Color.Transparent
                ),
                onClick = { currentType.value = TransactionType.Expense.value }
            ) {
                Text(
                    text = "Expense",
                    style = MaterialTheme.typography.subtitle1,
                    color = if (currentType.value == TransactionType.Expense.value) MaterialTheme.colors.primary else Color.Black
                )
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
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.subtitle2
                )
                CustomTextField(
                    text = currentAmount.value,
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                        .height(50.dp),
                    placeholderText = "Amount in Kyat",
                    onValueChange = {
                        currentAmount.value = it.filter { it.isDigit() }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
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
                    Text(text = mDate.value)
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(
                onClick = { onCloseBottomSheet() },
                modifier = Modifier
                    .absolutePadding(left = 10.dp, right = 10.dp)
                    .weight(1f)
                    .absolutePadding(top = 20.dp, bottom = 20.dp)
                    .clip(RoundedCornerShape(10.dp))
            ) {
                Text(text = "Cancel", style = MaterialTheme.typography.button)
            }
            TextButton(
                modifier = Modifier
                    .absolutePadding(left = 10.dp, right = 10.dp)
                    .weight(1f)
                    .absolutePadding(top = 20.dp, bottom = 20.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colors.primary),
                onClick = {
                    val amount =
                        if (currentAmount.value.isEmpty()) 0 else currentAmount.value.toInt()

                    if (currentCategory.value == null || amount <= 0) {
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

                }) {
                Text(text = if(isEditMode) "Confirm Edit" else "Add Transaction", color = Color.White)
            }
        }
    }
}
