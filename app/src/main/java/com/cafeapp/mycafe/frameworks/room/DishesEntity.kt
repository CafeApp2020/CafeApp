package com.less.repository.db.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// Для хранения блюд в базе данных
// После того, как как будут доделаны категории и список блюд раскоментировать. Следующий @Entity удалить
/*@Entity( indices = arrayOf(Index(value = arrayOf("id"), unique = true)),
         foreignKeys = arrayOf(
         ForeignKey(entity = CategoryEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("category_id"),
            onDelete = CASCADE)))*/
@Entity(indices = arrayOf(Index(value = arrayOf("id"), unique = true)))
class DishesEntity(
    @field:ColumnInfo(name = "category_id")
    var category_id: Int = 0,

    @field:ColumnInfo(name = "description")
    var description: String? = null,

    @field:ColumnInfo(name = "imagepath")
    var imagepath: String? = null,

    @field:ColumnInfo(name = "name")
    var name: String? = null,

    @field:ColumnInfo(name = "price")
    var price: Float? = null,

    @field:ColumnInfo(name = "weight")
    var weight: Float? = 0f
) {
    @field:PrimaryKey(autoGenerate = true)
    @field:ColumnInfo(name = "id")
    var id: Long = 0
}