package com.deskree.shoppinglist.list_activity.list_screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.Lifecycle
import com.deskree.shoppinglist.database.MainViewModel
import com.deskree.shoppinglist.database.entities.ShopListItem
import com.deskree.shoppinglist.database.entities.ShopListNameItem


@Composable
fun ListScreen(
    allItems: State<List<ShopListItem>>,
    nestedScrollConnection: NestedScrollConnection,
    onClickDelete: (ShopListItem) -> Unit,
    onClickEdit: (ShopListItem) -> Unit,
    onClickCheckBox: (ShopListItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        itemsIndexed(allItems.value) { _, listItem ->
            ListItem(
                listItem,
                onClickDelete = { item ->
                    onClickDelete.invoke(item)
                },
                onClickEdit = { item ->
                    onClickEdit.invoke(item)
                },
                onClickCheckBox = { item ->
                    onClickCheckBox.invoke(item)
                }
            )
        }
    }
}