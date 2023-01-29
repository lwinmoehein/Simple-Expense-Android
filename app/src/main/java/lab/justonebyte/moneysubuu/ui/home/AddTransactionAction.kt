package lab.justonebyte.moneysubuu.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lab.justonebyte.moneysubuu.ui.theme.negativeColor
import lab.justonebyte.moneysubuu.ui.theme.positiveColor
import lab.justonebyte.moneysubuu.R

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
        TextButton(
            onClick = {
               onAddIncome()
            }
        ) {
            Text(text =  stringResource(id = R.string.add_income))
        }
        TextButton(
            onClick = {
               onAddExpense()
            }
        ) {
            Text(text = stringResource(id = R.string.add_expense))
        }
    }

}