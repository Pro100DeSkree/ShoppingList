package com.deskree.shoppinglist.db

import androidx.lifecycle.*
import com.deskree.shoppinglist.entities.LibraryItem
import com.deskree.shoppinglist.entities.NoteItem
import com.deskree.shoppinglist.entities.ShopListItem
import com.deskree.shoppinglist.entities.ShopListNameItem
import kotlinx.coroutines.launch

class MainViewModel(dataBase: MainDataBase): ViewModel() {
    val dao = dataBase.getDao()

    val libraryItems = MutableLiveData<List<LibraryItem>>()

    val allNotes: LiveData<List<NoteItem>> = dao.getAllNotes().asLiveData()
    val allShopListNamesItem: LiveData<List<ShopListNameItem>> = dao.getAllShopListNames().asLiveData()

    fun getAllItemsFromList(listID: Int): LiveData<List<ShopListItem>>{
        return dao.getAllShopListItems(listID).asLiveData()
    }

    fun getAllLibraryItemsM(name: String) = viewModelScope.launch{
        libraryItems.postValue(dao.getAllLibraryItemsM(name))
    }

    fun insertNote(note: NoteItem) = viewModelScope.launch {
        dao.insertNote(note)
    }

    fun insertShoppingListName(listName: ShopListNameItem) = viewModelScope.launch {
        dao.insertShopListName(listName)
    }

    fun insertShopItem(shopListItem: ShopListItem) = viewModelScope.launch {
        dao.insertItem(shopListItem)
        if(!isLibraryItemExists(shopListItem.name)) dao.insertLibraryItem(
            LibraryItem(
                null,
                shopListItem.name))
    }

    fun updateNote(note: NoteItem) = viewModelScope.launch {
        dao.updateNote(note)
    }


    fun updateLibraryItem(item: LibraryItem) = viewModelScope.launch {
        dao.updateLibraryItem(item)
    }

    fun updateListItem(item: ShopListItem) = viewModelScope.launch {
        dao.updateShopListItem(item)
    }

    fun updateListName(shopListNameItem: ShopListNameItem) = viewModelScope.launch {
        dao.updateListName(shopListNameItem)
    }

    fun deleteNote(id: Int) = viewModelScope.launch {
        dao.deleteNote(id)
    }

    fun deleteShopList(id: Int) = viewModelScope.launch {
        dao.deleteShopListName(id)
        dao.deleteShopItemsByListID(id)
    }

    fun deleteLibraryItem(id: Int) = viewModelScope.launch {
        dao.deleteLibraryItem(id)
    }

    fun deleteShopListItem(itemID: Int) = viewModelScope.launch {
        dao.deleteShopListItem(itemID)
    }

    fun clearShopListItems(listID: Int) = viewModelScope.launch {
        dao.deleteShopItemsByListID(listID)
    }

    private suspend fun isLibraryItemExists(name: String): Boolean{
        return dao.getAllLibraryItemsM(name).isNotEmpty()
    }


    class MainViewModelFactory(val dataBase: MainDataBase): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(dataBase) as T
            }
            throw IllegalAccessException("Unknown ViewModelClass")
        }
    }
}
