package lab.justonebyte.moneysubuu.ui.home

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                AddTransactionSheetContent(
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
       BalanceCard(currentBalance=currentBalance)

    }
}


