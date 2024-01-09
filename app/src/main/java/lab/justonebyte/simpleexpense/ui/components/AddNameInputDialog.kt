package lab.justonebyte.simpleexpense.ui.category


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import lab.justonebyte.simpleexpense.ui.components.AppAlertDialog
import lab.justonebyte.simpleexpense.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNameInputDialog(
    isShown:Boolean = false,
    initialValue:String = "",
    title:String = "Enter :",
    onDialogDismiss:()->Unit,
    onConfirmClick:(name:String)->Unit
){
    if(isShown){
        val nameInputValue = remember { mutableStateOf(initialValue) }
        val focusRequester = remember() {
            FocusRequester()
        }
        AppAlertDialog(
            positiveBtnText = stringResource(id = R.string.confirm),
            negativeBtnText = stringResource(id = R.string.cancel),
            content = {
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
            },
            onPositiveBtnClicked = {
                onConfirmClick(nameInputValue.value)
                nameInputValue.value = ""
            },
            onNegativeBtnClicked = {
                nameInputValue.value = ""
                onDialogDismiss()
            }
        )
    }
}