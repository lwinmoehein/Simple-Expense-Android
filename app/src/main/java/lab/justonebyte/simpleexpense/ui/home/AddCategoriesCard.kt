package lab.justonebyte.simpleexpense.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.model.TransactionCategory
import lab.justonebyte.simpleexpense.model.TransactionType
import lab.justonebyte.simpleexpense.ui.category.AddNameInputDialog
import lab.justonebyte.simpleexpense.ui.components.getIconFromName


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
        modifier = modifier.absolutePadding(top=20.dp, bottom = 20.dp),
    ) {
        Column(
            Modifier.padding(5.dp)
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .absolutePadding(left = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.enter_category),
                    style = MaterialTheme.typography.labelLarge
                )
                TextButton(
                     onClick = { isAddCategoryDialogOpen.value = true },
                ) {
                    Text(
                        text = stringResource(id = R.string.add),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(imageVector = Icons.Default.Add, contentDescription = "add category")
                }
            }
            LazyVerticalGrid(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .heightIn(max = 300.dp)
                    .padding(10.dp),
                userScrollEnabled = true,
                columns = GridCells.Fixed(2),
                // content padding
                content = {

                    items(filteredCategories.size) { index ->
                        val isSelected = currentCategory?.let { (it.unique_id==filteredCategories[index].unique_id) }

                        Card(
                            modifier = Modifier
                                .padding(2.dp)
                                .fillMaxWidth()
                                .clickable {
                                    onCategoryChosen(filteredCategories[index])
                                },
                            colors =  CardDefaults.cardColors(
                                containerColor = if(isSelected==true) MaterialTheme.colorScheme.primary else  MaterialTheme.colorScheme.surface,
                            )
                        ) {

                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp, horizontal = 5.dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    modifier = Modifier.width(20.dp).height(20.dp),
                                    imageVector = getIconFromName(name = filteredCategories[index].icon_name) , contentDescription ="" ,
                                    tint = if(isSelected != true)  MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = filteredCategories[index].name,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.labelMedium,
                                    color =  if(isSelected != true)  MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                }
            )
        }
       
    }
}