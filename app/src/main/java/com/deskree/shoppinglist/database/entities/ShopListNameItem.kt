package com.deskree.shoppinglist.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/* **** СТВОРЕННЯ ТАБЛИЦІ **** */
@Entity (tableName = "shopping_list_names")
data class ShopListNameItem(
    /* **** СТВОРЕННЯ КОЛОН **** */
    // Ствпчик з ІД списку
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    // Стовпчик з назвою списку
    @ColumnInfo (name = "name")
    val name: String,
    // Стовпчик з датою та часом створення списку
    @ColumnInfo (name = "time")
    val time: String,
    // Стовпчик з кількістю елементів в списку
    @ColumnInfo (name = "allItemCount")
    val totalTasks: Int,
    // Стовпчик з кількістю відмічених елементів
    @ColumnInfo (name = "checkedItemsCounter")
    val tasksCompleted: Int,
    //
    @ColumnInfo (name = "itemsIDs")
    val itemIDs: String
    ):Serializable
