package lab.justonebyte.moneysubuu.ui.home

import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import lab.justonebyte.moneysubuu.model.TransactionType
import lab.justonebyte.moneysubuu.ui.theme.Green
import lab.justonebyte.moneysubuu.ui.theme.Red900
import lab.justonebyte.moneysubuu.ui.theme.SuBuuShapes
import lab.justonebyte.moneysubuu.utils.dateFormatter
import lab.justonebyte.moneysubuu.utils.getCurrentMonth
import lab.justonebyte.moneysubuu.utils.getToday
import lab.justonebyte.moneysubuu.utils.yearFormatter
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun BalanceCard(
    goToPiechart:(type:Int,tab:Int,date:String)->Unit,
    modifier: Modifier = Modifier,
    currentBalance: Int ,
    incomeBalance:Int,
    expenseBalance:Int,
    collectBalaceOfDay:(day:String)->Unit,
    selectedDay:String,
    selectedMonth:String,
    selectedYear:String,
    balanceType: BalanceType,
    onMonthChoose:()->Unit
){
    val currentDay = getToday()
        val currentMonth = getCurrentMonth()
        val mContext = LocalContext.current
        val mYear by remember(selectedDay) { mutableStateOf(selectedDay.split('-')[0].toInt())}
        val mMonth by remember(selectedDay){ mutableStateOf(selectedDay.split('-')[1].toInt())}
        val mDay by remember(selectedDay) { mutableStateOf(selectedDay.split('-')[2].toInt())}

        val mDate = remember { mutableStateOf(dateFormatter(System.currentTimeMillis())) }
        val mDatePickerDialog = DatePickerDialog(
            mContext,
            { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                mDate.value = "$mYear-${if(mMonth+1>=10) mMonth+1 else "0"+(mMonth+1)}-$mDayOfMonth"
                collectBalaceOfDay(mDate.value)
            }, mYear, mMonth-1, mDay
        )



    Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Balance : ",
                    style = MaterialTheme.typography.h5
                )
                Text(
                    text = currentBalance.toString(),
                    style = MaterialTheme.typography.h6,
                    color = if(currentBalance>0) Green else Red900
                )
            }
            Row(horizontalArrangement = Arrangement.Center){
                TextButton(onClick = {
                    when(balanceType){
                        BalanceType.DAILY->mDatePickerDialog.show()
                        BalanceType.MONTHLY->onMonthChoose()
                        BalanceType.YEARLY->onMonthChoose()
                    }
                }) {
                    Text(text =  when(balanceType){
                        BalanceType.DAILY-> if(mDate.value==currentDay) "Today" else mDate.value
                        BalanceType.MONTHLY->if(selectedMonth==currentMonth) "This Month" else selectedMonth
                        BalanceType.YEARLY->selectedYear
                        else->"Total"
                    })
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row() {
                Card(
                    shape = SuBuuShapes.small,
                    modifier = modifier
                        .height(100.dp)
                        .padding(10.dp)
                        .weight(1f)
                        .clickable {
                            goToPiechart(
                                TransactionType.Income.value,
                                balanceType.value,
                                when(balanceType){
                                    BalanceType.DAILY->mDate.value
                                    BalanceType.MONTHLY->selectedMonth
                                    BalanceType.YEARLY->selectedYear
                                    else-> "Total"
                                }
                            ) },
                    elevation = 10.dp
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        Text(
                            text = "Income : ",
                            style = MaterialTheme.typography.subtitle2
                        )
                        Text(
                            color = Green,
                            text = incomeBalance.toString(),
                            style = MaterialTheme.typography.h6

                        )
                    }
                }
                Card(
                    shape = SuBuuShapes.small,
                    modifier = modifier
                        .weight(1f)
                        .height(100.dp)
                        .padding(10.dp)
                        .clickable {
                            goToPiechart(
                                TransactionType.Expense.value,
                                balanceType.value,
                                when(balanceType){
                                    BalanceType.DAILY->mDate.value
                                    BalanceType.MONTHLY->selectedMonth
                                    BalanceType.YEARLY->selectedYear
                                    else-> "Total"
                                }
                            )
                        },
                    elevation = 10.dp
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        Text(
                            text = "Expense : ",
                            style = MaterialTheme.typography.subtitle2
                        )
                        Text(
                            color = Red900,
                            text = expenseBalance.toString(),
                            style = MaterialTheme.typography.h6

                        )
                    }
                }

            }
        }

}