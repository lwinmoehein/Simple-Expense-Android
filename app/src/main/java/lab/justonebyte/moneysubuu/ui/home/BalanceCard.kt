package lab.justonebyte.moneysubuu.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import lab.justonebyte.moneysubuu.model.TransactionType
import lab.justonebyte.moneysubuu.ui.components.ChooseTransactionTypeCard
import lab.justonebyte.moneysubuu.ui.theme.Red900
import lab.justonebyte.moneysubuu.ui.theme.SuBuuShapes
import lab.justonebyte.moneysubuu.ui.theme.negativeColor
import lab.justonebyte.moneysubuu.ui.theme.positiveColor


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
    onMonthChoose:()->Unit,
    onTypeChanged:(type:BalanceType)->Unit
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
            onMonthChoose = onMonthChoose,
            onTypeChanged = onTypeChanged
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
                    goToPiechart(
                        TransactionType.Income.value,
                        balanceType.value,
                        when (balanceType) {
                            BalanceType.DAILY -> mDate.value
                            BalanceType.MONTHLY -> selectedMonth
                            BalanceType.YEARLY -> selectedYear
                            else -> "Total"
                        }
                    )
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
                             text = "Balance  : ",
                             modifier = Modifier.weight(1f).wrapContentHeight(align = CenterVertically),
                             color = if(currentBalance>0) MaterialTheme.colors.primaryVariant else Red900,
                             )
                         Text(
                             text = "Income  : ",
                             modifier = Modifier.weight(1f).wrapContentHeight(align = CenterVertically),
                             color = positiveColor

                             )
                         Text(
                             text = "Expense : ",
                             modifier = Modifier.weight(1f).wrapContentHeight(align = CenterVertically),
                             color = negativeColor

                         )
                     }
                  Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Center) {
                      Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                          Text(
                              text = "$currentBalance",
                              style = MaterialTheme.typography.h6,
                              fontWeight = FontWeight.ExtraBold,
                              color = if(currentBalance>0) MaterialTheme.colors.primaryVariant else Red900,
                          )
                          Text(text = " Ks",color = if(currentBalance>0) MaterialTheme.colors.primaryVariant else Red900,)
                      }
                      Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {

                          Text(
                              text = "$incomeBalance",
                              style = MaterialTheme.typography.h6,
                              color = positiveColor
                          )
                          Text(text = " Ks", color = positiveColor)


                      }
                      Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
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