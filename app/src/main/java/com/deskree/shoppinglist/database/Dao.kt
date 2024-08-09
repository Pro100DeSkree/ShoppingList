package com.deskree.shoppinglist.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.deskree.shoppinglist.database.entities.LibraryItem
import com.deskree.shoppinglist.database.entities.NoteItem
import com.deskree.shoppinglist.database.entities.ShopListItem
import com.deskree.shoppinglist.database.entities.ShopListNameItem
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {


    // Бібліотека(Автодоповнення пошуку)
    @Insert
    suspend fun insertLibraryItem(libraryItem: LibraryItem)

    @Update
    suspend fun updateLibraryItem(libraryItem: LibraryItem)

    @Query("SELECT * FROM library WHERE name LIKE :name")
    fun getAllLibraryItemsM(name: String): Flow<List<LibraryItem>>

    @Delete
    suspend fun deleteLibraryItem(libraryItem: LibraryItem)


    // Нотатка
    @Insert
    suspend fun insertNote(note: NoteItem)

    @Update
    suspend fun updateNote(note: NoteItem)

    @Query("SELECT * FROM note_list")
    fun getAllNotes(): Flow<List<NoteItem>>

    @Delete
    suspend fun deleteNote(note: NoteItem)


    // Список
    @Insert
    suspend fun insertShopListName(name: ShopListNameItem)

    @Update
    suspend fun updateListName(shopListNameItem: ShopListNameItem)

    @Query("SELECT * FROM shopping_list_names")
    fun getAllShopListNames(): Flow<List<ShopListNameItem>>

    @Delete
    suspend fun deleteShopListNameItem(shopListNameItem: ShopListNameItem)


    // Елементи списку
    @Insert
    suspend fun insertItem(shopListItem: ShopListItem)

    @Update
    suspend fun updateShopListItem(item: ShopListItem)

    @Query("SELECT * FROM shop_list_item WHERE listID LIKE :listID")
    fun getAllShopListItems(listID: Int): Flow<List<ShopListItem>>

    @Query ("DELETE FROM shop_list_item WHERE listID IS :listID")
    fun deleteShopItemsByListID(listID: Int)

    @Delete
    suspend fun deleteShopListItem(shopListItem: ShopListItem)

}