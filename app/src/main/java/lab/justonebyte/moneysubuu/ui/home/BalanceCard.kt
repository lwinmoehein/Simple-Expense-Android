package lab.justonebyte.moneysubuu.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import lab.justonebyte.moneysubuu.model.BalanceType
import lab.justonebyte.moneysubuu.model.Currency
import lab.justonebyte.moneysubuu.ui.components.ChooseTransactionTypeCard
import lab.justonebyte.moneysubuu.ui.theme.Red900
import lab.justonebyte.moneysubuu.ui.theme.SuBuuShapes
import lab.justonebyte.moneysubuu.ui.theme.negativeColor
import lab.justonebyte.moneysubuu.ui.theme.positiveColor
import lab.justonebyte.moneysubuu.R


@Composable
fun BalanceCard(
    modifier: Modifier = Modifier,
    currency: Currency,
    currentBalance: Int,
    incomeBalance:Int,
    expenseBalance:Int,
    selectedDay:String,
    selectedMonth:String,
    selectedYear:String,
    balanceType: BalanceType,
    onDatePicked:(day:String)->Unit,
    onMonthPicked:(month:String)->Unit,
    onYearPicked:(year:String)->Unit,
    onTypeChanged:(type: BalanceType)->Unit
){


    val mDate = remember { mutableStateOf(selectedDay) }

    Column(
            verticalArrangement = Arrangement.Center,
        ) {

        ChooseTransactionTypeCard(
            onDatePicked = onDatePicked,
            selectedDay =  selectedDay,
            selectedMonth = selectedMonth,
            selectedYear = selectedYear,
            balanceType = balanceType,
            onMonthPicked = onMonthPicked,
            onYearPicked = onYearPicked,
            onTypeChanged = onTypeChanged
        )

        SectionTitle(title = stringResource(id = R.string.balance),modifier = Modifier.absolutePadding(top = 15.dp))
        Card(
            shape = SuBuuShapes.small,
            modifier = modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(10.dp),
        ) {

              Row(
                modifier = Modifier
                    .height(120.dp)
                    .padding(10.dp)
              ) {
                     Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Center) {
                         Text(
                             text = stringResource(id = R.string.current_balance),
                             modifier = Modifier
                                 .weight(1f)
                                 .wrapContentHeight(align = CenterVertically),
//                             color = if(currentBalance>0) MaterialTheme.colors.primaryVariant else Red900,
                             )
                         Text(
                             text =  stringResource(id = R.string.income_balance),
                             modifier = Modifier
                                 .weight(1f)
                                 .wrapContentHeight(align = CenterVertically),
//                             color = positiveColor

                             )
                         Text(
                             text = stringResource(id = R.string.expense_balance),
                             modifier = Modifier
                                 .weight(1f)
                                 .wrapContentHeight(align = CenterVertically),
//                             color = negativeColor

                         )
                     }
                  Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Center) {
                      Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                          Text(
                              text = "$currentBalance",
                              style = MaterialTheme.typography.h6
                          )
                          Text(
                              text = " " + stringResource(id = currency.name)

                          )
                      }
                      Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {

                          Text(
                              text = "$incomeBalance",
                              style = MaterialTheme.typography.h6
                          )
                          Text(text =  " " + stringResource(id = currency.name))


                      }
                      Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                          Text(
                              text = "$expenseBalance",
                              style = MaterialTheme.typography.h6
                          )
                          Text(text =  " " + stringResource(id = currency.name))

                      }


                  }
              }
        }
        }

}