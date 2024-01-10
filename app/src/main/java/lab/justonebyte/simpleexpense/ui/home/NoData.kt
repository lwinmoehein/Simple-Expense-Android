package lab.justonebyte.simpleexpense.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Info
import lab.justonebyte.simpleexpense.R

@Composable
fun NoData(modifier: Modifier=Modifier,text:String= stringResource(id = R.string.no_data)){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 300.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Row (
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(imageVector = FeatherIcons.Info, contentDescription ="",tint=MaterialTheme.colorScheme.primary)
            Text(text = text, color = MaterialTheme.colorScheme.secondary)
        }
    }
}