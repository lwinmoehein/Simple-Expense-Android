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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import lab.justonebyte.moneysubuu.model.Transaction
import me.bytebeats.views.charts.pie.PieChart
import me.bytebeats.views.charts.pie.PieChartData
import me.bytebeats.views.charts.pie.render.SimpleSliceDrawer
import me.bytebeats.views.charts.simpleChartAnimation

@Composable
fun CustomPieChartWithData(
    modifier: Modifier = Modifier,
    transactions:List<Transaction>,

    ){
    val addedColors = mutableListOf<Color>()
    fun getRandomColor():Color{
        var randomColor =  randomColor()
        while (randomColor in addedColors){
            randomColor = randomColor()
        }
        addedColors.add(randomColor)
        return randomColor
    }
    val groupByCategoryTransactions = transactions.groupBy { it.category }.map { it.key to it.value.sumOf { it.amount } }.sortedByDescending { it.second }
    val incomePieSlices = groupByCategoryTransactions.map { map->
        map.first to PieChartData.Slice((map.second ).toFloat(),getRandomColor())
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Row(modifier= Modifier
                .fillMaxWidth()
                .height(150.dp)){
                PieChart(
                    modifier = Modifier
                        .fillMaxWidth()
                    ,
                    pieChartData = PieChartData(
                        slices = incomePieSlices.map{it.second}
                    ),
                    // Optional properties.
                    animation = simpleChartAnimation(),
                    sliceDrawer = SimpleSliceDrawer(100f)
                )
            }
            Row(horizontalArrangement = Arrangement.Center) {
                Text(text = "Total : ", style = MaterialTheme.typography.h6)
                Text(text = incomePieSlices.sumOf { it.second.value.toInt() }.toString()+" Kyats", style = MaterialTheme.typography.h6, color = MaterialTheme.colors.primary)
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .absolutePadding(top = 10.dp)
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = {
                items(incomePieSlices){
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Card(
                            modifier = Modifier.absolutePadding(top=3.dp, bottom = 3.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth()
                            ){
                                Spacer(modifier = Modifier
                                    .absolutePadding(right = 4.dp)
                                    .width(10.dp)
                                    .height(10.dp)
                                    .background(it.second.color))
                                Text(text = it.first.name+" = " + it.second.value.toInt())
                            }
                        }
                    }
                }
            }
        )
    }
}