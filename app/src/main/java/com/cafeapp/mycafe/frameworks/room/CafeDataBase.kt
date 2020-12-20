package com.cafeapp.mycafe.frameworks.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.less.repository.db.room.CategoryDao
import com.less.repository.db.room.CategoryEntity
import com.less.repository.db.room.DishesDao
import com.less.repository.db.room.DishesEntity

@Database(entities = arrayOf(CategoryEntity::class,
                             DishesEntity::class,
                             OrdersEntity::class,
                             OrderDishEntity::class,
                             TableEntity::class), version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class CafeDataBase : RoomDatabase() {
     abstract fun categoryDao(): CategoryDao
     abstract fun dishesDao(): DishesDao
     abstract fun orderDao(): OrderDao
     abstract fun tablesDao(): TablesDao
}