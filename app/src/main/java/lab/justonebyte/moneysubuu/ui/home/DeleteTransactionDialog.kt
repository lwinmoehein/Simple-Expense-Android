package lab.justonebyte.moneysubuu.ui.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.DialogProperties
import lab.justonebyte.moneysubuu.ui.components.AppAlertDialog

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DeleteTransactionDialog(
    isOpen:Boolean = false,
    onCancelClick:()->Unit,
    onConfirmClick:()->Unit
){
    if (isOpen) {
        AppAlertDialog(
            title = "Are you sure?",
            positiveBtnText = "Confirm",
            negativeBtnText = "Cancel",
            content = {
                Text("Are you sure to delete this transaction?")
            },
            onPositiveBtnClicked = {
                onConfirmClick()
            },
            onNegativeBtnClicked = {
                onCancelClick()
            },
            properties = DialogProperties(usePlatformDefaultWidth = true)
        )
    }
}