package com.deskree.shoppinglist.main_activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import com.deskree.shoppinglist.database.entities.ShopListNameItem
import com.deskree.shoppinglist.list_activity.ListActivity
import com.deskree.shoppinglist.list_activity.ListActivity.Companion.LIST_KEY
import com.deskree.shoppinglist.main_activity.routes.settings.IS_DARK_THEME
import com.deskree.shoppinglist.main_activity.routes.settings.IS_DYNAMIC_COLORS
import com.deskree.shoppinglist.main_activity.routes.settings.MAIN_PREFERENCE_KEY
import com.deskree.shoppinglist.main_activity.routes.settings.Settings
import com.deskree.shoppinglist.note_activity.NoteActivity
import com.deskree.shoppinglist.note_activity.NoteActivity.Companion.NOTE_KEY
import com.deskree.shoppinglist.ui.theme.ShoppingListTheme
import kotlin.coroutines.coroutineContext

class MainActivity : ComponentActivity() {
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dao = MainDataBase.getDataBase(application).getDao()
        val repository = RepositoryImpl(dao)
        val factory = ViewModelFactory(repository)
        mainViewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        updateData()

        val pref = getSharedPreferences(MAIN_PREFERENCE_KEY, MODE_PRIVATE)


        setContent {
            val isSystemDarkTheme = isSystemInDarkTheme()
            val darkTheme =
                remember { mutableStateOf(pref.getBoolean(IS_DARK_THEME, isSystemDarkTheme)) }
            val dynamicColors =
                remember { mutableStateOf(pref.getBoolean(IS_DYNAMIC_COLORS, true)) }

            val settings = object : Settings {
                override fun changeTheme(isDark: Boolean) {
                    darkTheme.value = isDark
                    pref.edit().putBoolean(IS_DARK_THEME, isDark).apply()
                }

                override fun changeDynamicColors(isDynamic: Boolean) {
                    dynamicColors.value = isDynamic
                    pref.edit().putBoolean(IS_DYNAMIC_COLORS, isDynamic).apply()
                }
            }

            ShoppingListTheme(darkTheme = darkTheme.value, dynamicColor = dynamicColors.value) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppScaffold(mainViewModel, launcher, settings)
                }
            }
        }
    }

    private fun updateData() {
        launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == RESULT_OK) {

                    val editState = result.data?.getStringExtra(EDIT_STATE_KEY)

                    // TODO: Костильне збереження(тут і в ListActivity) для 100% оновлення даних
                    if (editState == "update_list") {
                        // Отримання списку на оновлення
                        val list = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            result.data?.getSerializableExtra(
                                ListActivity.LIST_KEY,
                                ShopListNameItem::class.java
                            )
                        } else {
                            @Suppress("DEPRECATION")
                            result.data?.getSerializableExtra(ListActivity.LIST_KEY) as ShopListNameItem
                        }!!
                        mainViewModel.updateListName(list)
                    } else {
                        // Отримання нової нотатки або оновленої
                        val note = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            result.data?.getSerializableExtra(NOTE_KEY, NoteItem::class.java)
                        } else {
                            @Suppress("DEPRECATION")
                            result.data?.getSerializableExtra(NoteActivity.NOTE_KEY) as NoteItem
                        }!!
                        // Оновлення
                        if (editState == "update_note")
                            mainViewModel.updateNote(note)
                        else
                            mainViewModel.insertNote(note)

                    }
                }
            }
    }

    companion object {
        const val EDIT_STATE_KEY = "edit_state_key"
    }
}



