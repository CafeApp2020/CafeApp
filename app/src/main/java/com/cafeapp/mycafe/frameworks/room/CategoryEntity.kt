package com.less.repository.db.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// Для хранения категорий в базе данных
@Entity(indices = arrayOf(Index(value = arrayOf("id"), unique = true)))
class CategoryEntity(
    @field:ColumnInfo(name = "description")
    var description: String,

    @field:ColumnInfo(name = "imagepath")
    var imagepath: String,

    @field:ColumnInfo(name = "deleted")
    var isDeleted: Boolean = false,

    @field:ColumnInfo(name = "stoplist")
    var isInStopList: Boolean = false,

    @field:ColumnInfo(name = "name")
    var name: String
) {
    @field:PrimaryKey(autoGenerate = true)
    @field:ColumnInfo(name = "id")
    var id: Long = 0
}