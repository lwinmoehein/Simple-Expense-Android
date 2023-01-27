package lab.justonebyte.moneysubuu.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
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
        modifier= Modifier
            .height(200.dp)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextButton(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(5.dp))
                .padding(0.dp)
                .background(positiveColor)
            ,
            onClick = {
               onAddIncome()
            }
        ) {
            Text(text =  stringResource(id = R.string.add_income),color=MaterialTheme.colors.onPrimary)
        }
        Divider(Modifier.height(20.dp), color = Color.Transparent)
        TextButton(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(5.dp))
                .padding(0.dp)
                .background(negativeColor)
            ,
            onClick = {
               onAddExpense()
            }
        ) {
            Text(text = stringResource(id = R.string.add_expense), color = MaterialTheme.colors.onPrimary)
        }
    }

}