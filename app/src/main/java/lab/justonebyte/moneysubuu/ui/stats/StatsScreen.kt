import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import lab.justonebyte.moneysubuu.model.BalanceType
import lab.justonebyte.moneysubuu.model.Transaction
import lab.justonebyte.moneysubuu.model.TransactionType
import lab.justonebyte.moneysubuu.ui.components.AppOption
import lab.justonebyte.moneysubuu.ui.components.ChooseTransactionTypeCard
import lab.justonebyte.moneysubuu.ui.components.OptionItem
import lab.justonebyte.moneysubuu.ui.detail.CustomPieChartWithData
import lab.justonebyte.moneysubuu.ui.detail.randomColor
import lab.justonebyte.moneysubuu.ui.home.*
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

    val balanceType = remember{ mutableStateOf<BalanceType>(BalanceType.MONTHLY) }

    val chosenDateString = when(balanceType.value){
        BalanceType.DAILY-> if(statsUiState.selectedDay== getCurrentDate()) "Today" else statsUiState.selectedDay
        BalanceType.MONTHLY->if(statsUiState.selectedMonth== getCurrentMonth()) "This month" else statsUiState.selectedMonth
        BalanceType.YEARLY->if(statsUiState.selectedYear== getCurrentYear()) "This year" else statsUiState.selectedYear
        else->"Total" }

    val transactionTypeOptions = listOf<OptionItem>(OptionItem("Expense",TransactionType.Expense),OptionItem("Income",TransactionType.Income))
    val selectedTransactionType = remember { mutableStateOf<TransactionType>(TransactionType.Expense) }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
        }
    ) {
        Column(modifier = Modifier
            .padding(10.dp)
            .padding(it)) {

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

                Column(
                    Modifier.absolutePadding(bottom = 20.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().absolutePadding(bottom = 20.dp)
                    ) {
                            SectionTitle(title = "Your ${if(selectedTransactionType.value==TransactionType.Expense) transactionTypeOptions[0].name else transactionTypeOptions[1].name} for $chosenDateString")
                            AppOption(
                                modifier = Modifier
                                    .defaultMinSize(minWidth = 100.dp).absolutePadding(top = 10.dp, bottom = 5.dp, right = 10.dp),
                                label = "Select",
                                options = transactionTypeOptions,
                                onItemSelected = {
                                   selectedTransactionType.value = it.value as TransactionType
                                },
                                selectedOption = when(selectedTransactionType.value){
                                    TransactionType.Expense->transactionTypeOptions[0]
                                    else->transactionTypeOptions[1]
                                }
                            )
                    }
                    CustomPieChartWithData(modifier =Modifier.fillMaxHeight() , transactions = transactions.filter { it.type==(if(selectedTransactionType.value==TransactionType.Expense) TransactionType.Expense else TransactionType.Income) })
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
                .height(250.dp)
                .absolutePadding(top = 20.dp, left = 20.dp, right = 20.dp),
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