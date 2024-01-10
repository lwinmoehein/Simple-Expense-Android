package lab.justonebyte.simpleexpense.ui.detail


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lab.justonebyte.simpleexpense.model.Currency
import lab.justonebyte.simpleexpense.model.Transaction
import lab.justonebyte.simpleexpense.ui.theme.*
import me.bytebeats.views.charts.pie.PieChart
import me.bytebeats.views.charts.pie.PieChartData
import me.bytebeats.views.charts.pie.render.SimpleSliceDrawer
import me.bytebeats.views.charts.simpleChartAnimation
import lab.justonebyte.simpleexpense.ui.home.NoData
import lab.justonebyte.simpleexpense.R

fun randomColor() = listOf(
    bar1,bar2,bar3,bar4,bar5,bar6,bar7,bar8,bar9,bar10,bar11,bar12,bar13,bar14,bar15,bar16,bar17,bar18,bar19,bar20
).random()

@Composable
fun CustomPieChartWithData(
    modifier: Modifier=Modifier,
    currency: Currency,
    transactions:List<Transaction>,

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
                    Text(
                        text = incomePieSlices.sumOf { it.second.value.toInt() }.toString()+" "+ stringResource(id = currency.name),
                        style =  MaterialTheme.typography.titleLarge
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
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .fillMaxWidth()
                                ){
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Spacer(modifier = Modifier
                                            .absolutePadding(right = 4.dp)
                                            .width(10.dp)
                                            .height(10.dp)
                                            .background(it.second.color))
                                        Text(text = it.first.name)
                                    }
                                    Text(text =  "${it.second.value.toInt()}"+" "+ stringResource(id = currency.name))
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