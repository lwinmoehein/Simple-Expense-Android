package lab.justonebyte.simpleexpense.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import compose.icons.FeatherIcons
import compose.icons.feathericons.Activity
import compose.icons.feathericons.Award
import compose.icons.feathericons.Briefcase
import compose.icons.feathericons.Coffee
import compose.icons.feathericons.Cpu
import compose.icons.feathericons.DollarSign
import compose.icons.feathericons.Home
import compose.icons.feathericons.Map
import compose.icons.feathericons.Menu
import compose.icons.feathericons.MoreVertical
import compose.icons.feathericons.ShoppingCart
import compose.icons.feathericons.Smile
import compose.icons.feathericons.Truck
import lab.justonebyte.simpleexpense.R

@Composable
fun getIconFromName(name:String):ImageVector{
    return when(name){
        "food_and_drink"->FeatherIcons.Coffee
        "it_devices"->FeatherIcons.Cpu
        "shopping"->FeatherIcons.ShoppingCart
        "housing"->FeatherIcons.Home
        "travel"->FeatherIcons.Map
        "life_and_entertainment"->FeatherIcons.Smile
        "vehicles"->FeatherIcons.Truck
        "salary"->FeatherIcons.Briefcase
        "health"->FeatherIcons.Activity
        "bonus"->FeatherIcons.Award
        "donation"->FeatherIcons.DollarSign
//        "allowance"->ImageVector.vectorResource(
//            id = R.drawable.ic_launcher_transparent
//        )
        else -> FeatherIcons.MoreVertical
    }
}