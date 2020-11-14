package com.less.repository.db.room

import androidx.room.*

@Dao
interface DishesDao {
    @Query("SELECT * FROM DishesEntity")
    suspend fun all(): List<DishesEntity>

    @Query("SELECT * FROM DishesEntity where category_id=:category_id")
    suspend fun getCategory(category_id: Long): List<DishesEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: DishesEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(entities: List<DishesEntity>)

    @Update
    suspend fun update(entity: DishesEntity)
    @Delete
    suspend fun delete(entity: DishesEntity)
}