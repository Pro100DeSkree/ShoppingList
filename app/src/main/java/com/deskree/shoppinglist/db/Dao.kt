package com.deskree.shoppinglist.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.deskree.shoppinglist.entities.LibraryItem
import com.deskree.shoppinglist.entities.NoteItem
import com.deskree.shoppinglist.entities.ShopListItem
import com.deskree.shoppinglist.entities.ShopListNameItem
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Query ("SELECT * FROM note_list")
    fun getAllNotes(): Flow<List<NoteItem>>

    @Query ("SELECT * FROM shopping_list_names")
    fun getAllShopListNames(): Flow<List<ShopListNameItem>>

    @Query ("SELECT * FROM shop_list_item WHERE listID LIKE :listID")
    fun getAllShopListItems(listID: Int): Flow<List<ShopListItem>>

    @Query ("SELECT * FROM library WHERE name LIKE :name")
    suspend fun getAllLibraryItemsM(name: String): List<LibraryItem>

    @Query ("DELETE FROM note_list WHERE id IS :id")
    suspend fun deleteNote(id: Int)

    @Query ("DELETE FROM shopping_list_names WHERE id IS :id")
    suspend fun deleteShopListName(id: Int)

    @Query ("DELETE FROM shop_list_item WHERE id IS :itemID")
    suspend fun deleteShopListItem(itemID: Int)

    @Query ("DELETE FROM shop_list_item WHERE listID IS :listID")
    suspend fun deleteShopItemsByListID(listID: Int)

    @Query ("DELETE FROM library WHERE id IS :id")
    suspend fun deleteLibraryItem(id: Int)

    @Insert
    suspend fun insertNote(note: NoteItem)
    @Insert
    suspend fun insertItem(shopListItem: ShopListItem)
    @Insert
    suspend fun insertLibraryItem(shopLibraryItem: LibraryItem)
    @Insert
    suspend fun insertShopListName(name: ShopListNameItem)
    @Update
    suspend fun updateNote(note: NoteItem)
    @Update
    suspend fun updateLibraryItem(libraryItem: LibraryItem)
    @Update
    suspend fun updateShopListItem(item: ShopListItem)
    @Update
    suspend fun updateListName(shopListNameItem: ShopListNameItem)
}