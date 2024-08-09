package com.deskree.shoppinglist.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory

class ViewModelFactory(private val repository: Repository) : NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T = MainViewModel(
        repository
    ) as T
}

