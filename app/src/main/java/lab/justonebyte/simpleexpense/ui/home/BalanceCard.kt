package lab.justonebyte.simpleexpense.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lab.justonebyte.simpleexpense.model.Currency
import lab.justonebyte.simpleexpense.ui.theme.SuBuuShapes
import lab.justonebyte.simpleexpense.R

@Composable
fun BalanceCard(
    modifier: Modifier = Modifier,
    currency: Currency,
    currentBalance: Int,
    incomeBalance: Int,
    expenseBalance: Int
){


    Column(
            verticalArrangement = Arrangement.Center,
        ) {

            SectionTitle(title = stringResource(id = R.string.balance),modifier = Modifier.absolutePadding(top = 15.dp, left = 10.dp, bottom = 5.dp, right = 10.dp))
            Card(
                shape = SuBuuShapes.small,
                modifier = modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(10.dp),
            ) {

                 if(currentBalance!=0){
                     Row(
                         modifier = Modifier
                             .height(120.dp)
                             .padding(10.dp),
                         horizontalArrangement = Arrangement.SpaceBetween
                     ) {
                         Column(verticalArrangement = Arrangement.Center, modifier = Modifier.weight(1f)) {
                             Text(
                                 text = stringResource(id = R.string.current_balance),
                                 modifier = Modifier
                                     .weight(1f)
                                     .wrapContentHeight(align = CenterVertically),
                             )
                             Text(
                                 text =  stringResource(id = R.string.income_balance),
                                 modifier = Modifier
                                     .weight(1f)
                                     .wrapContentHeight(align = CenterVertically),

                                 )
                             Text(
                                 text = stringResource(id = R.string.expense_balance),
                                 modifier = Modifier
                                     .weight(1f)
                                     .wrapContentHeight(align = CenterVertically),
                             )
                         }
                         Column( verticalArrangement = Arrangement.Center, modifier = Modifier.weight(1f)) {
                             Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                 Text(
                                     text = "$currentBalance",
                                 )
                                 Text(
                                     text = " " + stringResource(id = currency.name)

                                 )
                             }
                             Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {

                                 Text(
                                     text = "$incomeBalance",
                                 )
                                 Text(text =  " " + stringResource(id = currency.name))


                             }
                             Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                 Text(
                                     text = "$expenseBalance",
                                 )
                                 Text(text =  " " + stringResource(id = currency.name))

                             }
                         }
                         Spacer(modifier = Modifier.weight(1f))
                     }
                     
                 }else{
                     NoData(modifier = Modifier
                         .fillMaxWidth()
                         .height(100.dp),)
                 }
            }
        }

}