package lab.justonebyte.moneysubuu.ui.category

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import lab.justonebyte.moneysubuu.ui.theme.primary

@Composable
fun AddNameInputDialog(
    isShown:Boolean = false,
    initialValue:String = "",
    title:String = "Enter :",
    dialogColor:Color = primary,
    onDialogDismiss:()->Unit,
    onConfirmClick:(name:String)->Unit
){
    val nameInputValue = remember { mutableStateOf(initialValue) }
    val focusRequester = remember() {
        FocusRequester()
    }
    if(isShown){
        Dialog(onDismissRequest = {onDialogDismiss()}) {
            Column(
                Modifier
                    .padding(10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colors.onPrimary)
                    .padding(20.dp)

            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold,
                    color =  dialogColor
                )
                Divider(Modifier.height(10.dp), color = Color.Transparent)
                BasicTextField(
                    value = nameInputValue.value,
                    onValueChange = {
                        nameInputValue.value = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 40.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .border(2.dp,dialogColor).absolutePadding(left = 10.dp, right = 10.dp, top = 5.dp, bottom = 5.dp),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.subtitle1,
                )

                Divider(Modifier.height(10.dp), color = Color.Transparent)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            nameInputValue.value = ""
                            onDialogDismiss()
                        }
                    ) {
                        Text(text = "Cancel", color = Color.Gray)
                    }
                    Divider(modifier = Modifier.width(10.dp),color= Color.Transparent)
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = dialogColor, contentColor = MaterialTheme.colors.onPrimary),
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp)),
                        onClick = {
                            onConfirmClick(nameInputValue.value)
                            nameInputValue.value = ""
                        }
                    ) {
                        Text(text = "Add")
                    }
                }
            }
        }
    }
}