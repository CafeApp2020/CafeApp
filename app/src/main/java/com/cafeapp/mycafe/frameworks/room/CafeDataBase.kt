package com.cafeapp.mycafe.frameworks.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.less.repository.db.room.CategoryDao
import com.less.repository.db.room.CategoryEntity
import com.less.repository.db.room.DishesDao
import com.less.repository.db.room.DishesEntity

@Database(entities = arrayOf(CategoryEntity::class, DishesEntity::class), version = 1, exportSchema = false)
abstract class CafeDataBase : RoomDatabase() {
     open abstract fun categoryDao(): CategoryDao
     open abstract fun dishesDao(): DishesDao
}