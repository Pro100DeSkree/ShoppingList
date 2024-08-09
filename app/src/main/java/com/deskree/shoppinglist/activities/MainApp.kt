package com.deskree.shoppinglist.activities

import android.app.Application
import com.deskree.shoppinglist.db.MainDataBase

class MainApp:Application() {
    val database by lazy{ MainDataBase.getDataBase(this)}
}