package lab.justonebyte.moneysubuu.ui.home

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import lab.justonebyte.moneysubuu.ui.theme.Red900

@Composable
fun AddCategoryNameDialog(
    modifier:Modifier = Modifier,
    isShown:Boolean = false,
    name:String = "",
    onConfirm:(name:String)->Unit
){
    val isDialogShown = remember { mutableStateOf(isShown)}
    val addCategoryName = remember { mutableStateOf(name)}
    val isCategoryNameErrorShown = remember(addCategoryName.value,isDialogShown.value) { mutableStateOf(false) }

    if(isDialogShown.value){
        Dialog(onDismissRequest = {isDialogShown.value = false}) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(5.dp))
                    .background(MaterialTheme.colors.onPrimary)
                    .absolutePadding(top = 5.dp, bottom = 5.dp, right = 10.dp, left = 10.dp)

            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(4f)
                        .fillMaxWidth()
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        text = "Category Name : ",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.subtitle2
                    )
                    BasicTextField(modifier = modifier
                        .weight(1f)
                        .background(
                            MaterialTheme.colors.surface,
                            MaterialTheme.shapes.small,
                        )
                        .fillMaxWidth(),
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
                Row(
                    Modifier
                        .weight(2f)
                        .fillMaxWidth()
                ) {
                    Row(modifier = Modifier.wrapContentHeight()) {
                        TextButton(
                            onClick = { isDialogShown.value = false },
                            modifier = Modifier.weight(1f).absolutePadding(right = 10.dp)
                        ) {
                            Text(text = "Cancel")
                        }
                        TextButton(
                            modifier = Modifier
                                .weight(1f)
                                .absolutePadding(left = 20.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(MaterialTheme.colors.primary.copy(alpha = 1f)),
                            onClick = {
                                if(addCategoryName.value.isEmpty()){
                                    isCategoryNameErrorShown.value = true
                                }else{
                                    onConfirm(addCategoryName.value)
                                    isDialogShown.value = false
                                    addCategoryName.value = ""
                                    isCategoryNameErrorShown.value = false
                                }
                            }
                        ) {
                            Text(text = "Confirm",color = MaterialTheme.colors.onPrimary,style= MaterialTheme.typography.button)
                        }
                    }
                }
            }
        }
    }
}