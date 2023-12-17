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