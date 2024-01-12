package lab.justonebyte.simpleexpense.ui.detail


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.model.Currency
import lab.justonebyte.simpleexpense.model.Transaction
import lab.justonebyte.simpleexpense.model.TransactionType
import lab.justonebyte.simpleexpense.ui.components.FormattedCurrency
import lab.justonebyte.simpleexpense.ui.home.NoData
import lab.justonebyte.simpleexpense.ui.theme.bar1
import lab.justonebyte.simpleexpense.ui.theme.bar10
import lab.justonebyte.simpleexpense.ui.theme.bar11
import lab.justonebyte.simpleexpense.ui.theme.bar12
import lab.justonebyte.simpleexpense.ui.theme.bar13
import lab.justonebyte.simpleexpense.ui.theme.bar14
import lab.justonebyte.simpleexpense.ui.theme.bar15
import lab.justonebyte.simpleexpense.ui.theme.bar16
import lab.justonebyte.simpleexpense.ui.theme.bar17
import lab.justonebyte.simpleexpense.ui.theme.bar18
import lab.justonebyte.simpleexpense.ui.theme.bar19
import lab.justonebyte.simpleexpense.ui.theme.bar2
import lab.justonebyte.simpleexpense.ui.theme.bar20
import lab.justonebyte.simpleexpense.ui.theme.bar3
import lab.justonebyte.simpleexpense.ui.theme.bar4
import lab.justonebyte.simpleexpense.ui.theme.bar5
import lab.justonebyte.simpleexpense.ui.theme.bar6
import lab.justonebyte.simpleexpense.ui.theme.bar7
import lab.justonebyte.simpleexpense.ui.theme.bar8
import lab.justonebyte.simpleexpense.ui.theme.bar9
import me.bytebeats.views.charts.pie.PieChart
import me.bytebeats.views.charts.pie.PieChartData
import me.bytebeats.views.charts.pie.render.SimpleSliceDrawer
import me.bytebeats.views.charts.simpleChartAnimation

fun randomColor() = listOf(
    bar1,bar2,bar3,bar4,bar5,bar6,bar7,bar8,bar9,bar10,bar11,bar12,bar13,bar14,bar15,bar16,bar17,bar18,bar19,bar20
).random()

@Composable
fun CustomPieChartWithData(
    modifier: Modifier=Modifier,
    currency: Currency,
    transactions:List<Transaction>,
    transactionType: TransactionType
){
    val groupByCategoryTransactions = transactions.groupBy { it.category }.map { it.key to it.value.sumOf { it.amount } }.sortedByDescending { it.second }
    val incomePieSlices = groupByCategoryTransactions.map { map->
        map.first to PieChartData.Slice((map.second ).toFloat(), randomColor())
    }

    if(incomePieSlices.isNotEmpty()){
        Column(
            modifier = modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(modifier= Modifier
                    .weight(1f)
                    .width(140.dp)
                    .height(140.dp)){
                    PieChart(
                        pieChartData = PieChartData(
                            slices = incomePieSlices.map{it.second}
                        ),
                        // Optional properties.
                        animation = simpleChartAnimation(),
                        sliceDrawer = SimpleSliceDrawer(50f)
                    )
                }
                Column(verticalArrangement = Arrangement.Center, modifier = Modifier
                    .weight(1f)
                    .absolutePadding(left = 20.dp)) {
                    Text(
                        text = stringResource(R.string.total),
                        style = MaterialTheme.typography.titleSmall
                    )
                    FormattedCurrency(
                         amount =incomePieSlices.sumOf { it.second.value.toLong() },
                        color =if(transactionType==TransactionType.Income) MaterialTheme.colorScheme.primary  else Color.Red,
                        currencyCode = if(currency==Currency.Kyat) stringResource(id = R.string.kyat) else stringResource(R.string.dollar)
                    )
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .absolutePadding(top = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    items(incomePieSlices){
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            Row(
                                modifier = Modifier.absolutePadding(top=3.dp, bottom = 3.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .fillMaxWidth()
                                ){
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Spacer(modifier = Modifier
                                            .absolutePadding(right = 4.dp)
                                            .width(10.dp)
                                            .height(10.dp)
                                            .background(it.second.color))
                                        Text(text = it.first.name)
                                    }
                                    FormattedCurrency(
                                        modifier = Modifier.weight(1f),
                                        amount = it.second.value.toLong(),
                                        color =if(transactionType==TransactionType.Income) MaterialTheme.colorScheme.primary  else Color.Red,
                                        currencyCode = if(currency==Currency.Kyat) stringResource(id = R.string.kyat) else stringResource(
                                            id = R.string.dollar
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            )
        }
    }else{
        NoData(modifier = Modifier.fillMaxSize())
    }
}