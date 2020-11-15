package com.cafeapp.mycafe.use_case.interactors.dishes

import com.cafeapp.mycafe.use_case.data.DishResult
import com.less.repository.db.room.DishesEntity
import io.reactivex.Completable
import io.reactivex.Single

interface IDishInteractor {
    fun saveDish(dish: DishesEntity): Single<Long>?
    fun loadDish(dishId: Int): Single<DishResult>
    fun deleteDish(dish: DishesEntity): Completable
}