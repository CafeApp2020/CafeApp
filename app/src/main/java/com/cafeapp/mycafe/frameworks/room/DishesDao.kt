package com.less.repository.db.room

import androidx.room.*
import io.reactivex.Single

@Dao
interface DishesDao {
    @Query("SELECT * FROM DishesEntity")
    open fun all(): Single<List<DishesEntity?>?>?

    @Query("SELECT * FROM DishesEntity where category_id=:category_id")
    open fun getDishList(category_id: Long): Single<List<DishesEntity?>?>?

    @Query("SELECT * FROM DishesEntity where id=:dishId")
    open fun getDish(dishId: Long): Single<DishesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    open fun insert(dishes: DishesEntity?): Single<Long>?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    open fun insertAll(entities: List<DishesEntity>)

    @Update
    suspend fun update(entity: DishesEntity)

    @Delete
    suspend fun delete(entity: DishesEntity)
}