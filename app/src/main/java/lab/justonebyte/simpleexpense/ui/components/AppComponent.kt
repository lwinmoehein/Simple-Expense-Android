package lab.justonebyte.simpleexpense.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

object AppComponent {

    @Composable
    fun Header(
        text: String,
        modifier: Modifier = Modifier,
    ) {
        Text(
            modifier = modifier
                .padding(
                    start = 16.dp,
                    top = 32.dp,
                    end = 16.dp,
                    bottom = 32.dp
                )
                .fillMaxWidth(),
            text = text,
            style = MaterialTheme.typography.h1,
            textAlign = TextAlign.Center,
        )
    }

    @Composable
    fun SubHeader(
        text: String,
        modifier: Modifier = Modifier,
    ) {
        Text(
            modifier = modifier
                .padding(
                    start = 16.dp,
                    top = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .fillMaxWidth(),
            text = text,
            style = MaterialTheme.typography.h2,
            textAlign = TextAlign.Center,
        )
    }

    @Composable
    fun MediumSpacer(
        modifier: Modifier = Modifier,
    ) {
        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .height(16.dp)
        )
    }

    @Composable
    fun BigSpacer(
        modifier: Modifier = Modifier,
    ) {
        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .height(32.dp)
        )
    }

    @Composable
    fun CustomListItem(
        text: String,
        modifier: Modifier = Modifier,
    ) {
        Text(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp, 4.dp)
                .shadow(2.dp, RoundedCornerShape(4.dp))
                .clip(RoundedCornerShape(4.dp))
                .clickable {
                    // do things here.
                }
                .background(MaterialTheme.colors.surface)
                .padding(16.dp, 8.dp),
            text = text,
            textAlign = TextAlign.Center,
        )
    }
}