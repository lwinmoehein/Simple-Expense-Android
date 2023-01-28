package lab.justonebyte.moneysubuu.ui.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.launch
import lab.justonebyte.moneysubuu.model.BalanceType
import lab.justonebyte.moneysubuu.model.Transaction
import lab.justonebyte.moneysubuu.model.TransactionCategory
import lab.justonebyte.moneysubuu.ui.components.*


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class, ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen(){
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val homeUiState by homeViewModel.viewModelUiState.collectAsState()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()
    val currentTransaction = remember {
        mutableStateOf<Transaction?>(null)
    }
    val currentType:MutableState<Int> = remember(currentTransaction) { mutableStateOf(
        currentTransaction.value?.type?.value ?: 1) }

    val isChooseAddTransactionTypeOpen =  remember { mutableStateOf(false)}
    val isSelectedTransactionEditMode = remember { mutableStateOf<Boolean?>(null) }
    val isDeleteTransactionDialogOpen = remember { mutableStateOf(false) }
    
    LaunchedEffect(key1 = currentTransaction.value, block =  {
        if(currentTransaction.value!=null)
            bottomSheetScaffoldState.bottomSheetState.expand()
    } )



    fun clearStates(){
        println("clearing states:")
        isChooseAddTransactionTypeOpen.value = false
        isSelectedTransactionEditMode.value = null
        isDeleteTransactionDialogOpen.value = false
        currentTransaction.value = null
        currentType.value = 1
    }



    if (isDeleteTransactionDialogOpen.value) {
        AppAlertDialog(
            title = "Are you sure?",
            positiveBtnText = "Confirm",
            negativeBtnText = "Cancel",
            content = {
                      Text("Are you sure to delete this transaction?")
            },
            onPositiveBtnClicked = {
                currentTransaction.value?.let { homeViewModel.deleteTransaction(it) }

                clearStates()
            },
            onNegativeBtnClicked = {
                clearStates()
            },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        )
    }


    BottomSheetScaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                "X Money Tracker",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        navigationIcon = {
                            if(!bottomSheetScaffoldState.bottomSheetState.isAnimationRunning && bottomSheetScaffoldState.bottomSheetState.isCollapsed){
                                NewTransactionButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            bottomSheetScaffoldState.bottomSheetState.expand()
                                        }
                                        clearStates()
                                        isChooseAddTransactionTypeOpen.value = true
                                    }
                                )
                            }
                        },
                        actions = {

                        }
                    )
                },
        floatingActionButton = {
            if(!bottomSheetScaffoldState.bottomSheetState.isAnimationRunning && bottomSheetScaffoldState.bottomSheetState.isCollapsed){
               NewTransactionButton(
                   onClick = {
                       coroutineScope.launch {
                           bottomSheetScaffoldState.bottomSheetState.expand()
                       }
                       clearStates()
                       isChooseAddTransactionTypeOpen.value = true
                   }
               )
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
            if(isChooseAddTransactionTypeOpen.value && currentTransaction.value==null){
                AddTransactionAction(
                    onAddIncome = {
                        currentType.value = 1
                        isChooseAddTransactionTypeOpen.value = false
                    },
                    onAddExpense = {
                        currentType.value = 2
                        isChooseAddTransactionTypeOpen.value = false
                    }
                )

            }
            else if(currentTransaction.value!=null && isSelectedTransactionEditMode.value==null){
                ChooseTransactionAction(
                    onDeleteClick = {
                        isDeleteTransactionDialogOpen.value = true
                    },
                    onEditClick = {
                        isSelectedTransactionEditMode.value = true
                    }
                )
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
                          if(currentTransaction.value==null) {
                              println("edit:transaction:add")

                              homeViewModel.addTransaction(
                                  transactionCategory = category,
                                  type = type,
                                  amount = amount,
                                  date = date
                              )
                          } else {
                              println("edit:transaction:update")
                              currentTransaction.value?.let {
                                  homeViewModel.updateTransaction(
                                      transactionId = it.id,
                                      transactionCategory = category,
                                      type = type,
                                      amount = amount,
                                      date = date
                                  )
                              }
                          }
                          clearStates()

                      },
                      onCloseBottomSheet = {
                          clearStates()
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
                                  id = 0,
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


        Card {
            HomeContent(
                homeUiState = homeUiState,
                collectBalanceOfDay = {
                    homeViewModel.collectDailyBalance(it)
                                      },
                onMonthPicked = {
                    homeViewModel.collectMonthlyBalance(it)
                },
                onYearPicked = {
                    homeViewModel.collectYearlyBalance(it)
                },
                onTransactionClick = {
                    currentTransaction.value = it
                },
                onTypeChanged = { type->
                    when(type){
                        BalanceType.DAILY->homeViewModel.collectDailyBalance()
                        BalanceType.MONTHLY->homeViewModel.collectMonthlyBalance()
                        BalanceType.YEARLY->homeViewModel.collectYearlyBalance()
                        else->homeViewModel.collectTotalBalance()
                    }
                },
            )
        }
        SuBuuSnackBar(
            onDismissSnackBar = { homeViewModel.clearSnackBar() },
            scaffoldState = bottomSheetScaffoldState,
            snackBarType = homeUiState.currentSnackBar,
        )
    }

}