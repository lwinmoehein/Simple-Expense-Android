package lab.justonebyte.moneysubuu.ui.detail


// for a `var` variable also add

import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import lab.justonebyte.moneysubuu.model.Transaction
import lab.justonebyte.moneysubuu.model.TransactionType
import lab.justonebyte.moneysubuu.ui.home.HomeTab
import lab.justonebyte.moneysubuu.utils.dateFormatter
import lab.justonebyte.moneysubuu.utils.monthFormatter
import lab.justonebyte.moneysubuu.utils.yearFormatter
import me.bytebeats.views.charts.pie.PieChart
import me.bytebeats.views.charts.pie.PieChartData
import me.bytebeats.views.charts.pie.render.SimpleSliceDrawer
import me.bytebeats.views.charts.simpleChartAnimation
import java.util.*

@Composable
fun TransactionDetailScreen(
    modifier: Modifier = Modifier,
    goBack:()->Unit,
    transactionType:TransactionType = TransactionType.Income,
    tabType:HomeTab = HomeTab.Daily,
    dateData:String = dateFormatter(System.currentTimeMillis())
){
    val detailViewModel = hiltViewModel<TransactionDetailViewModel>()
    val detailUiState by detailViewModel.viewModelUiState.collectAsState()

    val transactions = detailUiState.transactions.filter { it.type== transactionType}
    val scaffoldState = rememberScaffoldState()

    //date picker dialog
    val mContext = LocalContext.current
    val mCalendar = Calendar.getInstance()
    mCalendar.time = Date()
    val mYear: Int = remember(dateData) {
        if(tabType==HomeTab.Daily) dateData.split('-')[0].toInt() else mCalendar.get(Calendar.YEAR)
    }
    val mMonth: Int = remember(dateData){
        if(tabType==HomeTab.Daily) dateData.split('-')[1].toInt() else mCalendar.get(Calendar.MONTH)
    }
    val mDay: Int = remember(dateData){
        if(tabType==HomeTab.Daily) dateData.split('-')[2].toInt() else mCalendar.get(Calendar.DAY_OF_MONTH)
    }
    val mDate = remember(dateData) { mutableStateOf(dateData ) }

    LaunchedEffect(key1 = mDate.value, block = {
        Log.i("mdate",mDate.value)
            detailViewModel.bindPieChartData(HomeTab.Daily,mDate.value)
    } )


    val mDatePickerDialog = remember(dateData){
        Log.i("datepicker","initializing")
        DatePickerDialog(
            mContext,
            { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                mDate.value =
                    "$mYear-${if (mMonth + 1 >= 10) mMonth + 1 else "0" + (mMonth + 1)}-$mDayOfMonth"

            }, mYear, mMonth, mDay
        )
    }



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
                   Row(verticalAlignment = Alignment.CenterVertically) {
                       IconButton(onClick = {
                           goBack()
                       }) {
                           Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back menu")
                       }
                       when(tabType){
                           HomeTab.Daily-> Text(text = (if(dateData== dateFormatter(System.currentTimeMillis())) "Today" else dateData)+if(transactionType==TransactionType.Income) " Income" else " Spending")
                           HomeTab.Monthly-> Text(text = (if(dateData== monthFormatter(System.currentTimeMillis())) "This Month" else dateData)+if(transactionType==TransactionType.Income) " Income" else " Spending")
                           HomeTab.Yearly-> Text(text = (if(dateData== yearFormatter(System.currentTimeMillis())) "This Year" else dateData)+if(transactionType==TransactionType.Income) " Income" else " Spending")
                           else->Text(if(transactionType==TransactionType.Income) "Total Income" else "Total Spending")
                       }
                   }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                       if(tabType==HomeTab.Daily){
                           TextButton(onClick = {
                               mDatePickerDialog.show()
                           }) {
                               Text(text = mDate.value)
                           }
                       }
                    }
                }
        }
    ) {
        CustomPieChartWithData(modifier = Modifier.padding(it),transactions = transactions)
    }
}

@Composable
fun CustomLineGraph() {

}
fun randomLength() = 100f
fun randomColor() = listOf(Color.Gray,Color.Green,Color.Red).random()
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