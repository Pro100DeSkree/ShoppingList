package com.deskree.shoppinglist.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.deskree.shoppinglist.database.entities.LibraryItem
import com.deskree.shoppinglist.database.entities.NoteItem
import com.deskree.shoppinglist.database.entities.ShopListItem
import com.deskree.shoppinglist.database.entities.ShopListNameItem
import kotlinx.coroutines.flow.Flow

class RepositoryImpl(private val dao: Dao) : Repository {
    override val allNotes: LiveData<List<NoteItem>> = dao.getAllNotes().asLiveData()
    override val allLists: LiveData<List<ShopListNameItem>> = dao.getAllShopListNames().asLiveData()

    override fun AllShopListItemsById(listId: Int): LiveData<List<ShopListItem>> {
        return dao.getAllShopListItems(listId).asLiveData()
    }

    // Бібліотека(Автодоповнення пошуку)
    override suspend fun insertLibraryItem(libraryItem: LibraryItem) {
        dao.insertLibraryItem(libraryItem)
    }

    override suspend fun updateLibraryItem(libraryItem: LibraryItem) {
        dao.updateLibraryItem(libraryItem)
    }

    override fun getAllLibraryItemsM(name: String): Flow<List<LibraryItem>> {
        return dao.getAllLibraryItemsM(name)
    }

    override suspend fun deleteLibraryItem(libraryItem: LibraryItem) {
        dao.deleteLibraryItem(libraryItem)
    }


    // Нотатка
    override suspend fun insertNote(note: NoteItem) {
        dao.insertNote(note)
    }

    override suspend fun updateNote(note: NoteItem) {
        dao.updateNote(note)
    }

    override fun getAllNotes(): Flow<List<NoteItem>> {
        return dao.getAllNotes()
    }

    override suspend fun deleteNote(note: NoteItem) {
        dao.deleteNote(note)
    }


    // Список
    override suspend fun insertShopListName(name: ShopListNameItem) {
        dao.insertShopListName(name)
    }

    override suspend fun updateListName(shopListNameItem: ShopListNameItem) {
        dao.updateListName(shopListNameItem)
    }

    override fun getAllShopListItems(listID: Int): Flow<List<ShopListItem>> {
        return dao.getAllShopListItems(listID)
    }

    override suspend fun deleteShopListNameItem(shopListNameItem: ShopListNameItem) {
        dao.deleteShopListNameItem(shopListNameItem)
    }


    // Елементи списку
    override suspend fun insertItem(shopListItem: ShopListItem) {
        dao.insertItem(shopListItem)
    }

    override suspend fun updateShopListItem(item: ShopListItem) {
        dao.updateShopListItem(item)
    }

    override fun getAllShopListNames(): Flow<List<ShopListNameItem>> {
        return dao.getAllShopListNames()
    }

    override suspend fun deleteShopListItem(shopListItem: ShopListItem) {
        dao.deleteShopListItem(shopListItem)
    }

    override fun deleteShopListItemsByListID(listID: Int) {
        dao.deleteShopItemsByListID(listID)
    }
}