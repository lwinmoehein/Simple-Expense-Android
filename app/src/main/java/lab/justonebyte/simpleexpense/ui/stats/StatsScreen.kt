package lab.justonebyte.simpleexpense.ui.stats

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowDown
import compose.icons.feathericons.ArrowUp
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.model.BalanceType
import lab.justonebyte.simpleexpense.ui.components.AppOption
import lab.justonebyte.simpleexpense.ui.components.OptionItem
import lab.justonebyte.simpleexpense.ui.components.TransactionTypePicker
import lab.justonebyte.simpleexpense.ui.detail.CustomPieChartWithData
import lab.justonebyte.simpleexpense.ui.home.*
import lab.justonebyte.simpleexpense.utils.getCurrentDate
import lab.justonebyte.simpleexpense.utils.getCurrentMonth
import lab.justonebyte.simpleexpense.utils.getCurrentYear

sealed class BalanceTypeOption(override val name:Int, override  val value:Any) : OptionItem {
    object MONTHLY: BalanceTypeOption(R.string.monthly,BalanceType.MONTHLY)
    object YEARLY: BalanceTypeOption(R.string.yearly,BalanceType.YEARLY)
    object TOTAL: BalanceTypeOption(R.string.total,BalanceType.TOTAL)
}

@Composable
fun TransactionTypeTab(modifier: Modifier = Modifier){
    Row (
        modifier = modifier
            .fillMaxWidth()
    ){
        Row(
            Modifier.absolutePadding(top = 5.dp, bottom = 5.dp)
        ){
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = FeatherIcons.ArrowUp , contentDescription ="", tint = Color.Red )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Expense")
            }
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = FeatherIcons.ArrowDown , contentDescription ="", tint = Color.Green )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Income")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun StatsScreen(goBack:()->Unit) {
    val statsViewModel = hiltViewModel<StatsViewModel>()
    val homeViewModel = hiltViewModel<HomeViewModel>()

    val statsUiState by statsViewModel.viewModelUiState.collectAsState()
    val transactions = statsUiState.transactions

    val balanceType = mutableStateOf(statsUiState.currentBalanceType)

    val chosenDateString = when(balanceType.value){
        BalanceType.DAILY-> if(statsUiState.selectedDay== getCurrentDate()) stringResource(id = R.string.this_day) else statsUiState.selectedDay+" "+stringResource(id = R.string.day)
        BalanceType.MONTHLY->if(statsUiState.selectedMonth== getCurrentMonth()) stringResource(id = R.string.this_month) else statsUiState.selectedMonth+" "+stringResource(id = R.string.month)
        BalanceType.YEARLY->if(statsUiState.selectedYear== getCurrentYear()) stringResource(id = R.string.this_year) else statsUiState.selectedYear+" "+stringResource(id = R.string.year)
        else->stringResource(id = R.string.total) }

    val balanceTypeOptions = listOf<OptionItem>(
        BalanceTypeOption.MONTHLY,
        BalanceTypeOption.YEARLY,
        BalanceTypeOption.TOTAL
    )
    val selectedBalanceType = remember { mutableStateOf<BalanceType>(BalanceType.MONTHLY) }

    Scaffold(
        topBar =  {
           Column {
               Row(
                   Modifier
                       .fillMaxWidth()
                       .padding(10.dp),
                   horizontalArrangement = Arrangement.SpaceBetween,
                   verticalAlignment = Alignment.CenterVertically
               ) {
                   Row(
                       verticalAlignment = Alignment.CenterVertically

                   ) {
                       Text(
                           stringResource(id = R.string.charts),
                           maxLines = 1,
                           overflow = TextOverflow.Ellipsis,
                           style = MaterialTheme.typography.titleLarge
                       )
                   }

               }
               Divider()
           }
        }
    ) {
        Column(modifier = Modifier
            .padding(it)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .absolutePadding(right = 10.dp, left = 10.dp, top = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppOption(
                    label = "Select",
                    options = balanceTypeOptions,
                    onItemSelected = {
                        selectedBalanceType.value = it.value as BalanceType
                        when(selectedBalanceType.value){
                            BalanceType.YEARLY->homeViewModel.collectDailyBalance()
                            BalanceType.MONTHLY-> homeViewModel.collectMonthlyBalance()
                            BalanceType.YEARLY->homeViewModel.collectYearlyBalance()
                            else->homeViewModel.collectTotalBalance()
                        }
                    },
                    selectedOption = selectedBalanceType.value
                )
                TransactionTypePicker(
                    onDatePicked = { date ->
                        statsViewModel.collectDailyBalance(date)
                    },
                    balanceType = statsUiState.currentBalanceType,
                    onMonthPicked = { month ->
                        statsViewModel.collectMonthlyBalance(month)
                    },
                    onYearPicked = { year ->
                        statsViewModel.collectYearlyBalance(year)
                    },
                    selectedYear = statsUiState.selectedYear,
                    selectedMonth = statsUiState.selectedMonth,
                    selectedDay = statsUiState.selectedDay
                )

            }

            Spacer(modifier = Modifier.height(10.dp))
            Card(
                Modifier.fillMaxSize().padding(10.dp)
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                TransactionTypeTab()
                Spacer(modifier = Modifier.height(20.dp))
                CustomPieChartWithData(
                    currency = statsUiState.currentCurrency,
                    transactions = transactions)
            }

        }
    }
}
