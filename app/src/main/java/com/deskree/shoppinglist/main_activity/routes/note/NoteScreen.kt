package com.deskree.shoppinglist.main_activity.routes.note

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.deskree.shoppinglist.database.entities.NoteItem

@Composable
fun NoteScreen(
    nestedScrollConnection: NestedScrollConnection,
    allNotes: List<NoteItem>,
    onClickDelete: (NoteItem) -> Unit,
    onClickItem: (NoteItem) -> Unit,
    ) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        itemsIndexed(allNotes) { _, note ->
            NoteItem(
                note,
                onClickItem = { onClickItem(it) },
                onClickDelete = { onClickDelete(it)}
            )
        }
    }
}