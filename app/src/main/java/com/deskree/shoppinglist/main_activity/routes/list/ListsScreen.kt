package com.deskree.shoppinglist.main_activity.routes.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.deskree.shoppinglist.database.entities.ShopListNameItem

@Composable
fun ListsScreen(
    nestedScrollConnection: NestedScrollConnection,
    allLists: List<ShopListNameItem>,
    onClickItem: (ShopListNameItem) -> Unit,
    onClickEdit: (ShopListNameItem) -> Unit,
    onClickDelete: (ShopListNameItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        itemsIndexed(allLists) { _, listItem ->
            ListItem(
                listItem,
                onClickItem = { onClickItem(it) },
                onClickEdit = { onClickEdit(it) },
                onClickDelete = { onClickDelete(it) }
            )
        }
    }
}
