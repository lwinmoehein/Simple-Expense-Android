package lab.justonebyte.moneysubuu.ui.home

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import lab.justonebyte.moneysubuu.model.TransactionType
import lab.justonebyte.moneysubuu.ui.theme.Red900
import lab.justonebyte.moneysubuu.ui.theme.SuBuuShapes
import lab.justonebyte.moneysubuu.ui.theme.negativeColor
import lab.justonebyte.moneysubuu.ui.theme.positiveColor
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
    onMonthChoose: () -> Unit
){
    val currentDay = getToday()
    val currentMonth = getCurrentMonth()
    val mContext = LocalContext.current
    val mYear by remember(selectedDay) { mutableStateOf(selectedDay.split('-')[0].toInt())}
    val mMonth by remember(selectedDay){ mutableStateOf(selectedDay.split('-')[1].toInt())}
    val mDay by remember(selectedDay) { mutableStateOf(selectedDay.split('-')[2].toInt())}

    val mDate = remember { mutableStateOf(selectedDay) }
    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate.value = "$mYear-${if(mMonth+1>=10) mMonth+1 else "0"+(mMonth+1)}-${if(mDayOfMonth<10) "0"+mDayOfMonth else mDayOfMonth}"
            collectBalaceOfDay(mDate.value)
        }, mYear, mMonth-1, mDay
    )

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
            modifier=Modifier.padding(10.dp)
        ){
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
            ) {
                Text(text = "Show Data By : ",modifier=Modifier.absolutePadding(bottom = 5.dp))
                Text(text = "Select Day : ")
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
            ) {
                val options = listOf(OptionItem("Daily",1),OptionItem("Monthly",2),OptionItem("Yearly",3))

                AppOption(
                    modifier = Modifier
                        .fillMaxWidth()
                        .absolutePadding(bottom = 5.dp),
                    label = "Select Day :",
                    options = options,
                    onItemSelected = {}
                )


                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            when (balanceType) {
                                BalanceType.DAILY -> mDatePickerDialog.show()
                                BalanceType.MONTHLY -> onMonthChoose()
                                else -> onMonthChoose()
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


    val mDate = remember { mutableStateOf(selectedDay) }

    Column(
            verticalArrangement = Arrangement.Center,
        ) {

        ChooseTransactionTypeCard(
            collectBalaceOfDay = collectBalaceOfDay,
            selectedDay =  selectedDay,
            selectedMonth = selectedMonth,
            selectedYear = selectedYear,
            balanceType = balanceType,
            onMonthChoose = onMonthChoose
        )

        SectionTitle(title = "Balances",modifier = Modifier.absolutePadding(top = 15.dp))
        Card(
            shape = SuBuuShapes.small,
            elevation = 2.dp,
            modifier = modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(10.dp)
                .clickable {
//                    goToPiechart(
//                        TransactionType.Income.value,
//                        balanceType.value,
//                        when (balanceType) {
//                            BalanceType.DAILY -> mDate.value
//                            BalanceType.MONTHLY -> selectedMonth
//                            BalanceType.YEARLY -> selectedYear
//                            else -> "Total"
//                        }
//                    )
                },
//            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.primary
        ) {

              Row(
                modifier = Modifier
                    .height(120.dp)
                    .padding(10.dp)
              ) {
                     Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Center) {
                         Text(
                             text = "Balance : ",
                             modifier = Modifier.weight(1f),
                             color = if(currentBalance>0) MaterialTheme.colors.primaryVariant else Red900,
                             )
                         Text(
                             text = "Income  : ",
                             modifier = Modifier.weight(1f),
                             color = positiveColor

                             )
                         Text(
                             text = "Expense : ",
                             modifier = Modifier.weight(1f),
                             color = negativeColor

                         )
                     }
                  Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Center) {
                      Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                          Text(
                              text = "$currentBalance",
                              style = MaterialTheme.typography.h6,
                              fontWeight = FontWeight.ExtraBold,
                              color = if(currentBalance>0) MaterialTheme.colors.primaryVariant else Red900,
                          )
                          Text(text = " Ks",color = if(currentBalance>0) MaterialTheme.colors.primaryVariant else Red900,)
                      }
                      Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {

                          Text(
                              text = "$incomeBalance",
                              style = MaterialTheme.typography.h6,
                              color = positiveColor
                          )
                          Text(text = " Ks", color = positiveColor)


                      }
                      Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                          Text(
                              text = "$expenseBalance",
                              style = MaterialTheme.typography.h6,
                              color = negativeColor
                          )
                          Text(text = " Ks", color = negativeColor)

                      }


                  }
              }
        }
        }

}