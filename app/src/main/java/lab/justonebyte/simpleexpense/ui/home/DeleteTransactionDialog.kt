package lab.justonebyte.simpleexpense.ui.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.ui.components.AppAlertDialog

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DeleteTransactionDialog(
    isOpen:Boolean = false,
    onCancelClick:()->Unit,
    onConfirmClick:()->Unit
){
    if (isOpen) {
        AppAlertDialog(
            title = stringResource(id = R.string.r_u_sure ),
            positiveBtnText = stringResource(id = R.string.confirm),
            negativeBtnText = stringResource(id = R.string.cancel),
            content = {
                Text(stringResource(id = R.string.r_u_sure_tran_delete))
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