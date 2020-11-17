package com.less.repository.db.room

import androidx.room.*
import io.reactivex.Single

@Dao
interface CategoryDao {
    @Query("SELECT * FROM CategoryEntity")
    fun all(): Single<List<CategoryEntity?>?>?

    @Query("SELECT * FROM CategoryEntity where id=:category_id")
    fun getCategory(category_id: Long): Single<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dishes: CategoryEntity?): Single<Long>?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(entities: List<CategoryEntity>)

    @Update
    suspend fun update(entity: CategoryEntity)
    @Delete
    suspend fun delete(entity: CategoryEntity)
}