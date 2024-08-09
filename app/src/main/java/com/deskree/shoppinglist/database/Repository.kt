package com.deskree.shoppinglist.database

import androidx.lifecycle.LiveData
import com.deskree.shoppinglist.database.entities.LibraryItem
import com.deskree.shoppinglist.database.entities.NoteItem
import com.deskree.shoppinglist.database.entities.ShopListItem
import com.deskree.shoppinglist.database.entities.ShopListNameItem
import kotlinx.coroutines.flow.Flow

interface Repository {
     val allNotes: LiveData<List<NoteItem>>
     val allLists: LiveData<List<ShopListNameItem>>

     fun AllShopListItemsById(listId: Int): LiveData<List<ShopListItem>>

     // Бібліотека(Автодоповнення пошуку)
     suspend fun insertLibraryItem(libraryItem: LibraryItem)

     suspend fun updateLibraryItem(libraryItem: LibraryItem)

     fun getAllLibraryItemsM(name: String): Flow<List<LibraryItem>>

     suspend fun deleteLibraryItem(libraryItem: LibraryItem)


     // Нотатка
     suspend fun insertNote(note: NoteItem)

     suspend fun updateNote(note: NoteItem)

     fun getAllNotes(): Flow<List<NoteItem>>

     suspend fun deleteNote(note: NoteItem)


     // Список
     suspend fun insertShopListName(name: ShopListNameItem)

     suspend fun updateListName(shopListNameItem: ShopListNameItem)

     fun getAllShopListItems(listID: Int): Flow<List<ShopListItem>>

     suspend fun deleteShopListNameItem(shopListNameItem: ShopListNameItem)


     // Елементи списку
     suspend fun insertItem(shopListItem: ShopListItem)

     suspend fun updateShopListItem(item: ShopListItem)

     fun getAllShopListNames(): Flow<List<ShopListNameItem>>

     suspend fun deleteShopListItem(shopListItem: ShopListItem)

     fun deleteShopListItemsByListID(id: Int)

}