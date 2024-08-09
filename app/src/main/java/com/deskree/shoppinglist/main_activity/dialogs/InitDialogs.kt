package com.deskree.shoppinglist.main_activity.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.window.Dialog
import com.deskree.shoppinglist.database.MainViewModel
import com.deskree.shoppinglist.database.entities.NoteItem
import com.deskree.shoppinglist.database.entities.ShopListNameItem
import com.deskree.shoppinglist.utils.TimeManager


@Composable
fun InitCreateListDialog(
    isDialogOpen: MutableState<Boolean>,
    isEditDialog: MutableState<ShopListNameItem>,
    mainViewModel: MainViewModel,
) {
    if (isDialogOpen.value) {
        Dialog(onDismissRequest = { isDialogOpen.value = false }) {
            CreateListDialogUI(isDialogOpen = isDialogOpen,
                isEditDialog = isEditDialog,
                saveNew = { listName ->
                    mainViewModel.insertShopListName(
                        ShopListNameItem(
                            null, listName, TimeManager.getCurrentTime(), 0, 0, ""
                        )
                    )
                },
                saveEdit = { listName ->
                    mainViewModel.updateListName(
                        isEditDialog.value.copy(name = listName)
                    )
                }
            )
        }
    }
}

@Composable
fun InitDeleteListDialog(
    isDeleteDialogOpen: MutableState<Boolean>,
    list: MutableState<ShopListNameItem>,
    mainViewModel: MainViewModel,
) {
    if (isDeleteDialogOpen.value) {
        Dialog(onDismissRequest = { isDeleteDialogOpen.value = false }) {
            DeleteListDialogUI(
                isDeleteDialogOpen, list,
                deleteList = { item ->
                    mainViewModel.deleteShopListItemsByListID(item.id!!)
                    mainViewModel.deleteShopListNameItem(item)
                }
            )
        }
    }
}

@Composable
fun InitDeleteNoteDialog(
    isDeleteDialogOpen: MutableState<Boolean>,
    note: MutableState<NoteItem>,
    mainViewModel: MainViewModel,
) {
    if (isDeleteDialogOpen.value) {
        Dialog(onDismissRequest = { isDeleteDialogOpen.value = false }) {
            DeleteNoteDialogUI(
                isDeleteDialogOpen, note,
                deleteList = { note ->
                    mainViewModel.deleteNote(note)
                }
            )
        }
    }
}