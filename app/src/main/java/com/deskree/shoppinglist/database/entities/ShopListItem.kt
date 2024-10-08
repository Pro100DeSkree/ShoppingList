package com.deskree.shoppinglist.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "shop_list_item")
data class ShopListItem(

    //
    @PrimaryKey (autoGenerate = true)
    val id: Int?,
    //
    @ColumnInfo (name = "name")
    val name: String,
    //
    @ColumnInfo (name = "itemInfo")
    val itemInfo: String = "",
    //
    @ColumnInfo (name = "itemChecked")
    val itemChecked: Boolean = false,
    //
    @ColumnInfo (name = "listID")
    val listID: Int,
    //
    @ColumnInfo (name = "itemType")
    val itemType: Int = 0
)
