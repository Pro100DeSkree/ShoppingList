package com.deskree.shoppinglist.note_activity

import android.app.Activity
import android.app.Activity.RESULT_OK
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.deskree.shoppinglist.database.MainViewModel
import com.deskree.shoppinglist.database.entities.NoteItem
import com.deskree.shoppinglist.list_activity.top_app_bar.NoteActTopAppBar
import com.deskree.shoppinglist.main_activity.MainActivity
import com.deskree.shoppinglist.utils.TimeManager
import com.deskree.shoppinglist.utils.convertHtmlToAnnotatedString
import com.deskree.shoppinglist.utils.isHtmlString
import java.sql.Time


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    note: NoteItem,
) {
    val context = LocalContext.current

    val description = if (isHtmlString(note.content))
        convertHtmlToAnnotatedString(note.content).text
    else
        note.content
    val textNameState = remember { mutableStateOf(TextFieldValue(note.title)) }
    val textDescriptionState = remember { mutableStateOf(TextFieldValue(description)) }
    val isErrorTextNameField = remember { mutableStateOf(false) }
    val isErrorTextDescriptionField = remember { mutableStateOf(false) }

    val focusRequesterName = remember { FocusRequester() }

    Scaffold(
        topBar = {
            NoteActTopAppBar(
                note,
                onClickClearField = {
                    textNameState.value = TextFieldValue()
                    textDescriptionState.value = TextFieldValue()
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val activity = context as? Activity
                    if (note.id != null) {
                        activity?.intent?.putExtra(MainActivity.EDIT_STATE_KEY, "update_note")
                        activity?.intent?.putExtra(
                            NoteActivity.NOTE_KEY,
                            note.copy(
                                title = textNameState.value.text,
                                content = textDescriptionState.value.text,
                                changeDateTime = TimeManager.getCurrentTime()
                            )
                        )
                    } else {
                        activity?.intent?.putExtra(MainActivity.EDIT_STATE_KEY, "new_note")
                        activity?.intent?.putExtra(
                            NoteActivity.NOTE_KEY, NoteItem(
                                null,
                                textNameState.value.text,
                                textDescriptionState.value.text,
                                TimeManager.getCurrentTime(),
                                null,
                                ""
                            )
                        )
                    }
                    activity?.setResult(RESULT_OK, activity.intent)
                    activity?.finish()
                },
                modifier = Modifier
                    .alpha(1f)
                    .zIndex(1f)
            ) {
                Icon(imageVector = Icons.Default.Done, contentDescription = "Add")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = it.calculateBottomPadding(), top = it.calculateTopPadding()),
        ) {
            NoteUI(
                textNameState,
                textDescriptionState,
                isErrorTextNameField,
                isErrorTextDescriptionField,
                focusRequesterName,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteUI(
    textNameState: MutableState<TextFieldValue>,
    textDescriptionState: MutableState<TextFieldValue>,
    isErrorTextNameField: MutableState<Boolean>,
    isErrorTextDescriptionField: MutableState<Boolean>,
    focusRequesterName: FocusRequester
) {

    // Фокус
    val focusRequesterDescription = remember { FocusRequester() }

    val keyboardController = LocalSoftwareKeyboardController.current
    val windowInfo = LocalWindowInfo.current


    Column(

    ) {
        OutlinedTextField(
            label = { Text(text = "Назва") },
            /* TODO: Обмежити кількість символів*/
            value = textNameState.value,
            onValueChange = { isErrorTextNameField.value = false; textNameState.value = it },
            isError = isErrorTextNameField.value,
            singleLine = true,
            modifier = Modifier
                .focusRequester(focusRequesterName)
                .padding(10.dp, 2.dp)
                .fillMaxWidth(),
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
                isErrorTextDescriptionField.value = false; textDescriptionState.value = it
            },
            isError = isErrorTextDescriptionField.value,
            singleLine = false,
            modifier = Modifier
                .focusRequester(focusRequesterDescription)
                .padding(10.dp, 2.dp)
                .fillMaxSize(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
        )

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