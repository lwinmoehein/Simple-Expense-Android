package lab.justonebyte.simpleexpense.ui.category


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.model.TransactionType
import lab.justonebyte.simpleexpense.ui.components.AppAlertDialog

@Composable
fun AddCategoryInputDialog(
    isShown:Boolean = false,
    initialValue:String = "",
    initialTransactionType:TransactionType = TransactionType.Expense,
    title:String = "Enter :",
    onDialogDismiss:()->Unit,
    onConfirmClick:(name:String,transactionType:TransactionType)->Unit
){
    if(isShown){
        val nameInputValue = remember { mutableStateOf(initialValue) }
        val transactionType = remember { mutableStateOf(initialTransactionType) }

        AppAlertDialog(
            positiveBtnText = stringResource(id = R.string.confirm),
            negativeBtnText = stringResource(id = R.string.cancel),
            content = {
                Column {
                    if(initialValue.isEmpty()){
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = transactionType.value ==TransactionType.Expense,
                                        onClick = {
                                            transactionType.value = TransactionType.Expense
                                        }
                                    )
                                    Text(
                                        text = stringResource(id = R.string.expense),
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(start = 3.dp)
                                    )
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = transactionType.value ==TransactionType.Income,
                                        onClick = {
                                            transactionType.value = TransactionType.Income
                                        }
                                    )
                                    Text(
                                        text = stringResource(id = R.string.income),
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(start = 3.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                    TextField(
                        shape = MaterialTheme.shapes.small,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        value = nameInputValue.value,
                        onValueChange = { nameInputValue.value = it },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text
                        ),
                        label = { Text(text = title) }
                    )
                }
            },
            onPositiveBtnClicked = {
                onConfirmClick(nameInputValue.value,transactionType.value)
                nameInputValue.value = ""
            },
            onNegativeBtnClicked = {
                nameInputValue.value = ""
                onDialogDismiss()
            }
        )
    }
}