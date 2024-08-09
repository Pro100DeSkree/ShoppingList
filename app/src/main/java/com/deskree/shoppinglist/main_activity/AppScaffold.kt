package com.deskree.shoppinglist.main_activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.zIndex
import androidx.navigation.compose.rememberNavController
import com.deskree.shoppinglist.database.MainViewModel
import com.deskree.shoppinglist.database.entities.NoteItem
import com.deskree.shoppinglist.database.entities.ShopListNameItem
import com.deskree.shoppinglist.main_activity.bottom_nav.BottomNav
import com.deskree.shoppinglist.main_activity.bottom_nav.NavGraph
import com.deskree.shoppinglist.main_activity.bottom_nav.NavItems
import com.deskree.shoppinglist.main_activity.dialogs.InitCreateListDialog
import com.deskree.shoppinglist.main_activity.routes.settings.Settings
import com.deskree.shoppinglist.main_activity.top_app_bar.TopAppBarCast
import com.deskree.shoppinglist.note_activity.NoteActivity
import kotlinx.coroutines.launch


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    mainViewModel: MainViewModel,
    inentLauncher: ActivityResultLauncher<Intent>,
    settings: Settings,
) {
    val navController = rememberNavController()
    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()
    val fabVisible = remember { mutableStateOf(true) }
    val currentFrag = remember { mutableStateOf<NavItems>(NavItems.ListsScreen) }

    val isOpenDialog = remember { mutableStateOf(false) }
    val isEditDialog = mutableStateOf(ShopListNameItem(null, "", "", 0, 0, ""))


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

    HideOrShowFloatingBtn(fabVisible, currentFrag)
    InitCreateListDialog(
        isDialogOpen = isOpenDialog,
        isEditDialog = isEditDialog,
        mainViewModel = mainViewModel
    )

    val alpha by animateFloatAsState(
        targetValue = if (fabVisible.value) 1f else 0f,
        animationSpec = tween(durationMillis = 200)
    )

    Scaffold(
        bottomBar = {
            BottomNav(navController = navController) { navItem ->
                currentFrag.value = navItem
            }
        },
        topBar = { TopAppBarCast(currentFrag) },
        floatingActionButton = {

            FloatingActionButton(
                onClick = {
                    if (currentFrag.value == NavItems.ListsScreen)
                        isOpenDialog.value = true
                    else if (currentFrag.value == NavItems.NotesScreen)
                        inentLauncher.launch(Intent(context, NoteActivity::class.java))
                },
                modifier = Modifier
                    .alpha(alpha)
                    .zIndex(1f)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        },
        floatingActionButtonPosition = FabPosition.End,

        ) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = it.calculateBottomPadding(), top = it.calculateTopPadding()),
        ) {
            NavGraph(
                navHostController = navController,
                nestedScrollConnection,
                isEditDialog,
                isOpenDialog,
                mainViewModel,
                inentLauncher,
                settings
            )
        }
    }
}

@Composable
fun HideOrShowFloatingBtn(
    fabVisible: MutableState<Boolean>,
    currentFrag: MutableState<NavItems>
) {
    if (currentFrag.value.route == NavItems.PreferenceScreen.route) {
        fabVisible.value = false

    } else {
        fabVisible.value = true
    }
}
