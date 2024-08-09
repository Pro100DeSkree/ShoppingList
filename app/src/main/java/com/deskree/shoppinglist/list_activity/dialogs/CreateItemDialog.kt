package com.deskree.shoppinglist.list_activity.dialogs

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
import com.deskree.shoppinglist.database.entities.ShopListItem
import com.deskree.shoppinglist.database.entities.ShopListNameItem

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateItemDialogUI(
    isDialogOpen: MutableState<Boolean>,
    list: ShopListNameItem,
    isEditDialog: MutableState<ShopListItem>,
    saveNew: (ShopListItem) -> Unit,
    saveEdit: (ShopListItem) -> Unit,
) {
    val textNameState = remember { mutableStateOf(TextFieldValue()) }
    val textDescriptionState = remember { mutableStateOf(TextFieldValue()) }
    val isErrorTextNameField = remember { mutableStateOf(false) }
    // Фокус
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequesterName = remember { FocusRequester() }
    val focusRequesterDescription = remember { FocusRequester() }
    val windowInfo = LocalWindowInfo.current

    textNameState.value = TextFieldValue(text = isEditDialog.value.name.ifEmpty { "" })
    textDescriptionState.value = TextFieldValue(text = isEditDialog.value.itemInfo.ifEmpty { "" })

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

            OutlinedTextField(
                label = { Text(text = "Назва") },
                /* TODO: Обмежити кількість символів*/
                /* TODO: Додати випадаючий список з підказками*/
                value = textNameState.value,
                onValueChange = { isErrorTextNameField.value = false; textNameState.value = it },
                isError = isErrorTextNameField.value,
                singleLine = true,
                modifier = Modifier.focusRequester(focusRequesterName).padding(10.dp, 2.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = {
                    isErrorTextNameField.value = textNameState.value.text.trim().isEmpty()
                    if (!isErrorTextNameField.value) {
                        focusRequesterDescription.requestFocus()
                    }
                })
            )
            OutlinedTextField(
                label = { Text(text = "Опис") },
                value = textDescriptionState.value,
                onValueChange = {
                    textDescriptionState.value = it
                },
                singleLine = true,
                modifier = Modifier.focusRequester(focusRequesterDescription).padding(10.dp, 2.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    isErrorTextNameField.value = textNameState.value.text.trim().isEmpty()
                    if (!isErrorTextNameField.value) {
                        if (isEditDialog.value.name.isNotEmpty()) {
                            saveEdit.invoke(
                                isEditDialog.value.copy(
                                    name = textNameState.value.text,
                                    itemInfo = textDescriptionState.value.text
                                )
                            )
                        } else {
                            saveNew.invoke(
                                ShopListItem(
                                    null,
                                    textNameState.value.text,
                                    textDescriptionState.value.text,
                                    false,
                                    list.id!!,
                                    0
                                )
                            )
                        }
                        clearEditState(isEditDialog)
                        isDialogOpen.value = false
                    }
                })
            )
            Row {
                TextButton(onClick = {
                    clearEditState(
                        isEditDialog
                    ); isDialogOpen.value = false
                }) {
                    Text(text = "Скасувати")
                }
                TextButton(onClick = {
                    isErrorTextNameField.value = textNameState.value.text.trim().isEmpty()
                    if (!isErrorTextNameField.value) {
                        if (isEditDialog.value.name.isNotEmpty()) {
                            saveEdit.invoke(
                                isEditDialog.value.copy(
                                    name = textNameState.value.text,
                                    itemInfo = textDescriptionState.value.text
                                )
                            )
                        } else {
                            saveNew.invoke(
                                ShopListItem(
                                    null,
                                    textNameState.value.text,
                                    textDescriptionState.value.text,
                                    false,
                                    list.id!!,
                                    0
                                )
                            )
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
                        focusRequesterName.requestFocus()
                        textNameState.value = TextFieldValue(
                            text = textNameState.value.text,
                            selection = TextRange(textNameState.value.text.length)
                        )
                        textDescriptionState.value = TextFieldValue(
                            text = textDescriptionState.value.text,
                            selection = TextRange(textDescriptionState.value.text.length)
                        )
                        keyboardController?.show()
                    }
                }
            }
        }
    }
}

fun clearEditState(isEdit: MutableState<ShopListItem>) {
    isEdit.value = ShopListItem(null, "", "", false, 0, 0)
}