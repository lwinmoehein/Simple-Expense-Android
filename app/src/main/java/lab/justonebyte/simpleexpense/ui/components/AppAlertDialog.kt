package lab.justonebyte.simpleexpense.ui.components
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties

@Composable
fun AppAlertDialog(
    title: String?=null,
    positiveBtnText: String? = null,
    onPositiveBtnClicked: (() -> Unit)? = null,
    negativeBtnText: String? = null,
    onNegativeBtnClicked: (() -> Unit)? = null,
    properties: DialogProperties =  DialogProperties(usePlatformDefaultWidth = true),
    content:@Composable () -> Unit,
){

        AlertDialog(
            modifier = if(properties.usePlatformDefaultWidth) Modifier.fillMaxWidth() else Modifier.fillMaxSize(),
            onDismissRequest = {
                if (onNegativeBtnClicked != null) {
                    onNegativeBtnClicked()
                }
            },
            title = {
                if (title != null) {
                    Text(text = title, style = MaterialTheme.typography.titleMedium)
                }
            },
            text = {
                content()
            },
            confirmButton = {
                if(positiveBtnText!=null && onPositiveBtnClicked!=null ){
                    Button(
                        onClick = {
                            onPositiveBtnClicked()
                        }
                    ) {
                        Text(positiveBtnText)
                    }
                }

            },
            dismissButton = {
                if(onNegativeBtnClicked!=null && negativeBtnText!=null){
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
            properties = properties,
            shape = MaterialTheme.shapes.large
        )
}