package lab.justonebyte.moneysubuu.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.insets.LocalWindowInsets
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val ELEMENT_HEIGHT = 35.dp

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalComposeUiApi
@Composable
fun TextInputFieldOne(
    modifier: Modifier = Modifier,
    textFieldValue: MutableState<TextFieldValue>,
    background: Color = Color.Gray,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    imeAction: ImeAction? = null,
    keyboardActions: KeyboardActions? = null,
    fontSize: TextUnit = 16.sp,
    height: Dp = ELEMENT_HEIGHT,
    isError: Boolean = false,
    onValueChange: (TextFieldValue) -> Unit = {},
    textColor:Color = MaterialTheme.colors.primary,
    hideKeyboard:Boolean = false
) {
    val focusManager = LocalFocusManager.current
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val interactionSource = remember { MutableInteractionSource() }
    val interactionSourceState = interactionSource.collectIsFocusedAsState()
    val scope = rememberCoroutineScope()
    val ime = LocalWindowInsets.current.ime
    val keyboardController = LocalSoftwareKeyboardController.current

    // Bring the composable into view (visible to user).
    LaunchedEffect(ime.isVisible, interactionSourceState.value) {
        if (ime.isVisible && interactionSourceState.value) {
            scope.launch {
                delay(300)
                bringIntoViewRequester.bringIntoView()
            }
        }
    }
    if(hideKeyboard){
        focusManager.clearFocus()
        keyboardController?.hide()
    }

    BasicTextField(
        value = textFieldValue.value,
        singleLine = singleLine,
        textStyle = TextStyle(
            fontFamily = MaterialTheme.typography.body1.fontFamily,
            fontSize = fontSize,
            color = textColor,
        ),
        onValueChange = {
            textFieldValue.value = it
            onValueChange(it)
        },
        keyboardActions = keyboardActions ?: KeyboardActions(
            onDone = { focusManager.clearFocus() },
            onNext = { focusManager.moveFocus(FocusDirection.Down) },
            onSearch = {
                focusManager.clearFocus()
                keyboardController?.hide()
            }
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction ?: if (singleLine) ImeAction.Done else ImeAction.Default
        ),
        interactionSource = interactionSource,
        modifier = modifier
            .bringIntoViewRequester(bringIntoViewRequester)
            .fillMaxWidth(),
        readOnly = readOnly,
        decorationBox = { innerTextField ->
            Box(
                Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .background(if (isError) MaterialTheme.colors.error else background)
                    .height(height)
                    .padding(horizontal = 12.dp),
                contentAlignment = if (singleLine) Alignment.CenterStart else Alignment.TopStart,
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            top = if (singleLine) 0.dp else 12.5.dp,
                            bottom = if (singleLine) 2.5.dp else 12.5.dp
                        )
                ) {
                    innerTextField()

                    if (textFieldValue.value.text.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = textColor.copy(.35f),
                            fontSize = fontSize,
                            maxLines = if (singleLine) 1 else Int.MAX_VALUE,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    )
}