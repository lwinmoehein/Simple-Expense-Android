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
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import lab.justonebyte.moneysubuu.model.Transaction
import lab.justonebyte.moneysubuu.model.TransactionType
import lab.justonebyte.moneysubuu.ui.components.DatePicker
import lab.justonebyte.moneysubuu.ui.home.BalanceType
import lab.justonebyte.moneysubuu.ui.home.HomeTab
import lab.justonebyte.moneysubuu.ui.home.MonthPicker
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
    balanceType:BalanceType = BalanceType.DAILY,
    dateData:String = dateFormatter(System.currentTimeMillis())
){
    val calendar = Calendar.getInstance()
    val detailViewModel = hiltViewModel<TransactionDetailViewModel>()
    val detailUiState by detailViewModel.viewModelUiState.collectAsState()

    val transactions = detailUiState.transactions.filter { it.type== transactionType}
    val scaffoldState = rememberScaffoldState()

    val mDate = remember(dateData) { mutableStateOf(dateData ) }
    //month picker
    val isMonthPickerShown = remember { mutableStateOf(false)}
    val isDatePickerShown = remember { mutableStateOf(false)}

    val selectedDate = remember { mutableStateOf(detailUiState.selectedDay)}
    val selectedMonthYear = remember { mutableStateOf(calendar.get(Calendar.YEAR))}
    val selectedMonthMonth = remember { mutableStateOf(calendar.get(Calendar.MONTH)+1)}

    val calculatedDate = remember(balanceType,selectedDate.value,selectedMonthMonth.value,selectedMonthMonth){ mutableStateOf(
        when(balanceType){
            BalanceType.DAILY->selectedDate.value
            BalanceType.MONTHLY->selectedMonthYear.value.toString()+"-"+selectedMonthMonth.value.toString()
            BalanceType.YEARLY->selectedMonthYear.value.toString()
            else->"Total"
        }
    )}


    LaunchedEffect(key1 = mDate.value, block = {
            when(balanceType){
                BalanceType.DAILY->detailViewModel.bindPieChartData(balanceType = balanceType,calculatedDate.value)
                BalanceType.MONTHLY->detailViewModel.bindPieChartData(balanceType = balanceType,calculatedDate.value)
                BalanceType.YEARLY-> detailViewModel.bindPieChartData(balanceType = balanceType,calculatedDate.value)
                else->detailViewModel.bindPieChartData(balanceType = balanceType, dateData = "")
            }
    } )


    if(isDatePickerShown.value){
        DatePicker(
            onDateChosen = {
                isDatePickerShown.value = false
                selectedDate.value = it
                detailViewModel.bindPieChartData(balanceType = balanceType, dateData = calculatedDate.value)
            },
            onDismiss = {
                isDatePickerShown.value = false
            },
            date = selectedDate.value,
        )
    }

    if(isMonthPickerShown.value){
        Dialog(onDismissRequest = { isMonthPickerShown.value=false }) {
            MonthPicker(
                selectedMonth = selectedMonthMonth.value,
                selectedYear =selectedMonthYear.value ,
                onYearSelected ={
                    selectedMonthYear.value=it
                } ,
                onMonthSelected = {
                    selectedMonthMonth.value=it

                },
                onConfirmPicker = {
                    isMonthPickerShown.value =false
                    if(balanceType==BalanceType.MONTHLY){
                        detailViewModel.bindPieChartData(balanceType = balanceType,calculatedDate.value)
                    }else{
                        detailViewModel.bindPieChartData(balanceType = balanceType,calculatedDate.value)
                    }
                },
                isMonthPicker = balanceType==BalanceType.MONTHLY
            )
        }
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
                       Text(calculatedDate.value)
                   }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                       when(balanceType){
                          BalanceType.DAILY-> TextButton(onClick = {
                                              isDatePickerShown.value = true
                                          }) {
                                              Text(text = calculatedDate.value)
                                          }
                           BalanceType.MONTHLY-> TextButton(onClick = { isMonthPickerShown.value = true}) {
                               Text(text = "For "+calculatedDate.value)
                           }
                           BalanceType.YEARLY-> TextButton(onClick = { isMonthPickerShown.value=true }) {
                               Text(text = "For "+calculatedDate.value)
                           }
                           else-> Text(text = "")
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