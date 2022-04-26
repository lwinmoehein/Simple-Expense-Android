package lab.justonebyte.moneysubuu.ui.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import lab.justonebyte.moneysubuu.model.Transaction
import me.bytebeats.views.charts.pie.PieChart
import me.bytebeats.views.charts.pie.PieChartData
import me.bytebeats.views.charts.pie.render.SimpleSliceDrawer
import me.bytebeats.views.charts.simpleChartAnimation


@Composable
fun TransactionDetailScreen(
    modifier: Modifier = Modifier,
    transactions:List<Transaction> = emptyList(),
    openDrawer:()->Unit
){
    CustomPieChart(Modifier.width(200.dp).height(200.dp))
}

@Composable
fun CustomLineGraph() {

}
fun randomLength() = 100f
fun randomColor() = listOf(Color.Gray,Color.Green,Color.Red).random()
@Composable
fun CustomPieChart(modifier: Modifier=Modifier){
    PieChart(
        modifier = modifier,
        pieChartData = PieChartData(
            slices = listOf(
                PieChartData.Slice(
                    randomLength(),
                    randomColor()
                ),
                PieChartData.Slice(randomLength(), randomColor()),
                PieChartData.Slice(randomLength(), randomColor())
            )
        ),
        // Optional properties.
        animation = simpleChartAnimation(),
        sliceDrawer = SimpleSliceDrawer(100f)
    )
}