import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import lab.justonebyte.moneysubuu.model.Transaction
import lab.justonebyte.moneysubuu.model.TransactionType
import lab.justonebyte.moneysubuu.ui.components.ChooseTransactionTypeCard
import lab.justonebyte.moneysubuu.ui.detail.randomColor
import lab.justonebyte.moneysubuu.ui.home.BalanceType
import lab.justonebyte.moneysubuu.ui.home.NoData
import lab.justonebyte.moneysubuu.ui.home.SectionTitle
import lab.justonebyte.moneysubuu.ui.stats.StatsViewModel
import lab.justonebyte.moneysubuu.utils.getCurrentDate
import lab.justonebyte.moneysubuu.utils.getCurrentMonth
import lab.justonebyte.moneysubuu.utils.getCurrentYear
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

    val balanceType = remember{ mutableStateOf(BalanceType.MONTHLY) }

    val chosenDateString = when(balanceType.value){
        BalanceType.DAILY-> if(statsUiState.selectedDay== getCurrentDate()) "Today" else statsUiState.selectedDay
        BalanceType.MONTHLY->if(statsUiState.selectedMonth== getCurrentMonth()) "This month" else statsUiState.selectedMonth
        BalanceType.YEARLY->if(statsUiState.selectedYear== getCurrentYear()) "This year" else statsUiState.selectedYear
        else->"Total" }


    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
        }
    ) {
        LazyColumn(modifier = Modifier
            .padding(10.dp)
            .padding(it)) {

            item {
                ChooseTransactionTypeCard(
                    modifier = Modifier.absolutePadding(bottom = 30.dp),
                    onDatePicked = { date ->
                       statsViewModel.collectDailyBalance(date)
                    },
                    balanceType =  balanceType.value,
                    onMonthPicked = { month->
                         statsViewModel.collectMonthlyBalance(month)
                    },
                    onYearPicked = { year->
                        statsViewModel.collectYearlyBalance(year)
                    },
                    onTypeChanged = { type->
                        balanceType.value = type
                        when(type){
                            BalanceType.DAILY->statsViewModel.collectDailyBalance()
                            BalanceType.MONTHLY->statsViewModel.collectMonthlyBalance()
                            BalanceType.YEARLY->statsViewModel.collectYearlyBalance()
                            else->statsViewModel.collectTotalBalance()
                        }
                    },
                    selectedYear = statsUiState.selectedYear,
                    selectedMonth = statsUiState.selectedMonth,
                    selectedDay = statsUiState.selectedDay
                )
            }

            item {
                Column(
                    Modifier.absolutePadding(bottom = 20.dp)
                ) {
                    Row(
                    ) {
                            SectionTitle(title = "Your income for $chosenDateString")
                    }
                    Divider(modifier = Modifier.absolutePadding(bottom = 1.dp))
                    CustomBarChart(transactions.filter { it.type==TransactionType.Income })
                }
            }

            item {
                Column(
                    Modifier.absolutePadding(bottom = 20.dp)
                ) {
                    Row() {
                            SectionTitle(title = "Your expense for $chosenDateString")
                    }
                    Divider(modifier = Modifier.absolutePadding(bottom = 1.dp))
                    CustomBarChart(transactions.filter { it.type==TransactionType.Expense })
                }
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
                .height(300.dp)
                .absolutePadding(top = 20.dp, left = 40.dp, right = 40.dp),
            animation = simpleChartAnimation(),
            barDrawer = SimpleBarDrawer(),
            xAxisDrawer = SimpleXAxisDrawer(
                axisLineThickness = 2.dp,
                axisLineColor = MaterialTheme.colors.onSurface
            ),
            yAxisDrawer = SimpleYAxisDrawer(
                axisLineThickness = 2.dp,
                axisLineColor = MaterialTheme.colors.onSurface,
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
    }else{
        NoData(modifier = Modifier.defaultMinSize(minHeight = 200.dp),"No Data.")
    }
}