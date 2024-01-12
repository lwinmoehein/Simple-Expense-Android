package lab.justonebyte.simpleexpense.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Check
import compose.icons.feathericons.Delete

@Composable
fun NumberKeyboard(
    onNumberClick: (Int) -> Unit,
    onDeleteClick: () -> Unit,
    onOkClick: () -> Unit
) {
    var currentNumber by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Card(
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    text = currentNumber,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            IconButton(onClick = {

            }) {
                Icon( imageVector = FeatherIcons.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
        }

        NumberButtonRow(
            numbers = listOf(1, 2, 3,10),
            onNumberClick = {
                currentNumber += it.toString()
            }
        )
        NumberButtonRow(
            numbers = listOf(4, 5, 6,0),
            onNumberClick = {
                currentNumber += it.toString()
            }
        )
        NumberButtonRow(
            numbers = listOf(7, 8, 9,11),
            onNumberClick = {
                currentNumber += it.toString()
            }
        )


        Spacer(modifier = Modifier.height(16.dp))

//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//            Button(
//                modifier = Modifier.weight(1f),
//                colors = ButtonDefaults.elevatedButtonColors(
//                    containerColor = MaterialTheme.colorScheme.primary,
//                    contentColor = MaterialTheme.colorScheme.onPrimary
//                ),
//                onClick = onOkClick
//            ) {
//                Text("OK", style = MaterialTheme.typography.labelMedium)
//            }
//        }
    }
}

@Composable
private fun NumberButtonRow(
    modifier: Modifier = Modifier,
    numbers: List<Int>,
    onNumberClick: (Int) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        numbers.forEach { number ->
            if(number==-1){
                Spacer(modifier = Modifier
                    .weight(1f)
                    .height(80.dp)
                )
            }

            if(number==10){
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(80.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    IconButton(
                        modifier = Modifier.fillMaxSize(),
                        onClick = { onNumberClick(number) }
                    ) {
                        Row (
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement =  Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Icon(imageVector = FeatherIcons.Delete, contentDescription ="" )
                        }
                    }
                }
            }
            if(number==11){
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(80.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    IconButton(
                        modifier = Modifier.fillMaxSize(),
                        onClick = { onNumberClick(number) }
                    ) {
                        Row (
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement =  Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Icon(imageVector = FeatherIcons.Check, contentDescription ="" )
                        }
                    }
                }
            }

            if(number in 0..9){
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(80.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    TextButton(
                        modifier = Modifier.fillMaxSize(),
                        onClick = { onNumberClick(number) }
                    ) {
                        Row (
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement =  Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(
                                text = number.toString(),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }
        }
    }
}