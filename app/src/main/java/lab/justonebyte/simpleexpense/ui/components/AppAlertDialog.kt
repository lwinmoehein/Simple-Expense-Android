package lab.justonebyte.simpleexpense.ui.components
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import compose.icons.FeatherIcons
import compose.icons.feathericons.X

@Composable
fun AppAlertDialog(
    title: String?=null,
    isCloseButtonShown:Boolean = false,
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (title != null) {
                        Text(text = title, style = MaterialTheme.typography.titleMedium)
                    }
                    if(isCloseButtonShown && onNegativeBtnClicked!=null){
                        IconButton(onClick = {
                            onNegativeBtnClicked()
                        }) {
                          Icon(imageVector = FeatherIcons.X, contentDescription ="" )
                        }
                    }
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
                            onNegativeBtnClicked()
                        }
                    ) {
                        Text(negativeBtnText)
                    }
                }

            },
            properties = properties,
            shape = if(properties.usePlatformDefaultWidth) MaterialTheme.shapes.large else MaterialTheme.shapes.small
        )
}