package lab.justonebyte.moneysubuu.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import lab.justonebyte.moneysubuu.model.TransactionCategory
import lab.justonebyte.moneysubuu.model.TransactionType
import lab.justonebyte.moneysubuu.ui.theme.Red900

@Composable
fun AddCategoriesCard(
    categories:List<TransactionCategory>,
    currentCategory:TransactionCategory?,
    onCategoryChosen:(categor:TransactionCategory)->Unit,
    modifier: Modifier = Modifier,
    currentTransactionType:TransactionType = TransactionType.Income,
    onAddCategory:(name:String)->Unit,
){
    val filteredCategories = categories.filter { it.transaction_type==currentTransactionType }
    val isAddCategoryDialogOpen = remember { mutableStateOf(false)}

    AddCategoryNameDialog(
        isShown = isAddCategoryDialogOpen.value,
        onConfirm = {
            onAddCategory(it)
        }
    )
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