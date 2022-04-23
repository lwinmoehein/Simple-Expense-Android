package lab.justonebyte.moneysubuu.ui.home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import lab.justonebyte.moneysubuu.model.Transaction
import lab.justonebyte.moneysubuu.model.TransactionCategory
import lab.justonebyte.moneysubuu.ui.appContentPadding
import lab.justonebyte.moneysubuu.ui.theme.SuBuuShapes

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    openDrawer:()->Unit,

){
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val homeUiState by homeViewModel.viewModelUiState.collectAsState()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()


    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetElevation = 8.dp,
        sheetShape = RoundedCornerShape(
            bottomStart = 0.dp,
            bottomEnd = 0.dp,
            topStart = 12.dp,
            topEnd = 12.dp
        ),

        sheetContent = {
            Card(
                        Modifier.heightIn(min = 500.dp, max = 1000.dp),
            ) {
                BottomSheetContent(
                    categories =  homeUiState.categories,
                    onAddTransaction = {type, amount, category ->
                        homeViewModel.addTransaction(
                            transactionCategory = category,
                            type = type,
                            amount = amount
                        )
                    },
                    onCloseBottomSheet = {
                        coroutineScope.launch {
                            Log.i("bottomsheet:", bottomSheetScaffoldState.bottomSheetState.isExpanded.toString())

                            bottomSheetScaffoldState.bottomSheetState.collapse()
                            Log.i("bottomsheet:", bottomSheetScaffoldState.bottomSheetState.isExpanded.toString())
                        }
                    }
                )
            }
        }, sheetPeekHeight = 0.dp
    ) {
        HomeContent(
            onOpenBottomSheet = {

                coroutineScope.launch {
                    bottomSheetScaffoldState.bottomSheetState.expand()
                }
            },
            currentBalance = homeUiState.currentBalance

        )
    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeContent(onOpenBottomSheet:()->Unit, currentBalance:Double = 1.0 ){
    Scaffold( floatingActionButton = {
        IconButton(
            modifier = Modifier.absolutePadding(bottom=100.dp, right = 30.dp),
            onClick = {
                onOpenBottomSheet()
              }) {
            Icon(              modifier = Modifier
                .width(100.dp)
                .height(100.dp),
                imageVector = Icons.Filled.AddCircle, contentDescription ="add transaction" )
        }

    },) {
       CurrentBalance(currentBalance=currentBalance)

    }
}
@Composable
fun CurrentBalance(
    modifier: Modifier =Modifier,
    currentBalance: Double =0.0
){
   Card(
       shape = SuBuuShapes.small,
       modifier = modifier
           .fillMaxWidth()
           .height(100.dp)
           .padding(10.dp),
       backgroundColor = MaterialTheme.colors.primary,
       elevation = 10.dp
   ) {
       Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
       ) {
           Row() {
               Text(
                   text = "Current Balance : ",
                   style = MaterialTheme.typography.h6
               )
               Text(
                   text = currentBalance.toString(),
                   style = MaterialTheme.typography.h6

               )
           }
       }
   }
}
@Composable
fun BottomSheetContent(
    onCloseBottomSheet:()->Unit,
    categories:List<TransactionCategory>,
    onAddTransaction:(type:Int,amount:Int,category:TransactionCategory)->Unit
){
    val currentType = remember{ mutableStateOf(1)}
    val currentCategory = remember{ mutableStateOf<TransactionCategory?>(null)}
    val currentAmount = remember {
        mutableStateOf(0)
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(appContentPadding)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween,modifier = Modifier.fillMaxWidth()) {
            Text(text = "Add transaction", style = MaterialTheme.typography.subtitle1)
            IconButton(onClick = { onCloseBottomSheet() }) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = "close sheet" )
            }
        }
        Row(horizontalArrangement = Arrangement.SpaceAround) {
            TextButton(onClick = { currentType.value =1 }) {
                Text(text = "Income", style = MaterialTheme.typography.subtitle1, color = if(currentType.value==1) MaterialTheme.colors.primary else Color.Black)
            }
            TextButton(onClick = { currentType.value =2  }) {
                Text(text = "Expense",style = MaterialTheme.typography.subtitle1,color = if(currentType.value==2) MaterialTheme.colors.primary else Color.Black)
            }
        }
        TextField(
            value = currentAmount.value.toString(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            onValueChange = {
            currentAmount.value =if(it=="") 0 else  it.toInt()
        })
        LazyColumn(){
            items(categories){
                CategoryItem(currentCategory = currentCategory.value,transactionCategory = it, onCategoryClicked = {
                    currentCategory.value = it
                })
            }
        }
        Row(horizontalArrangement = Arrangement.End) {
            TextButton(onClick = {
                currentCategory.value?.let {
                    onAddTransaction(
                        currentType.value,
                        currentAmount.value,
                        it
                    )
                }
            }) {
                Text(text = "Add Transaction")
            }
        }
    }
}
@Composable
fun CategoryItem(currentCategory:TransactionCategory? =null,transactionCategory: TransactionCategory,onCategoryClicked:(category:TransactionCategory)->Unit){
    Text(
        text = transactionCategory.name,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCategoryClicked(transactionCategory) },
        color = if(currentCategory!=null && currentCategory.id==transactionCategory.id) MaterialTheme.colors.primary else Color.Black
    )
}