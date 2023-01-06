package lab.justonebyte.moneysubuu.ui.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SectionTitle(title:String,modifier: Modifier = Modifier){
    Row(
        modifier = modifier.absolutePadding(top = 10.dp, bottom = 10.dp, left = 20.dp, right = 20.dp)
    ) {
        Text(text = title, style = MaterialTheme.typography.h6, color = MaterialTheme.colors.primary)
    }
}