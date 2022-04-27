package lab.justonebyte.moneysubuu.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.onPrimary)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        text = "Category Name : ",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.subtitle2
                    )
                    TextField(
                        value = addCategoryName.value,
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                         ,
                        onValueChange = {
                            addCategoryName.value = it
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                    )

                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .absolutePadding(top = 30.dp)
                ) {
                    TextButton(
                        onClick = { isAddCategoryDialogOpen.value = false },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Cancel")
                    }
                    TextButton(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            onAddCategory(addCategoryName.value)
                            isAddCategoryDialogOpen.value = false
                            addCategoryName.value = ""
                        }
                    ) {
                           Text(text = "Confirm",color = MaterialTheme.colors.primary)
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
                                    isAddCategoryDialogOpen.value = true
                                },
                        ) {
                          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                              Text(
                                  text ="Add",
                                  textAlign = TextAlign.Center,
                                  modifier = Modifier.padding(10.dp),
                                  style = MaterialTheme.typography.button,
                                  color = MaterialTheme.colors.primary
                              )
                              Icon(imageVector = Icons.Default.Add, contentDescription = "add category", tint = MaterialTheme.colors.primary)

                          }
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