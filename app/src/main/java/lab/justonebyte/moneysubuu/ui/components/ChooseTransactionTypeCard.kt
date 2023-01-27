package lab.justonebyte.moneysubuu.ui.components

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import lab.justonebyte.moneysubuu.model.BalanceType
import lab.justonebyte.moneysubuu.ui.home.*
import lab.justonebyte.moneysubuu.ui.theme.SuBuuShapes
import lab.justonebyte.moneysubuu.utils.getCurrentMonth
import lab.justonebyte.moneysubuu.utils.getCurrentDate
import lab.justonebyte.moneysubuu.utils.getCurrentYear
import java.util.*
import lab.justonebyte.moneysubuu.R

@SuppressLint("UnrememberedMutableState")
@Composable
fun ChooseTransactionTypeCard(
    modifier: Modifier = Modifier,
    selectedDay: String,
    selectedMonth: String,
    selectedYear: String,
    balanceType: BalanceType,
    onDatePicked: (day: String) -> Unit,
    onMonthPicked: (month:String) -> Unit,
    onYearPicked: (year:String) -> Unit,
    onTypeChanged: (type: BalanceType) -> Unit
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
        else->stringResource(id = R.string.total) }


    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate.value = "$mYear-${if(mMonth+1>=10) mMonth+1 else "0"+(mMonth+1)}-${if(mDayOfMonth<10) "0"+mDayOfMonth else mDayOfMonth}"
            onDatePicked(mDate.value)
        }, mYear, mMonth-1, mDay
    )

    val balanceTypeOptions = listOf(
        OptionItem(R.string.daily, BalanceType.DAILY),
        OptionItem(R.string.monthly, BalanceType.MONTHLY),
        OptionItem(R.string.yearly, BalanceType.YEARLY),
        OptionItem(R.string.total, BalanceType.TOTAL)
    )


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


    SectionTitle(title = stringResource(id = R.string.choose_data))

    Card(
        shape = SuBuuShapes.small,
        elevation = 2.dp,
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .absolutePadding(top = 0.dp, left = 10.dp, right = 10.dp)
            .clickable {
            },
        contentColor = MaterialTheme.colors.primary
    ) {
        Row(
            modifier= Modifier.padding(10.dp)
        ){
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(id = R.string.show_data_by),modifier= Modifier.fillMaxWidth().absolutePadding(bottom = 5.dp))
                if(balanceType!= BalanceType.TOTAL){
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = ""+when (balanceType) {
                        BalanceType.DAILY -> "${stringResource(R.string.select_date)} : "
                        BalanceType.MONTHLY -> "${stringResource(R.string.select_month)} : "
                        BalanceType.YEARLY -> "${stringResource(R.string.select_year)} : "
                        else->{} }
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
            ) {

                AppOption(
                    modifier = Modifier
                        .fillMaxWidth()
                        .absolutePadding(bottom = 5.dp),
                    label = "Select",
                    options = balanceTypeOptions,
                    onItemSelected = {
                        currentBalanceType.value = it.value as BalanceType
                        onTypeChanged(currentBalanceType.value)
                    },
                    selectedOption = when(currentBalanceType.value){
                        BalanceType.DAILY->balanceTypeOptions[0]
                        BalanceType.MONTHLY->balanceTypeOptions[1]
                        BalanceType.YEARLY->balanceTypeOptions[2]
                        else->balanceTypeOptions[3]
                    }
                )


                if(balanceType!= BalanceType.TOTAL){
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                when (balanceType) {
                                    BalanceType.DAILY -> mDatePickerDialog.show()
                                    BalanceType.MONTHLY -> isMonthPickerShown.value = true
                                    BalanceType.YEARLY -> isMonthPickerShown.value = true
                                    else -> {}
                                }
                            },
                        text =  chosenDateString
                    )
                }
            }
        }
    }
}