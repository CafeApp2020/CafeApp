package com.less.repository.db.room

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

// Для хранения блюд в базе данных
@Entity( indices = arrayOf(Index(value = arrayOf("id"), unique = true)),
         foreignKeys = arrayOf(
         ForeignKey(entity = CategoryEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("category_id"),
            onDelete = CASCADE)))

class DishesEntity (
    @field:PrimaryKey(autoGenerate = true)
    @field:ColumnInfo(name = "id")
    var id: Long,

    @field:ColumnInfo(name = "category_id")
    var category_id: Int?,

    @field:ColumnInfo(name = "name")
    var name: String?,

    @field:ColumnInfo(name = "description")
    var description: String?,

    @field:ColumnInfo(name = "imagepath")
    var imagepath: String?
)