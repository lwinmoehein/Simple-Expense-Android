package lab.justonebyte.simpleexpense.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import compose.icons.FeatherIcons
import compose.icons.feathericons.Award
import compose.icons.feathericons.Briefcase
import compose.icons.feathericons.Coffee
import compose.icons.feathericons.Cpu
import compose.icons.feathericons.Map
import compose.icons.feathericons.Menu
import compose.icons.feathericons.ShoppingCart
import compose.icons.feathericons.Smile
import lab.justonebyte.simpleexpense.R

@Composable
fun getIconFromName(name:String):ImageVector{
    return when(name){
        "food_and_drink"->FeatherIcons.Coffee
        "it_devices"->FeatherIcons.Cpu
        "shopping"->FeatherIcons.ShoppingCart
        "travel"->FeatherIcons.Map
        "life_and_entertainment"->FeatherIcons.Smile
        "salary"->FeatherIcons.Briefcase
        "bonus"->FeatherIcons.Award
        "allowance"->ImageVector.vectorResource(
            id = R.drawable.allowance
        )
        else -> FeatherIcons.Menu
    }
}