package com.deskree.shoppinglist.list_activity.top_app_bar

import android.app.Activity
import android.app.Activity.RESULT_OK
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.deskree.shoppinglist.database.entities.ShopListNameItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListActTopAppBar(
    list: ShopListNameItem,
    onClearList: () -> Unit,
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text(text = list.name) },
        navigationIcon = {
            IconButton(
                onClick = {
                    val activity = context as? Activity
                    activity?.finish()
                },
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(
                onClick = { expanded = true }
            ) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Очистити список") },
                    onClick = { onClearList.invoke(); expanded = false },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "delete",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                )
            }
        }
    )
}