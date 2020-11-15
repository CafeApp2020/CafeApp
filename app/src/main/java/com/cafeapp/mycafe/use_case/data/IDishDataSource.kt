package com.cafeapp.mycafe.use_case.data

import com.less.repository.db.room.DishesEntity
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

interface IDishDataSource {
    fun getData(categoryId:Long): Single<List<DishesEntity?>?>?
    fun saveDish(dish: DishesEntity): Single<Long>?
    fun loadDish(dishId: Int): Single<DishResult>
    fun deleteDish(dish: DishesEntity): Completable
}