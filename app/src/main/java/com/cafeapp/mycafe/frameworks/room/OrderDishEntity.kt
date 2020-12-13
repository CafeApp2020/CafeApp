package com.cafeapp.mycafe.frameworks.room

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.less.repository.db.room.DishesEntity

// хранение в базе данных выбранных при заказе (в зале, доставке, с собой) блюд
@Entity( indices = arrayOf(Index(value = arrayOf("id"), unique = true)),
    foreignKeys = arrayOf(
        ForeignKey(entity = DishesEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("dish_id"),
            onDelete = CASCADE)))
class OrderDishEntity(
    @field:ColumnInfo(name = "order_id")
    var order_id: Long = 0,
    @field:ColumnInfo(name = "dish_id")
    var dish_id: Long = 0,
    @field:ColumnInfo(name = "dishCount")
    var dishCount: Int = 0,
) {
    @field:PrimaryKey(autoGenerate = true)
    @field:ColumnInfo(name = "id")
    var id: Long = 0
}