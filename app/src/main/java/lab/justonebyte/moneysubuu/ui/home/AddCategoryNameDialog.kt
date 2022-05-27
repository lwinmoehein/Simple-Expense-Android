package lab.justonebyte.moneysubuu.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import lab.justonebyte.moneysubuu.ui.theme.GrayCardBg
import lab.justonebyte.moneysubuu.ui.theme.Red900

@Composable
fun AddCategoryNameDialog(
    modifier:Modifier = Modifier,
    isDialogShown:Boolean = false,
    name:String = "",
    onConfirm:(name:String)->Unit,
    onCloseDialog:()->Unit
){
    val addCategoryName = remember { mutableStateOf(name)}
    val isCategoryNameErrorShown = remember(addCategoryName.value,isDialogShown) { mutableStateOf(false) }

    if(isDialogShown){
        Dialog(onDismissRequest = {onCloseDialog()}) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .wrapContentSize()
                    .clip(shape = RoundedCornerShape(5.dp))
                    .background(MaterialTheme.colors.onPrimary)
                    .absolutePadding(top = 10.dp, bottom = 10.dp, right = 10.dp, left = 10.dp)

            ) {
                Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.wrapContentWidth()) {
                    Text(text = "Add New Category",style = MaterialTheme.typography.subtitle1, textAlign = TextAlign.Left)
                }
                Spacer(modifier = Modifier.height(10.dp))
                Spacer(modifier = Modifier.fillMaxWidth().height(2.dp).background(MaterialTheme.colors.primary))
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,

                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        text = "Category Name : ",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.subtitle2
                    )
                    BasicTextField(
                        modifier = modifier
                            .weight(1f)
                            .background(
                                MaterialTheme.colors.surface,
                                MaterialTheme.shapes.small,
                            )
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, MaterialTheme.colors.primary)
                            .padding(10.dp),
                        value = addCategoryName.value,
                        onValueChange = {
                            addCategoryName.value = it
                        },
                        singleLine = true,
                        cursorBrush = SolidColor(MaterialTheme.colors.primary),
                        textStyle = LocalTextStyle.current.copy(
                            color = MaterialTheme.colors.onSurface,
                            fontSize = MaterialTheme.typography.subtitle1.fontSize
                        ),
                        decorationBox = { innerTextField ->
                            Row(
                                modifier,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Box(Modifier.weight(1f)) {
                                    if (addCategoryName.value.isEmpty())
                                        Text(
                                            text = if(isCategoryNameErrorShown.value) "Invalid name" else "Enter name",
                                            style = LocalTextStyle.current.copy(
                                                color = if(isCategoryNameErrorShown.value) Red900 else  MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
                                                fontSize =  MaterialTheme.typography.subtitle1.fontSize
                                            )
                                        )
                                    innerTextField()
                                }
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                        TextButton(
                            onClick = { onCloseDialog() },
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(5.dp))
                        ) {
                            Text(text = "Cancel",color = GrayCardBg)
                        }
                        TextButton(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(5.dp))
                            ,
                            onClick = {
                                if(addCategoryName.value.isEmpty()){
                                    isCategoryNameErrorShown.value = true
                                }else{
                                    onConfirm(addCategoryName.value)
                                    addCategoryName.value = ""
                                    isCategoryNameErrorShown.value = false
                                }
                            }
                        ) {
                            Text(text = "Confirm",style= MaterialTheme.typography.button)
                        }
                }
            }
        }
    }
}