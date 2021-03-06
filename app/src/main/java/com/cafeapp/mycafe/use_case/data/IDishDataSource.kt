package com.cafeapp.mycafe.use_case.data

import com.less.repository.db.room.DishesEntity
import io.reactivex.Completable
import io.reactivex.Single

interface IDishDataSource {
    fun deleteDish(dish: DishesEntity): Completable
    fun updateDish(entity: DishesEntity): Completable
    fun getActiveDishList(categoryId: Long): Single<List<DishesEntity?>?>?
    fun getDishList(categoryId: Long): Single<List<DishesEntity?>?>?
    fun getDish(dishId: Long): Single<DishesEntity>
    fun saveDish(dish: DishesEntity): Single<Long>?
}