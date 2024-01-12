package lab.justonebyte.simpleexpense.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun MinimalDialog(onDismissRequest: () -> Unit,content: @Composable () -> Unit) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        content()
    }
}

@Composable
fun ProgressDialog(onDismissRequest: () -> Unit,text:String){
    MinimalDialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            shape = MaterialTheme.shapes.medium
        ) {
            Row (
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.background(MaterialTheme.colorScheme.surface).padding(horizontal = 30.dp, vertical = 15.dp)
            ){
                Text(text = text,color = MaterialTheme.colorScheme.onSurface)

                CircularProgressIndicator(
                    modifier = Modifier.width(20.dp).height(20.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    strokeWidth = 3.dp
                )
            }
        }
    }
}
