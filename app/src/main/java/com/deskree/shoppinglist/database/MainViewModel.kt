package com.deskree.shoppinglist.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deskree.shoppinglist.database.entities.LibraryItem
import com.deskree.shoppinglist.database.entities.NoteItem
import com.deskree.shoppinglist.database.entities.ShopListItem
import com.deskree.shoppinglist.database.entities.ShopListNameItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {

    val allNotes: LiveData<List<NoteItem>> = repository.allNotes
    val allLists: LiveData<List<ShopListNameItem>> = repository.allLists

    fun AllShopListItemsById(listID: Int): LiveData<List<ShopListItem>>{
        return repository.AllShopListItemsById(listID)
    }

    // Бібліотека(Автодоповнення пошуку)
    fun insertLibraryItem(libraryItem: LibraryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertLibraryItem(libraryItem)
        }
    }

    fun updateLibraryItem(libraryItem: LibraryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateLibraryItem(libraryItem)
        }
    }

    fun getAllLibraryItems(name: String): Flow<List<LibraryItem>> {
        return repository.getAllLibraryItemsM(name)
    }

    fun deleteLibraryItem(libraryItem: LibraryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteLibraryItem(libraryItem)
        }
    }


    // Нотатка
    fun insertNote(note: NoteItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertNote(note)
        }
    }

    fun updateNote(note: NoteItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNote(note)
        }
    }

    fun getAllNotes(): Flow<List<NoteItem>> {
        return repository.getAllNotes()
    }

    fun deleteNote(note: NoteItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNote(note)
        }
    }


    // Список
    fun insertShopListName(name: ShopListNameItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertShopListName(name)
        }
    }

    fun updateListName(shopListNameItem: ShopListNameItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateListName(shopListNameItem)
        }
    }

    fun getAllShopListItems(listID: Int): Flow<List<ShopListItem>> {
        return repository.getAllShopListItems(listID)
    }

    fun deleteShopListNameItem(shopListNameItem: ShopListNameItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteShopListNameItem(shopListNameItem)
        }
    }


    // Елементи списку
    fun insertItem(shopListItem: ShopListItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertItem(shopListItem)
        }
    }

    fun updateShopListItem(item: ShopListItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateShopListItem(item)
        }
    }

    fun getAllShopListNames(): Flow<List<ShopListNameItem>> {
        return repository.getAllShopListNames()
    }

    fun deleteShopListItem(shopListItem: ShopListItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteShopListItem(shopListItem)
        }
    }

    fun deleteShopListItemsByListID(listID: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteShopListItemsByListID(listID)
        }
    }
}