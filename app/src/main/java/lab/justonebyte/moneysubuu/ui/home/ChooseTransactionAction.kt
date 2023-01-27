package lab.justonebyte.moneysubuu.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lab.justonebyte.moneysubuu.R
import lab.justonebyte.moneysubuu.ui.theme.Red300
import lab.justonebyte.moneysubuu.ui.theme.primary

@Composable
fun ChooseTransactionAction(
    onDeleteClick:()->Unit,
    onEditClick:()->Unit
){
    Column(
        modifier= Modifier
            .height(200.dp)
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextButton(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(5.dp))
                .padding(0.dp)
                .background(primary)
            ,
            onClick = {
                onEditClick()
            }
        ) {
            Text(text = stringResource(id = R.string.edit_tran),color=MaterialTheme.colors.onPrimary)
        }
        Divider(Modifier.height(20.dp), color = Color.Transparent)
        TextButton(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(5.dp))
                .padding(0.dp)
                .background(Red300)
            ,
            onClick = {
               onDeleteClick()
            }
        ) {
            Text(text = stringResource(id = R.string.delete_tran), color = MaterialTheme.colors.onPrimary)
        }
    }
}