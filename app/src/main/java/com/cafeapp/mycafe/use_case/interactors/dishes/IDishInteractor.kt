package com.cafeapp.mycafe.use_case.interactors.dishes

import com.less.repository.db.room.DishesEntity
import io.reactivex.Completable
import io.reactivex.Single

interface IDishInteractor {
    fun deleteDish(dish: DishesEntity): Completable
    fun updateDish(entity: DishesEntity): Completable
    fun getData(category_id: Long): Single<List<DishesEntity?>?>?
    fun getDish(dishId: Long): Single<DishesEntity>
    fun saveDish(dish: DishesEntity): Single<Long>?
}