package lab.justonebyte.simpleexpense.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material3.Card
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Check
import compose.icons.feathericons.Delete
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.model.Currency

@Composable
fun NumberKeyboard(
    initialNumber:String = "",
    isKeyboardShown:Boolean = false,
    onNumberConfirm: (number:Int) -> Unit,
    onKeyboardToggled:(isShown:Boolean)->Unit,
    currency: Currency
) {
    var currentNumber by remember { mutableStateOf(initialNumber) }
    val isNumbersShown = remember { mutableStateOf(isKeyboardShown) }

    fun appendNumber(number: Int){
        if(number==0 && currentNumber.isEmpty()) return
        currentNumber = currentNumber.plus(number.toString())
    }

    fun willCurrentNumberExceed(appendNumber:Int):Boolean{
        if(currentNumber.isEmpty()) return false
        val appendedNumber = currentNumber.plus(appendNumber.toString()).toLong()
        return appendedNumber>Int.MAX_VALUE
    }
    fun clearLastNumber() {
        if (currentNumber.isNotEmpty()) {
            currentNumber = currentNumber.dropLast(1)
        }
    }
    fun onConfirmClick(){
        if(currentNumber.isEmpty()){
            onNumberConfirm(-1)
        }else{
            onNumberConfirm(currentNumber.toInt())
        }
        isNumbersShown.value = false
    }
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    isNumbersShown.value = !isNumbersShown.value
                    onKeyboardToggled(isNumbersShown.value)
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Card(
                modifier = Modifier.height(50.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 10.dp),
                ) {
                    Row(
                        Modifier.padding(horizontal = 10.dp)
                    ) {
                        Text(
                            modifier = Modifier.absolutePadding(right = 15.dp),
                            text = stringResource(id =if(currency.name==Currency.Kyat.name) R.string.kyat else R.string.dollar),
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier
                            .fillMaxHeight()
                            .width(2.dp)
                            .background(MaterialTheme.colorScheme.secondary))
                    }
                    Text(
                        text = if(currentNumber.isEmpty()) stringResource(id = R.string.enter_amount) else currentNumber,
                        style = if(currentNumber.isEmpty()) MaterialTheme.typography.titleSmall else MaterialTheme.typography.titleLarge,
                        color =if(currentNumber.isEmpty()) MaterialTheme.colorScheme.secondary else  MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }

        if(isNumbersShown.value){
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                NumberButtonRow(
                    numbers = listOf(1, 2, 3,10),
                    onNumberClick = {
                        if(!willCurrentNumberExceed(it)) appendNumber(it)
                    },
                    onDelete = {
                        clearLastNumber()
                    },
                    onConfirm = {
                        onConfirmClick()
                    }
                )
                NumberButtonRow(
                    numbers = listOf(4, 5, 6,0),
                    onNumberClick = {
                        if(!willCurrentNumberExceed(it)) appendNumber(it)
                    },
                    onDelete = {
                       clearLastNumber()
                    },
                    onConfirm = {
                        onConfirmClick()
                    }
                )
                NumberButtonRow(
                    numbers = listOf(7, 8, 9,11),
                    onNumberClick = {
                        if(!willCurrentNumberExceed(it)) appendNumber(it)
                    },
                    onDelete = {
                        clearLastNumber()
                    },
                    onConfirm = {
                        onConfirmClick()
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun NumberButtonRow(
    modifier: Modifier = Modifier,
    numbers: List<Int>,
    onNumberClick: (Int) -> Unit,
    onDelete:()->Unit,
    onConfirm:()->Unit
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
                        onClick = {
                            onDelete()
                        }
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
                        onClick = {
                            onConfirm()
                        }
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