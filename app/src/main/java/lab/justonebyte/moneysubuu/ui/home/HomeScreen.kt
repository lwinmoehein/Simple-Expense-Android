package lab.justonebyte.moneysubuu.ui.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import lab.justonebyte.moneysubuu.model.Transaction
import lab.justonebyte.moneysubuu.model.TransactionCategory
import lab.justonebyte.moneysubuu.ui.components.SnackBarType
import lab.justonebyte.moneysubuu.ui.components.SuBuuSnackBar
import lab.justonebyte.moneysubuu.ui.components.SuBuuSnackBarHost
import lab.justonebyte.moneysubuu.ui.theme.negativeColor
import lab.justonebyte.moneysubuu.ui.theme.positiveColor
import java.util.*


@OptIn(ExperimentalMaterialApi::class, com.google.accompanist.pager.ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
    goToPieChartDetail:(type:Int,tab:Int,date:String)->Unit,
){
    val calendar = Calendar.getInstance()
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val homeUiState by homeViewModel.viewModelUiState.collectAsState()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()
    val balanceType = remember{ mutableStateOf(BalanceType.DAILY)}

    val isMonthPickerShown = remember { mutableStateOf(false)}
    val selectedMonthYear = remember { mutableStateOf(calendar.get(Calendar.YEAR))}
    val selectedMonthMonth = remember { mutableStateOf(calendar.get(Calendar.MONTH)+1)}
    val currentTransaction = remember {
        mutableStateOf<Transaction?>(null)
    }
    val currentType:MutableState<Int> = remember(currentTransaction) { mutableStateOf(
        currentTransaction.value?.type?.value ?: 1) }

    val isChooseAddTransactionTypeOpen =  remember { mutableStateOf(false)}


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
                        if(balanceType.value==BalanceType.MONTHLY){
                            homeViewModel.collectMonthlyBalance("${selectedMonthYear.value}-${if(selectedMonthMonth.value<10) "0"+selectedMonthMonth.value else selectedMonthMonth.value}")
                        }else{
                            homeViewModel.collectYearlyBalance(selectedMonthYear.value.toString())
                        }
                    },
                    isMonthPicker = balanceType.value==BalanceType.MONTHLY
            )
        }
    }

    BottomSheetScaffold(
        floatingActionButton = {
            if(!bottomSheetScaffoldState.bottomSheetState.isAnimationRunning && bottomSheetScaffoldState.bottomSheetState.isCollapsed){
                Box(
                    modifier = Modifier
                        .absolutePadding(bottom = 100.dp, left = 30.dp)
                       ) {
                  TextButton(
                      onClick = {
                          coroutineScope.launch {
                              bottomSheetScaffoldState.bottomSheetState.expand()
                          }
                          isChooseAddTransactionTypeOpen.value = true
                      },
                      modifier = Modifier
                          .fillMaxWidth()
                          .clip(RoundedCornerShape(8.dp))
                          .padding(0.dp)
                          .background(MaterialTheme.colors.primary)
                  ) {
                      Text(text = "Add New Record", style = MaterialTheme.typography.button, color = MaterialTheme.colors.onPrimary)
                      Icon(
                          modifier = Modifier
                              .width(30.dp)
                              .height(30.dp)
                          ,
                          imageVector = Icons.Filled.Add, contentDescription = "add transaction",
                          tint = MaterialTheme.colors.onPrimary
                      )
                  }
                }

            }
        },
        scaffoldState = bottomSheetScaffoldState,
        snackbarHost = { SuBuuSnackBarHost(hostState = it) },
        sheetElevation = 8.dp,
        sheetShape = RoundedCornerShape(
            bottomStart = 0.dp,
            bottomEnd = 0.dp,
            topStart = 12.dp,
            topEnd = 12.dp
        ),

        sheetContent = {
          if(isChooseAddTransactionTypeOpen.value){
              Column(
                  modifier= Modifier
                      .height(200.dp)
                      .padding(20.dp),
                  horizontalAlignment = Alignment.CenterHorizontally,
                  verticalArrangement = Arrangement.Center
              ) {
                  TextButton(
                      modifier = Modifier
                          .fillMaxWidth()
                          .clip(RoundedCornerShape(5.dp))
                          .padding(0.dp)
                          .background(positiveColor)
                          ,
                      onClick = {
                          currentType.value = 1
                          isChooseAddTransactionTypeOpen.value = false
                      }
                  ) {
                      Text(text = "Add Income",color=MaterialTheme.colors.onPrimary)
                  }
                  Divider(Modifier.height(20.dp), color = Color.Transparent)
                  TextButton(
                      modifier = Modifier
                          .fillMaxWidth()
                          .clip(RoundedCornerShape(5.dp))
                          .padding(0.dp)
                          .background(negativeColor)
                          ,
                      onClick = {
                          currentType.value = 2
                          isChooseAddTransactionTypeOpen.value = false
                      }
                  ) {
                      Text(text = "Add Expense", color = MaterialTheme.colors.onPrimary)
                  }
              }
          }else{
              Card(
                  Modifier.heightIn(min = 500.dp, max = 1000.dp),
              ) {
                  AddTransactionSheetContent(
                      currentType = currentType.value,
                      currentTransaction = currentTransaction.value,
                      categories =  homeUiState.categories,
                      onConfirmTransactionForm = { type, amount, category,date->
                          Log.i("on confirm sheet",if(currentTransaction.value!=null) "yes" else "no")
                          if(currentTransaction.value==null)
                              homeViewModel.addTransaction(
                                  transactionCategory = category,
                                  type = type,
                                  amount = amount,
                                  date = date
                              )
                          else
                              currentTransaction.value?.let {
                                  homeViewModel.updateTransaction(
                                      transactionId =  it.id,
                                      transactionCategory = category,
                                      type = type,
                                      amount = amount,
                                      date = date
                                  )
                              }


                      },
                      onCloseBottomSheet = {
                          coroutineScope.launch {
                              bottomSheetScaffoldState.bottomSheetState.collapse()
                          }
                      },
                      showIncorrectDataSnack = {
                          homeViewModel.showSnackBar(SnackBarType.INCORRECT_DATA)
                      },
                      onAddCategory = { name,type->
                          homeViewModel.addCategory(
                              TransactionCategory(
                                  id = 1,
                                  transaction_type = type,
                                  name = name,
                                  created_at = System.currentTimeMillis()
                              )
                          )
                      },
                      isBottomSheetOpened = bottomSheetScaffoldState.bottomSheetState.isExpanded
                  )


              }
          }
        }, sheetPeekHeight = 0.dp
    ) {

        HomeTabs(
            goToPieChart = {type, tab, date ->
                goToPieChartDetail(type,tab,date)
            },
            homeUiState = homeUiState,
            onTabChanged = {
                balanceType.value = it
                when(it){
                    BalanceType.DAILY->homeViewModel.collectDailyBalance()
                    BalanceType.MONTHLY->homeViewModel.collectMonthlyBalance()
                    BalanceType.YEARLY->homeViewModel.collectYearlyBalance()
                    else->homeViewModel.collectTotalBalance()
                }
            },
            collectBalanceOfDay = {
                homeViewModel.collectDailyBalance(it)
            },
            selectedBalanceType = balanceType.value,
            onMonthChoose = {
                isMonthPickerShown.value =true
            },
            onTransactionClick = {
                coroutineScope.launch {
                    currentTransaction.value = it
                    bottomSheetScaffoldState.bottomSheetState.expand()
                }
            }
        )
        SuBuuSnackBar(
            onDismissSnackBar = { homeViewModel.clearSnackBar() },
            scaffoldState = bottomSheetScaffoldState,
            snackBarType = homeUiState.currentSnackBar,
        )
    }

}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeContent(
    goToPieChart:(type:Int, tab:Int, date:String)->Unit,
    homeUiState: HomeUiState,
    collectBalanceOfDay:(day:String)->Unit,
    balanceType: BalanceType,
    onMonthChoose:()->Unit,
    onTransactionClick:(t:Transaction)->Unit
){

    Scaffold(

    ) {
        Column(Modifier.padding(it)) {
            Spacer(modifier = Modifier.height(30.dp))
            BalanceCard(
                currentBalance = homeUiState.currentBalance,
                incomeBalance = homeUiState.incomeBalance,
                expenseBalance = homeUiState.expenseBalance,
                collectBalaceOfDay = {
                    collectBalanceOfDay(it)
                },
                selectedDay = homeUiState.selectedDay,
                selectedMonth = homeUiState.selectedMonth,
                selectedYear = homeUiState.selectedYear,
                balanceType = balanceType,
                onMonthChoose = {
                    onMonthChoose()
                },
                goToPiechart ={ type, tab, date ->
                    goToPieChart(type,tab,date)
                }
            )
            SectionTitle(title = "History")
            TransactionsCard(
                transactions = homeUiState.transactions,
                onTransactionClick = {
                    onTransactionClick(it)
                }
            )
        }
    }
}
@Composable
fun SectionTitle(title:String,modifier: Modifier = Modifier){
    Row(
        modifier = modifier.absolutePadding(top = 10.dp, bottom = 10.dp, left = 20.dp, right = 20.dp)
    ) {
        Text(text = title, style = MaterialTheme.typography.h6, color = MaterialTheme.colors.primary)
    }
}