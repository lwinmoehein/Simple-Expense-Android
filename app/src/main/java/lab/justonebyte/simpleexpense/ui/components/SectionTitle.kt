package lab.justonebyte.simpleexpense.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

@Composable
fun SectionTitle(title:String,modifier: Modifier=Modifier,style:TextStyle=MaterialTheme.typography.titleMedium){
    Text(
        text = title,
        style = style,
        fontWeight = FontWeight.Bold,
        modifier = modifier,
        color = MaterialTheme.colorScheme.primary
    )
}