package lab.justonebyte.moneysubuu.ui.components
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AppAlertDialog(
    title: String,
    positiveBtnText: String,
    onPositiveBtnClicked: () -> Unit = {},
    negativeBtnText: String? = null,
    onNegativeBtnClicked: (() -> Unit)? = null,
    properties: DialogProperties =  DialogProperties(usePlatformDefaultWidth = true),
    content:@Composable () -> Unit,
){

        AlertDialog(
            modifier = if(properties.usePlatformDefaultWidth) Modifier.wrapContentSize() else Modifier.fillMaxSize(),
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                if (onNegativeBtnClicked != null) {
                    onNegativeBtnClicked()
                }
            },
            title = {
                Text(text = title)
            },
            text = {
                content()
            },
            confirmButton = {
                if(properties.usePlatformDefaultWidth){
                    TextButton(
                        onClick = {
                            onPositiveBtnClicked()
                        }
                    ) {
                        Text(positiveBtnText)
                    }
                }

            },
            dismissButton = {
                if(properties.usePlatformDefaultWidth){
                    TextButton(
                        onClick = {
                            if (onNegativeBtnClicked != null) {
                                onNegativeBtnClicked()
                            }
                        }
                    ) {
                        if (negativeBtnText != null) {
                            Text(negativeBtnText)
                        }
                    }
                }

            },
            properties = properties
        )
}