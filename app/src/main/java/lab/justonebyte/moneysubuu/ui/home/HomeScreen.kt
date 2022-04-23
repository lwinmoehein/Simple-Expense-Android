package lab.justonebyte.moneysubuu.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import lab.justonebyte.moneysubuu.model.TransactionCategory
import lab.justonebyte.moneysubuu.ui.appContentPadding
import lab.justonebyte.moneysubuu.ui.theme.SuBuuShapes

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    openDrawer:()->Unit,
    homeUiState: HomeUiState
){
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
                    categories = homeUiState.categories,
                    onCloseBottomSheet = {
                        coroutineScope.launch {
                            if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                bottomSheetScaffoldState.bottomSheetState.expand()
                            } else {
                                bottomSheetScaffoldState.bottomSheetState.collapse()
                            }
                        }
                    }
                )
            }
        }, sheetPeekHeight = 0.dp
    ) {
        HomeContent(
            homeUiState = homeUiState,
            onOpenBottomSheet = {
                coroutineScope.launch {
                    if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                        bottomSheetScaffoldState.bottomSheetState.expand()
                    } else {
                        bottomSheetScaffoldState.bottomSheetState.collapse()
                    }
                }
            }
        )
    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeContent(onOpenBottomSheet:()->Unit,homeUiState: HomeUiState){
    val coroutineScope = rememberCoroutineScope()

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
        CurrentBalance(currentBalance = homeUiState.currentBalance)
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
fun BottomSheetContent(onCloseBottomSheet:()->Unit,categories:List<TransactionCategory>){
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
        LazyColumn(){
            items(categories){
                CategoryItem(transactionCategory = it)
            }
        }
    }
}
@Composable
fun CategoryItem(transactionCategory: TransactionCategory){
    Text(text = transactionCategory.name,modifier = Modifier.fillMaxWidth())
}