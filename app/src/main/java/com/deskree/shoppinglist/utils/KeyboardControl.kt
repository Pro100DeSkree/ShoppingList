package com.deskree.shoppinglist.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.deskree.shoppinglist.activities.ShopListActivity

object KeyboardControl {
    const val STATE_HIDE = "hide"
    const val STATE_SHOW = "show"

    fun showOrHideKeyboard(state: String, activity: Activity,) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val isKeyboardOpen = imm.isActive
        when (state) {
            STATE_HIDE -> {
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
//                imm.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
            }
            STATE_SHOW -> {
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            }
        }
    }
}