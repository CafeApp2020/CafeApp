package com.cafeapp.mycafe.use_case.repositories

import com.less.repository.db.room.DishesEntity
import io.reactivex.Completable
import io.reactivex.Single

interface IDishRepository {
    fun deleteDish(dish: DishesEntity): Completable
    fun getData(category_id: Long): Single<List<DishesEntity?>?>?
    fun updateDish(entity: DishesEntity): Completable
    fun getDish(dishId: Long): Single<DishesEntity>
    fun saveDish(dish: DishesEntity): Single<Long>?
}