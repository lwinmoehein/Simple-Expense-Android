package lab.justonebyte.simpleexpense.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Info
import lab.justonebyte.simpleexpense.R

@Composable
fun NoData(modifier: Modifier=Modifier,text:String= stringResource(id = R.string.no_data)){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 150.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Column (
           horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Image(
                modifier = Modifier.width(180.dp),
                painter = painterResource(id = R.drawable.cat),
                contentDescription =""
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = text, color = MaterialTheme.colorScheme.secondary,style=MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}