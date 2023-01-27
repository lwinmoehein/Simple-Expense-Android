package lab.justonebyte.moneysubuu.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lab.justonebyte.moneysubuu.R


@Composable
fun NewTransactionButton(
    modifier: Modifier = Modifier,
    onClick:()->Unit
){
    Box(
        modifier = Modifier
            .absolutePadding(bottom = 100.dp, left = 30.dp)
    ) {
        TextButton(
            onClick = {
               onClick()
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .padding(0.dp)
                .background(MaterialTheme.colors.primary)
        ) {
            Text(text = stringResource(id = R.string.add_new_record), style = MaterialTheme.typography.button, color = MaterialTheme.colors.onPrimary)
            Icon(
                modifier = Modifier
                    .width(30.dp)
                    .height(30.dp)
                ,
                imageVector = Icons.Filled.Add, contentDescription = "add transaction",
                tint = MaterialTheme.colors.onPrimary
            )
        }
    }
}