package lab.justonebyte.moneysubuu.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import lab.justonebyte.moneysubuu.model.TransactionCategory

@Composable
fun AddCategoriesCard(
    categories:List<TransactionCategory>,
    currentCategory:TransactionCategory?,
    onCategoryChosen:(categor:TransactionCategory)->Unit,
    modifier: Modifier = Modifier
){
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
                    items(categories.size) { index ->

                        Card(
                            backgroundColor =  currentCategory?.let {
                                if (it.id==categories[index].id) MaterialTheme.colors.primary else MaterialTheme.colors.onPrimary
                            }?: MaterialTheme.colors.onPrimary,
                            modifier = Modifier
                                .padding(2.dp)
                                .fillMaxWidth()
                                .clickable {
                                    onCategoryChosen(categories[index])
                                },
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
        }
       
    }
}