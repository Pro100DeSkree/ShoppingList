package com.deskree.shoppinglist.main_activity.routes.settings

import android.content.Context.MODE_PRIVATE
import android.os.Build
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PreferenceScreen(
    settings: Settings,
) {
    val context = LocalContext.current
    val pref = context.getSharedPreferences(MAIN_PREFERENCE_KEY, MODE_PRIVATE)

    val isDarkTheme = pref.getBoolean(IS_DARK_THEME, isSystemInDarkTheme())
    val isDynamicColors = pref.getBoolean(IS_DYNAMIC_COLORS, true)

    LazyColumn {
        /*TODO: Додати "Екран запуска"*/
        item {
            ChangeTheme(isDarkTheme) { isDark ->
                settings.changeTheme(isDark)
            }
        }
        item {
            ChangeDynamicColors(isDynamicColors) { isDynamic ->
                settings.changeDynamicColors(isDynamic)
            }
        }
    }
}

@Composable
fun ChangeTheme(isDarkTheme: Boolean, onChange: (Boolean) -> Unit) {
    val isChecked = remember { mutableStateOf(isDarkTheme) }

    SwitchPlate(
        title = "Тема",
        signatureOn = "Увімкнути світлу тему",
        signatureOff = "Увімкнути темну тему",
        isDescription = false,
        description = "",
        isChecked
    ) { isDarkThemeChanged ->
        onChange.invoke(isDarkThemeChanged)
    }
}

@Composable
fun ChangeDynamicColors(isDynamicColors: Boolean, onChange: (Boolean) -> Unit) {
    if (Build.VERSION.SDK_INT >= 31) {
        val isChecked = remember { mutableStateOf(isDynamicColors) }
        SwitchPlate(
            title = "Динамічні кольори",
            signatureOn = "Вимкнути динамічні кольори",
            signatureOff = "Увімкнути динамічні кольори",
            isDescription = true,
            description = "При увімкненому положенні будуть використовуватись три основні кольори вашої системи",
            isChecked
        ) { isDynamicColorsChanged ->
            onChange.invoke(isDynamicColorsChanged)
        }
    }
}

interface Settings {
    fun changeTheme(isDark: Boolean)
    fun changeDynamicColors(isDynamic: Boolean)
}