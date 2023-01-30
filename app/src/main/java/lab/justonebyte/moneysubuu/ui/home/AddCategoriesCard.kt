package lab.justonebyte.moneysubuu.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import lab.justonebyte.moneysubuu.R
import lab.justonebyte.moneysubuu.model.TransactionCategory
import lab.justonebyte.moneysubuu.model.TransactionType
import lab.justonebyte.moneysubuu.ui.category.AddNameInputDialog


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

    AddNameInputDialog(
        title = if(currentTransactionType==TransactionType.Income) stringResource(id = R.string.enter_in_category) else stringResource(R.string.enter_ex_category),
        isShown = isAddCategoryDialogOpen.value,
        onDialogDismiss = {
            isAddCategoryDialogOpen.value = false
        },
        onConfirmClick = {
            if(it.isEmpty()) {

            }else{
                onAddCategory(it)
            }
            isAddCategoryDialogOpen.value = false
        }
    )

    Card(
        modifier = modifier.absolutePadding(top=30.dp, bottom = 20.dp),
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row() {
                Text(text = stringResource(id = R.string.enter_category))
            }
            LazyVerticalGrid(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.heightIn(max=300.dp),
                userScrollEnabled = true,
                columns = GridCells.Fixed(2),
                // content padding
                contentPadding = PaddingValues(
                    top = 16.dp,
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
                                  text = stringResource(id = R.string.add),
                                  textAlign = TextAlign.Center,
                                  modifier = Modifier.padding(10.dp)
                              )
                              Icon(imageVector = Icons.Default.Add, contentDescription = "add category")

                          }
                        }
                    }
                    items(filteredCategories.size) { index ->
                        val isSelected = currentCategory?.let { (it.id==filteredCategories[index].id) }

                        Card(
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