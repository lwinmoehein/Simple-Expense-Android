import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import lab.justonebyte.moneysubuu.model.Transaction
import lab.justonebyte.moneysubuu.model.TransactionCategory
import lab.justonebyte.moneysubuu.model.TransactionType
import lab.justonebyte.moneysubuu.ui.detail.CustomPieChartWithData
import lab.justonebyte.moneysubuu.ui.detail.TransactionDetailViewModel
import lab.justonebyte.moneysubuu.ui.detail.randomColor
import lab.justonebyte.moneysubuu.ui.home.HomeTab
import lab.justonebyte.moneysubuu.ui.stats.StatsUiState
import lab.justonebyte.moneysubuu.ui.stats.StatsViewModel
import me.bytebeats.views.charts.AxisLabelFormatter
import me.bytebeats.views.charts.LabelFormatter
import me.bytebeats.views.charts.bar.BarChart
import me.bytebeats.views.charts.bar.BarChartData
import me.bytebeats.views.charts.bar.render.bar.SimpleBarDrawer
import me.bytebeats.views.charts.bar.render.label.SimpleLabelDrawer
import me.bytebeats.views.charts.bar.render.xaxis.SimpleXAxisDrawer
import me.bytebeats.views.charts.bar.render.yaxis.SimpleYAxisDrawer
import me.bytebeats.views.charts.simpleChartAnimation


@Composable
fun StatsScreen(goBack:()->Unit) {
    val scaffoldState = rememberScaffoldState()
    val statsViewModel = hiltViewModel<StatsViewModel>()
    val statsUiState by statsViewModel.viewModelUiState.collectAsState()

    val transactions = statsUiState.transactions
    val mostCostCategory = transactions
                                .filter { it.type==TransactionType.Expense }
                                .groupBy { it.category }
                                .map{it.key to it.value
                                .sumOf { it.amount }}
                                .maxByOrNull { it.second }
    val mostIncomeCategory = transactions
                                .filter { it.type==TransactionType.Income }
                                .groupBy { it.category }
                                .map{it.key to it.value
                                .sumOf { it.amount }}
                                .maxByOrNull { it.second }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .absolutePadding(right = 20.dp, left = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

            }
        }
    ) {
        Column(modifier = Modifier
            .padding(10.dp)
            .padding(it)) {
            Column(Modifier.weight(1f)) {
                mostIncomeCategory?.first?.name?.let { mostIncomeCategory ->
                   Row() {
                       Text(text = mostIncomeCategory, color = Color.Green, fontWeight = FontWeight.ExtraBold)
                       Text(
                           text=" category gets you most money.",
                           style = MaterialTheme.typography.subtitle1,
                           modifier = Modifier.absolutePadding(bottom = 5.dp)
                       )
                   }
                }

                Divider(modifier = Modifier.absolutePadding(bottom = 10.dp))
                CustomBarChart(transactions.filter { it.type==TransactionType.Income })
            }
            Column(Modifier.weight(1f)) {
                mostCostCategory?.first?.name?.let { mostCostCategory ->
                    Row() {
                        Text(text = mostCostCategory, color = Color.Red)
                        Text(
                            text=" category costs you most money.",
                            style = MaterialTheme.typography.subtitle1,
                            modifier = Modifier.absolutePadding(bottom = 5.dp)
                        )
                    }
                }
                Divider(modifier = Modifier.absolutePadding(bottom = 10.dp))
                CustomBarChart(transactions.filter { it.type==TransactionType.Expense })
            }
        }
    }
}
@Composable
fun CustomBarChart(transactions:List<Transaction>){

    val bars = transactions
            .groupBy { it.category }
            .map{it.key to it.value
            .sumOf { it.amount }}
            .sortedByDescending { it.second }
            .map { BarChartData.Bar(
            label = it.first.name,
            value = it.second.toFloat(),
            color = randomColor()
    ) }

    if(bars.size>0){
        BarChart(
            barChartData = BarChartData(
                bars = bars
            ),
            // Optional properties.
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            animation = simpleChartAnimation(),
            barDrawer = SimpleBarDrawer(),
            xAxisDrawer = SimpleXAxisDrawer(
                axisLineThickness = 3.dp,
                axisLineColor = MaterialTheme.colors.primary
            ),
            yAxisDrawer = SimpleYAxisDrawer(
                axisLineThickness = 3.dp,
                axisLineColor = MaterialTheme.colors.primary,
                drawLabelEvery = 1000,
                labelValueFormatter = {
                    val yLabel = (it.toInt()/1000)*1000
                 return@SimpleYAxisDrawer yLabel.toString()
                }
            ),
            labelDrawer = SimpleLabelDrawer(
               labelTextColor = MaterialTheme.colors.primary,
                drawLocation = SimpleLabelDrawer.DrawLocation.Outside
            )
        )
    }
}