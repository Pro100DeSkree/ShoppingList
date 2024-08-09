package com.deskree.shoppinglist.main_activity.bottom_nav

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.deskree.shoppinglist.database.MainViewModel
import com.deskree.shoppinglist.database.entities.NoteItem
import com.deskree.shoppinglist.database.entities.ShopListNameItem
import com.deskree.shoppinglist.list_activity.ListActivity
import com.deskree.shoppinglist.list_activity.top_app_bar.NoteActTopAppBar
import com.deskree.shoppinglist.main_activity.dialogs.InitDeleteListDialog
import com.deskree.shoppinglist.main_activity.dialogs.InitDeleteNoteDialog
import com.deskree.shoppinglist.main_activity.routes.list.ListsScreen
import com.deskree.shoppinglist.main_activity.routes.note.NoteItem
import com.deskree.shoppinglist.main_activity.routes.note.NoteScreen
import com.deskree.shoppinglist.main_activity.routes.settings.PreferenceScreen
import com.deskree.shoppinglist.main_activity.routes.settings.Settings
import com.deskree.shoppinglist.note_activity.NoteActivity
import com.deskree.shoppinglist.utils.TimeManager
import kotlinx.coroutines.delay

@SuppressLint("UnrememberedMutableState")
@Composable
fun NavGraph(
    navHostController: NavHostController,
    nestedScrollConnection: NestedScrollConnection,
    isEditDialog: MutableState<ShopListNameItem>,
    isDialogOpen: MutableState<Boolean>,
    mainViewModel: MainViewModel,
    inentLauncher: ActivityResultLauncher<Intent>,
    settings: Settings
) {
    val context = LocalContext.current
    val defaultScreen = NavItems.ListsScreen.route

    NavHost(navController = navHostController, startDestination = defaultScreen) {
        composable(NavItems.ListsScreen.route) {

            val allLists = mainViewModel.getAllShopListNames().collectAsState(emptyList())
//            val allLists by mainViewModel.allLists.observeAsState(emptyList())

            val isOpenDeleteDialog = remember { mutableStateOf(false) }
            val list = mutableStateOf(ShopListNameItem(null, "", "", 0, 0, ""))

            InitDeleteListDialog(isOpenDeleteDialog, list, mainViewModel)

            if (allLists.value.isNotEmpty()) {
                ListsScreen(
                    nestedScrollConnection,
                    allLists.value,
                    onClickItem = { item ->
                        inentLauncher.launch(
                            Intent(context, ListActivity::class.java)
                                .putExtra(IT_LIST, item)
                        )
                    },
                    onClickEdit = { item ->
                        isEditDialog.value = item
                        isDialogOpen.value = true
                    },
                    onClickDelete = { item ->
                        list.value = item
                        isOpenDeleteDialog.value = true
                    }
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Списків ще не створено.\nНатисніть '+' щоб створити новий список",
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    )
                }
            }
        }

        composable(NavItems.NotesScreen.route) {
//            val allNotes = mainViewModel.getAllNotes().collectAsState(emptyList())
            val allNotes by mainViewModel.allNotes.observeAsState(emptyList())
            val isOpenDeleteDialog = remember { mutableStateOf(false) }
            val note = remember { mutableStateOf(NoteItem(null, "", "", "", null, "")) }

            InitDeleteNoteDialog(isOpenDeleteDialog, note, mainViewModel)
            if (allNotes.isNotEmpty()) {
                NoteScreen(
                    nestedScrollConnection, allNotes,
                    onClickItem = { item ->
                        inentLauncher.launch(
                            Intent(context, NoteActivity::class.java).putExtra(
                                NoteActivity.NOTE_KEY,
                                item
                            )
                        )
                    },
                    onClickDelete = { item ->
                        note.value = item
                        isOpenDeleteDialog.value = true
                    }
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Нотаток ще не створено.\nНатисніть '+' щоб створити нову нотатку",
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    )
                }
            }
        }

        composable(NavItems.PreferenceScreen.route) {
            PreferenceScreen(settings)
        }
    }
}

const val IT_LIST = "it_list"
