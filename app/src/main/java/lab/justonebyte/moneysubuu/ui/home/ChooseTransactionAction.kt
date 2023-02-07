package lab.justonebyte.moneysubuu.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onEditClick()
            }
        ) {
            Text(text = stringResource(id = R.string.edit_tran))
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
               onDeleteClick()
            }
        ) {
            Text(text = stringResource(id = R.string.delete_tran))
        }
    }
}