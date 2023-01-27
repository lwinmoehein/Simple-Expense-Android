package lab.justonebyte.moneysubuu.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import lab.justonebyte.moneysubuu.R
import lab.justonebyte.moneysubuu.ui.theme.MoneySuBuuTheme


@Composable
fun AppDrawer(
    currentRoute: String,
    navigateToHome: () -> Unit,
    navigateToSettings: () -> Unit,
    navigateToStats:()->Unit,
    navigateToCategory:()->Unit,
    closeDrawer: () -> Unit
) {

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(Modifier.height(24.dp))
        JetNewsLogo(Modifier.padding(16.dp))
        Divider(color = MaterialTheme.colors.onSurface.copy(alpha = .2f))
        DrawerButton(
            icon = {
                Image(
                    painterResource(id = R.drawable.ic_round_home_24),
                    contentDescription = null,
                )
            },
            label = stringResource(id = R.string.home),
            isSelected = currentRoute == MainDestinations.HOME_ROUTE,
            action = {
                navigateToHome()
                closeDrawer()
            }
        )
        DrawerButton(
            icon = {
                Image(
                    painterResource(id = R.drawable.ic_round_pie_chart_24),
                    contentDescription = null,
                )
            },
            label = stringResource(id = R.string.charts),
            isSelected = currentRoute == MainDestinations.STATS_ROUTE,
            action = {
                navigateToStats()
                closeDrawer()
            }
        )
        DrawerButton(
            icon = {
                Image(
                    painterResource(id = R.drawable.ic_round_category_24),
                    contentDescription = null,
                )
            },
            label = stringResource(id = R.string.m_categories),
            isSelected = currentRoute == MainDestinations.CATEGORY_ROUTE,
            action = {
                navigateToCategory()
                closeDrawer()
            }
        )
        DrawerButton(
            icon = {
                Image(
                    painterResource(id = R.drawable.ic_round_settings_24),
                    contentDescription = null,
                )
            },
            label = stringResource(id = R.string.settings),
            isSelected = currentRoute == MainDestinations.SETTINGS_ROUTE,
            action = {
                navigateToSettings()
                closeDrawer()
            }
        )

    }
}

@Composable
private fun JetNewsLogo(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = null, // decorative
            colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
        )
        Spacer(Modifier.width(8.dp))
        Image(
            painter = painterResource(R.drawable.ic_launcher_background),
            contentDescription = stringResource(R.string.app_name),
            colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface)
        )
    }
}

@Composable
private fun DrawerButton(
    icon: @Composable () -> Unit,
    label: String,
    isSelected: Boolean,
    action: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colors
    val imageAlpha = if (isSelected) {
        1f
    } else {
        0.6f
    }
    val textIconColor = if (isSelected) {
        colors.primary
    } else {
        colors.onSurface.copy(alpha = 0.6f)
    }
    val backgroundColor = if (isSelected) {
        colors.primary.copy(alpha = 0.12f)
    } else {
        Color.Transparent
    }

    val surfaceModifier = modifier
        .padding(start = 8.dp, top = 8.dp, end = 8.dp)
        .fillMaxWidth()
    Surface(
        modifier = surfaceModifier,
        color = backgroundColor,
        shape = MaterialTheme.shapes.small
    ) {
        TextButton(
            onClick = action,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                icon()
                Spacer(Modifier.width(16.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.button,
                    color = textIconColor
                )
            }
        }
    }
}
