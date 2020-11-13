package com.cafeapp.mycafe.frameworks.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.less.repository.db.room.CategoryDao
import com.less.repository.db.room.CategoryEntity
import com.less.repository.db.room.DishesDao

@Database(entities = arrayOf(CategoryEntity::class, DishesDao::class), version = 1, exportSchema = false)
abstract class CafeDataBase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun dishesDao(): DishesDao
}