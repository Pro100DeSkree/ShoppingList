package com.deskree.shoppinglist.note_activity

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
import com.deskree.shoppinglist.database.entities.NoteItem
import com.deskree.shoppinglist.main_activity.routes.settings.IS_DARK_THEME
import com.deskree.shoppinglist.main_activity.routes.settings.IS_DYNAMIC_COLORS
import com.deskree.shoppinglist.main_activity.routes.settings.MAIN_PREFERENCE_KEY
import com.deskree.shoppinglist.ui.theme.ShoppingListTheme

class NoteActivity : ComponentActivity() {
    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dao = MainDataBase.getDataBase(application).getDao()
        val repository = RepositoryImpl(dao)
        val factory = ViewModelFactory(repository)
        mainViewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        var note = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(NOTE_KEY, NoteItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra(NOTE_KEY) as NoteItem
        }

        if (note == null){
            note = NoteItem(null, "","","","","")
        }

        val pref = getSharedPreferences(MAIN_PREFERENCE_KEY, MODE_PRIVATE)

        setContent {
            val isSystemDarkTheme = isSystemInDarkTheme()
            val darkTheme =
                remember { mutableStateOf(pref.getBoolean(IS_DARK_THEME, isSystemDarkTheme)) }
            val dynamicColors =
                remember { mutableStateOf(pref.getBoolean(IS_DYNAMIC_COLORS, true)) }

            ShoppingListTheme(darkTheme = darkTheme.value, dynamicColor = dynamicColors.value) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(note)
                }
            }
        }
    }
    companion object{
        const val NOTE_KEY = "note_key"
    }
}
