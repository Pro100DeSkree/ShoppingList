package com.deskree.shoppinglist.main_activity.dialogs

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deskree.shoppinglist.database.entities.NoteItem
import com.deskree.shoppinglist.database.entities.ShopListNameItem

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteNoteDialogUI(
    isDeleteDialogOpen: MutableState<Boolean>,
    note: MutableState<NoteItem>,
    deleteList: (NoteItem) -> Unit,
) {

    Surface(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(6.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Видалити",
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = FontWeight.ExtraBold,
            )

            Spacer(modifier = Modifier.padding(vertical = 4.dp))

            Text(text = "Ви дійсно бажаєте видалити нотатку?")

            Row {
                TextButton(onClick = {
                    isDeleteDialogOpen.value = false
                }) {
                    Text(text = "Скасувати")
                }
                TextButton(
                    onClick = {
                        deleteList.invoke(note.value)
                        isDeleteDialogOpen.value = false
                    }
                ) {
                    Text(text = "Видалити")
                }
            }
        }
    }
}