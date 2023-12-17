package lab.justonebyte.simpleexpense.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import lab.justonebyte.simpleexpense.R
@Composable
fun AddTransactionAction(
    onAddIncome:()->Unit,
    onAddExpense:()->Unit
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
               onAddIncome()
            }
        ) {
            Text(text =  stringResource(id = R.string.add_income))
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
               onAddExpense()
            }
        ) {
            Text(text = stringResource(id = R.string.add_expense))
        }
    }

}