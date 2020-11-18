package com.less.repository.db.room

import androidx.room.*
import io.reactivex.Single

@Dao
interface DishesDao {
    @Query("SELECT * FROM DishesEntity")
    fun all(): Single<List<DishesEntity?>?>?

    @Delete
    fun delete(entity: DishesEntity)

    @Query("SELECT * FROM DishesEntity where category_id=:category_id")
    fun getDishList(category_id: Long): Single<List<DishesEntity?>?>?

    @Query("SELECT * FROM DishesEntity where id=:dishId")
    fun getDish(dishId: Long): Single<DishesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dishes: DishesEntity?): Single<Long>?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(entities: List<DishesEntity>)

    @Update
    fun update(entity: DishesEntity)
}