package com.less.repository.db.room

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CategoryDao {
    @Query("SELECT * FROM CategoryEntity")
    fun all(): Single<List<CategoryEntity?>?>?

    @Delete
    fun delete(entity: CategoryEntity)

    @Query("SELECT * FROM CategoryEntity where id=:category_id")
    fun getCategory(category_id: Long): Single<CategoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dishes: CategoryEntity?): Single<Long>?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(entities: List<CategoryEntity>)

    @Update
    fun update(entity: CategoryEntity): Completable
}