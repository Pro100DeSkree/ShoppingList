package com.deskree.shoppinglist.main_activity.bottom_nav

import com.deskree.shoppinglist.R

sealed class NavItems(val title: String, val iconID: Int, val route: String) {
    object ListsScreen: NavItems("Списки", R.drawable.ic_shopping_list, "lists")
    object NotesScreen: NavItems("Нотатки", R.drawable.ic_note, "notes")
    object PreferenceScreen: NavItems("Налаштування", R.drawable.ic_settings, "settings")
}