package lab.justonebyte.moneysubuu.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import lab.justonebyte.moneysubuu.model.TransactionType
import lab.justonebyte.moneysubuu.ui.components.DatePicker
import lab.justonebyte.moneysubuu.ui.home.BalanceType
import lab.justonebyte.moneysubuu.ui.home.MonthPicker
import lab.justonebyte.moneysubuu.utils.dateFormatter
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

    val selectedDate = remember { mutableStateOf(dateData)}
    val selectedMonthYear = remember {
        if(balanceType==BalanceType.MONTHLY || balanceType==BalanceType.YEARLY){
            mutableStateOf(dateData.split("-")[0].toInt())
        }else{
            mutableStateOf(1)
        }
    }
    val selectedMonthMonth = remember {
        if(balanceType==BalanceType.MONTHLY){
            mutableStateOf(dateData.split("-")[1].toInt())
        }else{
            mutableStateOf(1)
        }
    }

    val calculatedDate = remember(balanceType,selectedDate.value,selectedMonthMonth.value,selectedMonthYear.value){ mutableStateOf(
        when(balanceType){
            BalanceType.DAILY->selectedDate.value
            BalanceType.MONTHLY->selectedMonthYear.value.toString()+"-"+if(selectedMonthMonth.value<10) "0"+selectedMonthMonth.value.toString() else selectedMonthMonth.value.toString()
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
                detailViewModel.bindPieChartData(balanceType = balanceType, dateData = it)
            },
            onDismiss = {
                isDatePickerShown.value = false
            },
            date = calculatedDate.value,
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


