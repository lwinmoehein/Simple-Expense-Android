package lab.justonebyte.simpleexpense.ui.components
import androidx.compose.foundation.background
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import compose.icons.FeatherIcons
import compose.icons.feathericons.X

// No changes needed for AppDialog
@Composable
fun AppDialog(
    properties: DialogProperties =  DialogProperties(usePlatformDefaultWidth = true, decorFitsSystemWindows = true),
    content:@Composable () -> Unit,
    onDismiss:()->Unit
){
    Dialog(
        onDismissRequest = {
            onDismiss()
        },
        properties = properties
    ) {
        Row (
            modifier = Modifier.background(MaterialTheme.colorScheme.surface).fillMaxSize()
        ){
            content()
        }
    }
}

@Composable
fun AppAlertDialog(
    title: String? = null,
    isCloseButtonShown: Boolean = false,
    positiveBtnText: String? = null,
    onPositiveBtnClicked: (() -> Unit)? = null,
    negativeBtnText: String? = null,
    onNegativeBtnClicked: (() -> Unit)? = null,
    properties: DialogProperties = DialogProperties(usePlatformDefaultWidth = true, decorFitsSystemWindows = true),
    content: @Composable () -> Unit,
) {
    AlertDialog(
        // Improved: Removed custom modifier and shape logic.
        // This allows the AlertDialog to use its default Material 3 behavior,
        // which is responsive and follows design guidelines.
        onDismissRequest = {
            onNegativeBtnClicked?.invoke()
        },
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (title != null) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall, // Use a more appropriate style for a dialog title
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                if (isCloseButtonShown && onNegativeBtnClicked != null) {
                    IconButton(onClick = {
                        onNegativeBtnClicked()
                    }) {
                        Icon(imageVector = FeatherIcons.X, contentDescription = "Close dialog")
                    }
                }
            }
        },
        text = {
            content()
        },
        confirmButton = {
            if (positiveBtnText != null && onPositiveBtnClicked != null) {
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
            if (onNegativeBtnClicked != null && negativeBtnText != null) {
                TextButton(
                    onClick = {
                        onNegativeBtnClicked()
                    }
                ) {
                    Text(negativeBtnText)
                }
            }

        },
        properties = properties
    )
}