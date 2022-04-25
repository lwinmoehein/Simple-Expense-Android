package lab.justonebyte.moneysubuu.ui.home

import android.app.DatePickerDialog
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
import lab.justonebyte.moneysubuu.model.TransactionCategory
import lab.justonebyte.moneysubuu.ui.appContentPadding
import lab.justonebyte.moneysubuu.utils.dateFormatter
import java.util.*
import lab.justonebyte.moneysubuu.ui.components.*


@Composable
fun AddTransactionSheetContent(
    onCloseBottomSheet:()->Unit,
    categories:List<TransactionCategory>,
    showIncorrectDataSnack:()->Unit,
    onAddTransaction:(type:Int,amount:Int,category: TransactionCategory,date:String)->Unit
){

    val mContext = LocalContext.current
    val mYear: Int
    val mMonth: Int
    val mDay: Int
    val mCalendar = Calendar.getInstance()
    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)
    mCalendar.time = Date()
    val mDate = remember { mutableStateOf(dateFormatter(System.currentTimeMillis())) }

    val localFocusManage =  LocalFocusManager.current
    val currentType = remember{ mutableStateOf(1) }
    val currentCategory = remember{ mutableStateOf<TransactionCategory?>(null) }
    val currentAmount = remember {
        mutableStateOf("")
    }

    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate.value = "$mYear-${if(mMonth+1>=10) mMonth+1 else "0"+(mMonth+1)}-$mDayOfMonth"
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
                Icon(imageVector = Icons.Filled.Close, contentDescription = "close sheet" )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier
            .fillMaxWidth()) {
            TextButton(
                modifier = Modifier.weight(1f),
                border = BorderStroke(2.dp,if(currentType.value==1) MaterialTheme.colors.primary else Color.Transparent),
                onClick = { currentType.value =1 }
            ) {
                Text(text = "Income", style = MaterialTheme.typography.subtitle1, color = if(currentType.value==1) MaterialTheme.colors.primary else Color.Black)
            }
            TextButton(
                modifier= Modifier.weight(1f),
                border = BorderStroke(2.dp,if(currentType.value==2) MaterialTheme.colors.primary else Color.Transparent),
                onClick = { currentType.value =2  }
            ) {
                Text(text = "Expense",style = MaterialTheme.typography.subtitle1,color = if(currentType.value==2) MaterialTheme.colors.primary else Color.Black)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Card(elevation = 8.dp) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth().absolutePadding(left = 10.dp, right = 10.dp)
                ) {
                    Text("Amount in Kyat: ",modifier = Modifier.weight(1f),style = MaterialTheme.typography.subtitle2)
                    CustomTextField(
                        text = currentAmount.value,
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                MaterialTheme.colors.surface,
                                RoundedCornerShape(percent = 50)
                            )
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
            categories = categories,
            currentCategory = currentCategory.value,
            onCategoryChosen = {
                currentCategory.value = it
            }
        )
        Card(
            elevation = 8.dp
        ) {
            Row(modifier = Modifier.padding(12.dp),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
                Text(text = "Date : ", style = MaterialTheme.typography.subtitle2,modifier = Modifier.weight(1f))
                TextButton(onClick = { mDatePickerDialog.show() },modifier = Modifier.weight(1f)) {
                    Text(text = mDate.value)
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            TextButton(
                modifier = Modifier

                    .absolutePadding(top = 20.dp, bottom = 20.dp)
                    .background(MaterialTheme.colors.primary)
                    .absolutePadding(left = 40.dp, right = 40.dp)
                    .clip(RoundedCornerShape(10.dp))

                ,
                onClick = {
                    val amount =if(currentAmount.value.isEmpty()) 0 else currentAmount.value.toInt()

                    if(currentCategory.value==null || amount<=0){
                        showIncorrectDataSnack()
                    }else{
                        currentCategory.value?.let {
                            onAddTransaction(
                                currentType.value,
                                amount,
                                it,
                                mDate.value.replace('/','-'),

                            )
                            onCloseBottomSheet()
                            currentAmount.value = ""
                            currentCategory.value = null
                            currentType.value=1
                           localFocusManage.clearFocus()


                        }
                    }

                }) {
                Text(text = "Add Transaction", color = Color.White)
            }
        }
    }
}