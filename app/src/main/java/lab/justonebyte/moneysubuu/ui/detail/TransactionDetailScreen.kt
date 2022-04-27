package lab.justonebyte.moneysubuu.ui.detail


// for a `var` variable also add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import lab.justonebyte.moneysubuu.model.Transaction
import lab.justonebyte.moneysubuu.model.TransactionType
import me.bytebeats.views.charts.pie.PieChart
import me.bytebeats.views.charts.pie.PieChartData
import me.bytebeats.views.charts.pie.render.SimpleSliceDrawer
import me.bytebeats.views.charts.simpleChartAnimation
import java.util.*

@Composable
fun TransactionDetailScreen(
    modifier: Modifier = Modifier,
    openDrawer:()->Unit
){
    val detailViewModel = hiltViewModel<TransactionDetailViewModel>()
    val detailUiState by detailViewModel.viewModelUiState.collectAsState()

    val incomeTransactions = detailUiState.transactions.filter { it.type==TransactionType.Income }

    IncomePieChart(
        incomeTransactions = incomeTransactions
    )
}

@Composable
fun CustomLineGraph() {

}
fun randomLength() = 100f
fun randomColor() = listOf(Color.Gray,Color.Green,Color.Red).random()
@Composable
fun IncomePieChart(
    modifier: Modifier=Modifier,
    incomeTransactions:List<Transaction>
){
    val groupByCategoryTransactions = incomeTransactions.groupBy { it.category }.map { it.key to it.value.sumOf { it.amount } }.sortedByDescending { it.second }
    val incomePieSlices = groupByCategoryTransactions.map { map->
        map.first to PieChartData.Slice((map.second ).toFloat(), randomColor())
    }

   Card(
       modifier = modifier.padding(20.dp).wrapContentHeight()
   ) {
      Column(
          modifier = modifier
              .padding(20.dp)
              .fillMaxWidth().wrapContentHeight(),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally
      ) {
          Row(modifier=Modifier.fillMaxWidth().height(200.dp)){
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
          LazyColumn(
              modifier = Modifier
                  .weight(1f)
                  .fillMaxWidth().wrapContentHeight(),
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
                                  modifier = Modifier.padding(10.dp).fillMaxWidth()
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
}