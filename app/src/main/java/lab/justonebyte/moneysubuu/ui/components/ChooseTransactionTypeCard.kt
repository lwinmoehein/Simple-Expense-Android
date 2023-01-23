package lab.justonebyte.moneysubuu.ui.components

import android.app.DatePickerDialog
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
import androidx.compose.ui.unit.dp
import lab.justonebyte.moneysubuu.ui.home.AppOption
import lab.justonebyte.moneysubuu.ui.home.BalanceType
import lab.justonebyte.moneysubuu.ui.home.OptionItem
import lab.justonebyte.moneysubuu.ui.home.SectionTitle
import lab.justonebyte.moneysubuu.ui.theme.SuBuuShapes
import lab.justonebyte.moneysubuu.utils.getCurrentMonth
import lab.justonebyte.moneysubuu.utils.getToday

@Composable
fun ChooseTransactionTypeCard(
    modifier: Modifier = Modifier,
    collectBalaceOfDay: (day: String) -> Unit,
    selectedDay: String,
    selectedMonth: String,
    selectedYear: String,
    balanceType: BalanceType,
    onMonthChoose: () -> Unit,
    onTypeChanged: (type: BalanceType) -> Unit
){
    val currentDay = getToday()
    val currentMonth = getCurrentMonth()
    val mContext = LocalContext.current
    val mYear by remember(selectedDay) { mutableStateOf(selectedDay.split('-')[0].toInt()) }
    val mMonth by remember(selectedDay){ mutableStateOf(selectedDay.split('-')[1].toInt()) }
    val mDay by remember(selectedDay) { mutableStateOf(selectedDay.split('-')[2].toInt()) }

    val mDate = remember { mutableStateOf(selectedDay) }
    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate.value = "$mYear-${if(mMonth+1>=10) mMonth+1 else "0"+(mMonth+1)}-${if(mDayOfMonth<10) "0"+mDayOfMonth else mDayOfMonth}"
            collectBalaceOfDay(mDate.value)
        }, mYear, mMonth-1, mDay
    )
    val balanceTypeOptions = listOf(
        OptionItem("Daily", BalanceType.DAILY),
        OptionItem("Monthly", BalanceType.MONTHLY),
        OptionItem("Yearly", BalanceType.YEARLY),
        OptionItem("Total", BalanceType.TOTAL)
    )
    val currentBalanceType = remember {
        mutableStateOf(BalanceType.MONTHLY)
    }

    SectionTitle(title = "Choose Type")

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
            ) {
                Text(text = "Show Data By : ",modifier= Modifier.absolutePadding(bottom = 5.dp))
                Text(text = "Select Day : ")
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
            ) {

                AppOption(
                    modifier = Modifier
                        .fillMaxWidth()
                        .absolutePadding(bottom = 5.dp),
                    label = "Select Day :",
                    options = balanceTypeOptions,
                    onItemSelected = {
                        currentBalanceType.value = it.value as BalanceType
                        onTypeChanged(currentBalanceType.value)
                    },
                    selectedOption = when(balanceType){
                        BalanceType.DAILY->balanceTypeOptions[0]
                        BalanceType.MONTHLY->balanceTypeOptions[1]
                        BalanceType.YEARLY->balanceTypeOptions[2]
                        else->balanceTypeOptions[3]
                    }
                )


                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            when (balanceType) {
                                BalanceType.DAILY -> mDatePickerDialog.show()
                                BalanceType.MONTHLY -> onMonthChoose()
                                BalanceType.YEARLY -> onMonthChoose()
                                else->{}
                            }
                        },
                    text =  when(balanceType){
                        BalanceType.DAILY-> if(mDate.value==currentDay) "Today" else ""+mDate.value
                        BalanceType.MONTHLY->if(selectedMonth==currentMonth) "This Month" else selectedMonth
                        BalanceType.YEARLY->" $selectedYear"
                        else->"Total" }
                )
            }
        }
    }
}