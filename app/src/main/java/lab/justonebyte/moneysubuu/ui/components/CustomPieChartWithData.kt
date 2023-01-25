package lab.justonebyte.moneysubuu.ui.detail


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import lab.justonebyte.moneysubuu.model.Transaction
import lab.justonebyte.moneysubuu.ui.theme.*
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
    transactions:List<Transaction>,

){
    val groupByCategoryTransactions = transactions.groupBy { it.category }.map { it.key to it.value.sumOf { it.amount } }.sortedByDescending { it.second }
    val incomePieSlices = groupByCategoryTransactions.map { map->
        map.first to PieChartData.Slice((map.second ).toFloat(), randomColor())
    }

      Column(
          modifier = modifier
              .fillMaxWidth(),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally
      ) {
          Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
              Row(modifier= Modifier
                  .width(150.dp)
                  .height(150.dp)){
                  PieChart(
                      pieChartData = PieChartData(
                          slices = incomePieSlices.map{it.second}
                      ),
                      // Optional properties.
                      animation = simpleChartAnimation(),
                      sliceDrawer = SimpleSliceDrawer(100f)
                  )
              }
              Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.absolutePadding(left = 20.dp)) {
                  Text(text = "Total : ", style = MaterialTheme.typography.h6)
                  Text(text = incomePieSlices.sumOf { it.second.value.toInt() }.toString()+" kyats", style = MaterialTheme.typography.h6, color = MaterialTheme.colors.primary)
              }
          }
          LazyColumn(
              modifier = Modifier
                  .fillMaxWidth()
                  .absolutePadding(top = 10.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              content = {
                  items(incomePieSlices){
                      Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                          Card(
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
                                  Text(text =  "${it.second.value.toInt()} kyats", style = MaterialTheme.typography.subtitle1)
                              }
                          }
                      }
                  }
              }
          )
   }
}