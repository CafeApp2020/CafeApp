package com.cafeapp.mycafe.use_case.repositories

import com.cafeapp.mycafe.use_case.data.DishResult
import com.less.repository.db.room.DishesEntity
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

interface IDishRepository {
        fun getData(category_id:Long): Single<List<DishesEntity?>?>?
        fun saveDish(dish: DishesEntity): Single<Long>?
        fun loadDish(dishId: Int): Single<DishResult>
        fun deleteDish(dish: DishesEntity): Completable
}