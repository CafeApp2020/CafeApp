package com.less.repository.db.room

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface DishesDao {
    @Query("SELECT * FROM DishesEntity")
    fun getAllDishes(): Single<List<DishesEntity?>?>?

    @Delete
    fun delete(entity: DishesEntity)

    @Query("SELECT * FROM DishesEntity where category_id=:category_id AND deleted = 0")
    fun getActiveDishes(category_id: Long): Single<List<DishesEntity?>?>?

    @Query("SELECT * FROM DishesEntity where category_id=:category_id")
    fun getDishList(category_id: Long): Single<List<DishesEntity?>?>?

    @Query("SELECT * FROM DishesEntity where id=:dishId")
    fun getDish(dishId: Long): Single<DishesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dishes: DishesEntity?): Single<Long>?

    @Update
    fun update(entity: DishesEntity): Completable
}