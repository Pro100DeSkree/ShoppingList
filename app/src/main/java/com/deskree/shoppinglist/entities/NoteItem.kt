package com.deskree.shoppinglist.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity (tableName = "note_list")
data class NoteItem(
    @PrimaryKey (autoGenerate = true)
    val id: Int?,
    @ColumnInfo (name = "title")
    val title: String,
    @ColumnInfo (name = "content")
    val content: String,
    @ColumnInfo (name = "data_time")
    val dataTime: String,
    @ColumnInfo (name = "change_date_time")
    val changeDateTime: String? = null,
    @ColumnInfo (name = "category")
    val category: String
): Serializable
