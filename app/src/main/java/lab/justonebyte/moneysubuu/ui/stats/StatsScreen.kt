import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import lab.justonebyte.moneysubuu.ui.detail.randomColor
import me.bytebeats.views.charts.bar.BarChart
import me.bytebeats.views.charts.bar.BarChartData
import me.bytebeats.views.charts.bar.render.bar.SimpleBarDrawer
import me.bytebeats.views.charts.bar.render.label.SimpleLabelDrawer
import me.bytebeats.views.charts.bar.render.xaxis.SimpleXAxisDrawer
import me.bytebeats.views.charts.bar.render.yaxis.SimpleYAxisDrawer
import me.bytebeats.views.charts.simpleChartAnimation


@Composable
fun StatsScreen() {
  Column(
       verticalArrangement = Arrangement.spacedBy(30.dp),
      modifier = Modifier.padding(10.dp)
  ) {
      Column(Modifier.weight(1f)) {
          Text(
              text = "What gets you more money?",
              color = MaterialTheme.colors.primary,
              style = MaterialTheme.typography.subtitle1,
              modifier = Modifier.absolutePadding(bottom = 5.dp)
          )
          Divider(modifier = Modifier.absolutePadding(bottom = 30.dp))
          CustomBarChart()
      }
      Column(Modifier.weight(1f)) {
          Text(
              text = "What costs you most?",
              color = MaterialTheme.colors.primary,
              style = MaterialTheme.typography.subtitle1,
              modifier = Modifier.absolutePadding(bottom = 5.dp)
          )
          Divider(modifier = Modifier.absolutePadding(bottom = 30.dp))
          CustomBarChart()
      }
  }
}
@Composable
fun CustomBarChart(){
    BarChart(
        barChartData = BarChartData(
            bars = listOf(
                BarChartData.Bar(
                    label = "Bar 1",
                    value = 1f,
                    color = randomColor()
                ),
                BarChartData.Bar(
                    label = "Bar 2",
                    value = 3f,
                    color = randomColor()
                ),
                BarChartData.Bar(
                    label = "Bar 3",
                    value = 100f,
                    color = randomColor()
                ),

                BarChartData.Bar(
                    label = "Bar 33",
                    value = 101f,
                    color = randomColor()
                ),
                BarChartData.Bar(
                    label = "Bar 4",
                    value = 5f,
                    color = randomColor()
                ),
            )
        ),
        // Optional properties.
        modifier = Modifier.fillMaxSize(),
        animation = simpleChartAnimation(),
        barDrawer = SimpleBarDrawer(),
        xAxisDrawer = SimpleXAxisDrawer(),
        yAxisDrawer = SimpleYAxisDrawer(),
        labelDrawer = SimpleLabelDrawer()
    )
}