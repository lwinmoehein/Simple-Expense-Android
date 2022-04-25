package lab.justonebyte.moneysubuu.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import lab.justonebyte.moneysubuu.model.TransactionCategory
import lab.justonebyte.moneysubuu.model.TransactionType
import lab.justonebyte.moneysubuu.ui.components.CustomTextField

@Composable
fun AddCategoriesCard(
    categories:List<TransactionCategory>,
    currentCategory:TransactionCategory?,
    onCategoryChosen:(categor:TransactionCategory)->Unit,
    modifier: Modifier = Modifier,
    currentTransactionType:TransactionType = TransactionType.Income,
    onAddCategory:(name:String)->Unit
){
    val filteredCategories = categories.filter { it.transaction_type==currentTransactionType }
    val isAddCategoryDialogOpen = remember { mutableStateOf(false)}
    val addCategoryName = remember { mutableStateOf("")}

    if(isAddCategoryDialogOpen.value){
        Dialog(onDismissRequest = {isAddCategoryDialogOpen.value = false}) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .absolutePadding(left = 10.dp, right = 10.dp)
                ) {
                    Text("Category Name : ",modifier = Modifier.weight(1f),style = MaterialTheme.typography.subtitle2)
                    CustomTextField(
                        text = addCategoryName.value,
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                            .height(50.dp),
                        placeholderText = "Entry category name",
                        onValueChange = {
                            addCategoryName.value = it
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                    )

                }
                Row() {
                    TextButton(onClick = { isAddCategoryDialogOpen.value = false }) {
                        Text(text = "Cancel")
                    }
                    TextButton(onClick = { onAddCategory(addCategoryName.value) }) {
                        Text(text = "Add Category")
                    }
                }
            }
        }
    }

    Card(
        modifier = modifier.absolutePadding(top=30.dp, bottom = 20.dp),
        elevation = 5.dp
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row() {
                Text(text = "Choose Category:", style = MaterialTheme.typography.subtitle2)
            }
            LazyVerticalGrid(
                columns = GridCells.Adaptive(128.dp),
                // content padding
                contentPadding = PaddingValues(
                    top = 16.dp,
                    end = 12.dp,
                ),
                content = {
                    item {
                        Card(
                            modifier = Modifier
                                .padding(2.dp)
                                .fillMaxWidth()
                                .clickable {
                                    onAddCategory(addCategoryName.value)
                                },
                        ) {
                            Text(
                                text ="Add Category",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                    items(filteredCategories.size) { index ->

                        Card(
                            backgroundColor =  currentCategory?.let {
                                if (it.id==filteredCategories[index].id) MaterialTheme.colors.primary else MaterialTheme.colors.onPrimary
                            }?: MaterialTheme.colors.onPrimary,
                            modifier = Modifier
                                .padding(2.dp)
                                .fillMaxWidth()
                                .clickable {
                                    onCategoryChosen(filteredCategories[index])
                                },
                        ) {
                            Text(
                                text = filteredCategories[index].name,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            )
        }
       
    }
}