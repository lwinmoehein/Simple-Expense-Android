package lab.justonebyte.moneysubuu.ui.category

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import lab.justonebyte.moneysubuu.R
import lab.justonebyte.moneysubuu.ui.components.AppAlertDialog
import lab.justonebyte.moneysubuu.ui.theme.primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNameInputDialog(
    isShown:Boolean = false,
    initialValue:String = "",
    title:String = "Enter :",
    dialogColor:Color = primary,
    onDialogDismiss:()->Unit,
    onConfirmClick:(name:String)->Unit
){
    if(isShown){
        val nameInputValue = remember { mutableStateOf(initialValue) }
        val focusRequester = remember() {
            FocusRequester()
        }
        AppAlertDialog(
            title = title,
            positiveBtnText = stringResource(id = R.string.confirm),
            negativeBtnText = stringResource(id = R.string.cancel),
            content = {
//                BasicTextField(
//                    value = nameInputValue.value,
//                    onValueChange = {
//                        nameInputValue.value = it
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .heightIn(min = 40.dp)
//                        .absolutePadding(left = 10.dp, right = 10.dp, top = 5.dp, bottom = 5.dp),
//                    singleLine = true,
//                )
                TextField(
                    shape = MaterialTheme.shapes.small,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    value = nameInputValue.value,
                    onValueChange = { nameInputValue.value = it },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        disabledTextColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
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