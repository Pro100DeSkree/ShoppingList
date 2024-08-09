package com.deskree.shoppinglist.list_activity.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.window.Dialog
import com.deskree.shoppinglist.database.MainViewModel
import com.deskree.shoppinglist.database.entities.ShopListItem
import com.deskree.shoppinglist.database.entities.ShopListNameItem

@Composable
fun InitCreateItemDialog(
    isDialogOpen: MutableState<Boolean>,
    list: ShopListNameItem,
    isEditDialog: MutableState<ShopListItem>,
    mainViewModel: MainViewModel,
) {
    if (isDialogOpen.value) {
        Dialog(onDismissRequest = { isDialogOpen.value = false }) {
            CreateItemDialogUI(
                isDialogOpen, list, isEditDialog,
                saveNew = { item ->
                    mainViewModel.insertItem(item)
                },
                saveEdit = { item ->
                    mainViewModel.updateShopListItem(item)
                }
            )
        }
    }
}

@Composable
fun InitClearDialog(
    isClearDialogOpen: MutableState<Boolean>,
    list: ShopListNameItem,
    mainViewModel: MainViewModel,
) {
    if (isClearDialogOpen.value) {
        Dialog(onDismissRequest = { isClearDialogOpen.value = false }) {
            ClearDialogUI(
                isClearDialogOpen, list,
                clearList = { item ->
                    mainViewModel.deleteShopListItemsByListID(item.id!!)
                }
            )
        }
    }
}