package com.deskree.shoppinglist.list_activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.deskree.shoppinglist.database.MainDataBase
import com.deskree.shoppinglist.database.MainViewModel
import com.deskree.shoppinglist.database.RepositoryImpl
import com.deskree.shoppinglist.database.ViewModelFactory
import com.deskree.shoppinglist.database.entities.ShopListNameItem
import com.deskree.shoppinglist.main_activity.bottom_nav.IT_LIST
import com.deskree.shoppinglist.main_activity.routes.settings.IS_DARK_THEME
import com.deskree.shoppinglist.main_activity.routes.settings.IS_DYNAMIC_COLORS
import com.deskree.shoppinglist.main_activity.routes.settings.MAIN_PREFERENCE_KEY
import com.deskree.shoppinglist.ui.theme.ShoppingListTheme

class ListActivity : ComponentActivity() {
    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        val dao = MainDataBase.getDataBase(application).getDao()
        val repository = RepositoryImpl(dao)
        val factory = ViewModelFactory(repository)
        mainViewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        val list = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(IT_LIST, ShopListNameItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra(IT_LIST) as ShopListNameItem
        }

        val pref = getSharedPreferences(MAIN_PREFERENCE_KEY, MODE_PRIVATE)

        setContent{
            val isSystemDarkTheme = isSystemInDarkTheme()
            val darkTheme = remember { mutableStateOf(pref.getBoolean(IS_DARK_THEME, isSystemDarkTheme)) }
            val dynamicColors = remember { mutableStateOf(pref.getBoolean(IS_DYNAMIC_COLORS, true)) }

            ShoppingListTheme(darkTheme = darkTheme.value, dynamicColor = dynamicColors.value) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ListScaffold(list!!, mainViewModel)
                }
            }
        }
    }

    companion object{
        const val LIST_KEY = "list_key"
    }
}