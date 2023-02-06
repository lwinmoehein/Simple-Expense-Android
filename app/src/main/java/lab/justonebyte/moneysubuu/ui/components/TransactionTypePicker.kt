package lab.justonebyte.moneysubuu.ui.components

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import lab.justonebyte.moneysubuu.R
import lab.justonebyte.moneysubuu.model.BalanceType
import lab.justonebyte.moneysubuu.ui.home.MonthPicker
import lab.justonebyte.moneysubuu.utils.getCurrentDate
import lab.justonebyte.moneysubuu.utils.getCurrentMonth
import lab.justonebyte.moneysubuu.utils.getCurrentYear
import java.util.*

@SuppressLint("UnrememberedMutableState")
@Composable
fun TransactionTypePicker(
    modifier: Modifier = Modifier,
    selectedDay: String,
    selectedMonth: String,
    selectedYear: String,
    balanceType: BalanceType,
    onDatePicked: (day: String) -> Unit,
    onMonthPicked: (month:String) -> Unit,
    onYearPicked: (year:String) -> Unit
){
    val mContext = LocalContext.current

    val currentDay = getCurrentDate()
    val currentMonth = getCurrentMonth()
    val currentYear = getCurrentYear()

    val calendar = Calendar.getInstance()
    val currentBalanceType = mutableStateOf(balanceType)

    //for date picker
    val mYear by remember(selectedDay) { mutableStateOf(selectedDay.split('-')[0].toInt()) }
    val mMonth by remember(selectedDay){ mutableStateOf(selectedDay.split('-')[1].toInt()) }
    val mDay by remember(selectedDay) { mutableStateOf(selectedDay.split('-')[2].toInt()) }

    //for month picker
    val selectedMonthYear = remember { mutableStateOf(calendar.get(Calendar.YEAR)) }
    val selectedMonthMonth = remember { mutableStateOf(calendar.get(Calendar.MONTH)+1) }

    val isMonthPickerShown = remember { mutableStateOf(false) }
    val mDate = remember { mutableStateOf(selectedDay) }

    val chosenDateString = when(balanceType){
        BalanceType.DAILY-> if(selectedDay==currentDay) stringResource(id = R.string.this_day) else selectedDay
        BalanceType.MONTHLY->if(selectedMonth==currentMonth) stringResource(id = R.string.this_month) else selectedMonth
        BalanceType.YEARLY->if(selectedYear==currentYear) stringResource(id = R.string.this_year) else selectedYear
        else-> stringResource(id = R.string.total) }


    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate.value = "$mYear-${if(mMonth+1>=10) mMonth+1 else "0"+(mMonth+1)}-${if(mDayOfMonth<10) "0"+mDayOfMonth else mDayOfMonth}"
            onDatePicked(mDate.value)
        }, mYear, mMonth-1, mDay
    )

    val balanceTypeOptions = listOf(
        BalanceTypeOption.DAILY,BalanceTypeOption.MONTHLY,BalanceTypeOption.YEARLY,BalanceTypeOption.TOTAL
    )

    var currentHomeTabIndex: Int by remember { mutableStateOf(0) }
    val homeTabs = listOf<OptionItem>(
        BalanceType.DAILY,
        BalanceType.MONTHLY,
        BalanceType.YEARLY,
        BalanceType.TOTAL)


    if(isMonthPickerShown.value){
        Dialog(onDismissRequest = { isMonthPickerShown.value=false }) {
            MonthPicker(
                selectedMonth = selectedMonthMonth.value,
                selectedYear =selectedMonthYear.value ,
                onYearSelected ={
                    selectedMonthYear.value=it
                } ,
                onMonthSelected = {
                    selectedMonthMonth.value=it

                },
                onConfirmPicker = {
                    isMonthPickerShown.value =false
                    if(currentBalanceType.value== BalanceType.MONTHLY){
                        onMonthPicked("${selectedMonthYear.value}-${if(selectedMonthMonth.value<10) "0"+selectedMonthMonth.value else selectedMonthMonth.value}")
                    }else{
                        onYearPicked(selectedMonthYear.value.toString())
                    }
                },
                isMonthPicker = currentBalanceType.value== BalanceType.MONTHLY
            )
        }
    }
//    Text(modifier = Modifier.clickable {
//            when (balanceType) {
//                BalanceType.DAILY -> mDatePickerDialog.show()
//                BalanceType.MONTHLY -> isMonthPickerShown.value = true
//                BalanceType.YEARLY -> isMonthPickerShown.value = true
//                else -> {}
//            }
//        },
//        text = chosenDateString,
//        style = MaterialTheme.typography.titleSmall
//    )
    OutlinedButton(modifier = modifier, onClick = {
        when (balanceType) {
            BalanceType.DAILY -> mDatePickerDialog.show()
            BalanceType.MONTHLY -> isMonthPickerShown.value = true
            BalanceType.YEARLY -> isMonthPickerShown.value = true
            else -> {}
        }
    }) {
        Text(text = chosenDateString)
    }
}