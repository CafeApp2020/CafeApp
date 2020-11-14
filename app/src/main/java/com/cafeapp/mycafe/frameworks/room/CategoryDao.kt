package com.less.repository.db.room

import androidx.room.*

@Dao
interface CategoryDao {
    @Query("SELECT * FROM CategoryEntity")
    suspend fun all(): List<CategoryEntity>

    @Query("SELECT * FROM CategoryEntity where id=:category_id")
    suspend fun getCategory(category_id: Long): List<CategoryEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: CategoryEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(entities: List<CategoryEntity>)

    @Update
    suspend fun update(entity: CategoryEntity)
    @Delete
    suspend fun delete(entity: CategoryEntity)
}