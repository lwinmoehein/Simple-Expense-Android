package lab.justonebyte.moneysubuu.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import lab.justonebyte.moneysubuu.model.TransactionCategory
import lab.justonebyte.moneysubuu.ui.appContentPadding

@Composable
fun AddTransactionSheetContent(
    onCloseBottomSheet:()->Unit,
    categories:List<TransactionCategory>,
    showIncorrectDataSnack:()->Unit,
    onAddTransaction:(type:Int,amount:Int,category: TransactionCategory)->Unit
){
    val currentType = remember{ mutableStateOf(1) }
    val currentCategory = remember{ mutableStateOf<TransactionCategory?>(null) }
    val currentAmount = remember {
        mutableStateOf("")
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(appContentPadding)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
        ) {
            Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxHeight()) {
                Text(text = "Add transaction", style = MaterialTheme.typography.subtitle1)
            }
            IconButton(onClick = { onCloseBottomSheet() }) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = "close sheet" )
            }
        }
        Row(modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()) {
            TextButton(
                modifier = Modifier.weight(1f),
                border = BorderStroke(2.dp,if(currentType.value==1) MaterialTheme.colors.primary else Color.Transparent),
                onClick = { currentType.value =1 }
            ) {
                Text(text = "Income", style = MaterialTheme.typography.subtitle1, color = if(currentType.value==1) MaterialTheme.colors.primary else Color.Black)
            }
            TextButton(
                modifier= Modifier.weight(1f),
                border = BorderStroke(2.dp,if(currentType.value==2) MaterialTheme.colors.primary else Color.Transparent),
                onClick = { currentType.value =2  }
            ) {
                Text(text = "Expense",style = MaterialTheme.typography.subtitle1,color = if(currentType.value==2) MaterialTheme.colors.primary else Color.Black)
            }
        }
        Row(horizontalArrangement = Arrangement.Center,modifier = Modifier
            .fillMaxWidth()
            .absolutePadding(left = 50.dp, right = 50.dp)) {
            TextField(
                colors = TextFieldDefaults.textFieldColors(

                    backgroundColor = Color.Transparent,

                    ),
                value = currentAmount.value,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                onValueChange = {
                    currentAmount.value = it.filter { it.isDigit() }
                },
                label = {
                    Row(horizontalArrangement = Arrangement.End) {
                        Text(text = if(currentType.value==1) "Income amount in kyat" else "Expense amount in kyat" )
                    }
                }
            )

        }


        LazyVerticalGrid(
            columns = GridCells.Adaptive(128.dp),

            // content padding
            contentPadding = PaddingValues(
                start = 12.dp,
                top = 16.dp,
                end = 12.dp,
                bottom = 16.dp
            ),
            content = {
                items(categories.size) { index ->

                    Card(
                        backgroundColor =  currentCategory.value?.let {
                            if (it.id==categories[index].id) MaterialTheme.colors.primary else MaterialTheme.colors.onPrimary
                        }?: MaterialTheme.colors.onPrimary,
                        modifier = Modifier
                            .padding(2.dp)
                            .fillMaxWidth()
                            .clickable {
                                currentCategory.value = categories[index]
                            },
                        elevation = 8.dp,
                    ) {
                        Text(
                            text = categories[index].name,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        )
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            TextButton(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier

                    .absolutePadding(top = 20.dp, bottom = 20.dp)
                    .background(MaterialTheme.colors.primary)
                    .absolutePadding(left = 40.dp, right = 40.dp),
                onClick = {
                    val amount =if(currentAmount.value.isEmpty()) 0 else currentAmount.value.toInt()

                    if(currentCategory.value==null || amount<=0){
                        showIncorrectDataSnack()
                    }else{
                        currentCategory.value?.let {
                            onAddTransaction(
                                currentType.value,
                                amount,
                                it
                            )
                            onCloseBottomSheet()
                            currentAmount.value = ""
                            currentCategory.value = null
                            currentType.value=1

                        }
                    }

                }) {
                Text(text = "Add Transaction", color = Color.White)
            }
        }
    }
}