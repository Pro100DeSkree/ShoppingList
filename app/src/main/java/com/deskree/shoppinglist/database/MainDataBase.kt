package com.deskree.shoppinglist.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.deskree.shoppinglist.database.entities.LibraryItem
import com.deskree.shoppinglist.database.entities.NoteItem
import com.deskree.shoppinglist.database.entities.ShopListItem
import com.deskree.shoppinglist.database.entities.ShopListNameItem

@Database (entities = [
    LibraryItem::class,
    NoteItem::class,
    ShopListItem::class,
    ShopListNameItem::class
    ], version = 1)
abstract class MainDataBase: RoomDatabase() {
    abstract fun getDao(): Dao


    companion object{
        @Volatile
        private var INSTANCE: MainDataBase? = null
        fun getDataBase(context: Context): MainDataBase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDataBase::class.java,
                    "shopping_list.db"
                ).build()
                instance
            }
        }
    }
}