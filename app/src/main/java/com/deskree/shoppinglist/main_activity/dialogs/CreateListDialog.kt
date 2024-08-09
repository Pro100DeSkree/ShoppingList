package com.deskree.shoppinglist.main_activity.dialogs

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.deskree.shoppinglist.database.entities.ShopListNameItem

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateListDialogUI(
    isDialogOpen: MutableState<Boolean>,
    isEditDialog: MutableState<ShopListNameItem>,
    saveNew: (String) -> Unit,
    saveEdit: (String) -> Unit,
) {
    val textState = remember { mutableStateOf(TextFieldValue()) }
    val isErrorTextField = remember { mutableStateOf(false) }
    // Фокус
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val windowInfo = LocalWindowInfo.current

    textState.value = TextFieldValue(text = isEditDialog.value.name.ifEmpty { "" })

    Surface(
        shape = RoundedCornerShape(10.dp),
    ) {

        Column(
            modifier = Modifier.padding(6.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "${if (isEditDialog.value.name.isEmpty()) "Додати" else "Редагувати"} список",
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = FontWeight.ExtraBold,
            )

            Spacer(modifier = Modifier.padding(vertical = 4.dp))

            OutlinedTextField(label = { Text(text = "Назва списку") },
                /* TODO: Обмежити кількість символів*/
                value = textState.value,
                onValueChange = { isErrorTextField.value = false; textState.value = it },
                isError = isErrorTextField.value,
                singleLine = true,
                modifier = Modifier.focusRequester(focusRequester).padding(10.dp, 2.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    isErrorTextField.value = textState.value.text.trim().isEmpty()
                    if (!isErrorTextField.value) {
                        saveNew.invoke(textState.value.text)
                        clearEditState(isEditDialog)
                        isDialogOpen.value = false
                    }
                })
            )

            Row {
                TextButton(onClick = { clearEditState(isEditDialog); isDialogOpen.value = false }) {
                    Text(text = "Скасувати")
                }
                TextButton(onClick = {

                    if (textState.value.text.trim().isEmpty()) {
                        isErrorTextField.value = true
                    } else {
                        if (isEditDialog.value.name.isNotEmpty()) {
                            saveEdit.invoke(textState.value.text.trim())
                        } else {
                            saveNew.invoke(textState.value.text.trim())
                        }
                        clearEditState(isEditDialog)
                        isDialogOpen.value = false
                    }
                }) {
                    Text(text = if (isEditDialog.value.name.isEmpty()) "Створити" else "Зберегти")
                }
            }

            // Фокусування та відкриття клавіатури
            LaunchedEffect(true) {
                snapshotFlow { windowInfo.isWindowFocused }.collect { isWindowFocused ->
                    if (isWindowFocused) {
                        focusRequester.requestFocus()
                        textState.value = TextFieldValue(
                            text = textState.value.text,
                            selection = TextRange(textState.value.text.length)
                        )
                        keyboardController?.show()
                    }
                }
            }
        }
    }
}

fun clearEditState(isEdit: MutableState<ShopListNameItem>) {
    isEdit.value = ShopListNameItem(null, "", "", 0, 0, "")
}
