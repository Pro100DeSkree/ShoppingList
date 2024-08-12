package com.deskree.shoppinglist.list_activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.deskree.shoppinglist.database.MainViewModel
import com.deskree.shoppinglist.database.entities.ShopListItem
import com.deskree.shoppinglist.database.entities.ShopListNameItem
import com.deskree.shoppinglist.list_activity.dialogs.InitClearDialog
import com.deskree.shoppinglist.list_activity.dialogs.InitCreateItemDialog
import com.deskree.shoppinglist.list_activity.list_screen.ListScreen
import com.deskree.shoppinglist.list_activity.top_app_bar.ListActTopAppBar
import com.deskree.shoppinglist.main_activity.MainActivity
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScaffold(
    list: ShopListNameItem,
    mainViewModel: MainViewModel,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val fabVisible = remember { mutableStateOf(true) }

    val isEmptyList = remember { mutableStateOf(true) }
    val isOpenCreateDialog = remember { mutableStateOf(false) }
    val isOpenClearDialog = remember { mutableStateOf(false) }
    val isEditDialog = mutableStateOf(ShopListItem(null, "", "", false, 1, 0))

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                coroutineScope.launch {
                    if (source == NestedScrollSource.Drag) {
                        if (available.y > 3) {
                            fabVisible.value = true
                        } else if (available.y < -3) {
                            fabVisible.value = false
                        }
                    }
                }
                return Offset.Zero
            }
        }
    }

    val alpha by animateFloatAsState(
        targetValue = if (fabVisible.value) 1f else 0f,
        animationSpec = tween(durationMillis = 200)
    )

    InitCreateItemDialog(isOpenCreateDialog, list, isEditDialog, mainViewModel)
    InitClearDialog(isOpenClearDialog, list, mainViewModel)

    Scaffold(
        topBar = {
            ListActTopAppBar(
                list,
                onClearList = {
                    if (!isEmptyList.value) {
                        isOpenClearDialog.value = true
                    }
                }
            )
        },
        floatingActionButton = {
            if (alpha != 0f) {
                FloatingActionButton(
                    onClick = { isOpenCreateDialog.value = true },
                    modifier = Modifier
                        .alpha(alpha)
                        .zIndex(1f)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = it.calculateBottomPadding(), top = it.calculateTopPadding()),
        ) {
            val listItems =
                mainViewModel.AllShopListItemsById(list.id!!).observeAsState(emptyList())
            if (listItems.value.isNotEmpty()) {
                ListScreen(
                    listItems,
                    nestedScrollConnection,
                    onClickDelete = { item ->
                        mainViewModel.deleteShopListItem(item)
                    },
                    onClickEdit = { item ->
                        isEditDialog.value = item
                        isOpenCreateDialog.value = true
                    },
                    onClickCheckBox = { item ->
                        mainViewModel.updateShopListItem(item)
                    }
                )
                isEmptyList.value = false
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Список пустий.\nНатисніть '+' щоб створити запис",
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    )
                }
                isEmptyList.value = true
            }
            saveItemCount(list, mainViewModel, listItems.value, context)
        }
    }
}

fun saveItemCount(
    list: ShopListNameItem,
    mainViewModel: MainViewModel,
    listItems: List<ShopListItem>,
    context: Context
) {
    val activity = context as? Activity
    var totalTasks = 0
    var tasksCompleted = 0

    listItems.forEach { item ->
        totalTasks++
        if (item.itemChecked) {
            tasksCompleted++
        }
    }
    mainViewModel.updateListName(
        list.copy(
            totalTasks = totalTasks,
            tasksCompleted = tasksCompleted
        )
    )
    activity?.intent?.putExtra(MainActivity.EDIT_STATE_KEY, "update_list")
    activity?.intent?.putExtra(
        ListActivity.LIST_KEY,
        list.copy(
            totalTasks = totalTasks,
            tasksCompleted = tasksCompleted
        )
    )
    activity?.setResult(RESULT_OK, activity.intent)
}
