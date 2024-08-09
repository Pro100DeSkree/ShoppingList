package com.deskree.shoppinglist.main_activity.top_app_bar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.deskree.shoppinglist.R
import com.deskree.shoppinglist.main_activity.bottom_nav.NavItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarCast(
    currentFrag: MutableState<NavItems>
){
    val context = LocalContext.current
    TopAppBar(
        title = { Text(currentFrag.value.title) },
    )
}