package com.cafeapp.mycafe.use_case.interactors.dishes

import com.cafeapp.mycafe.use_case.data.DishResult
import com.cafeapp.mycafe.use_case.repositories.IDishRepository
import com.less.repository.db.room.DishesEntity
import io.reactivex.Completable
import io.reactivex.Single

class DishInteractor(val repository: IDishRepository): IDishInteractor {
    override fun saveDish(dish: DishesEntity): Single<Long>? {
        return repository.saveDish(dish)
    }

    override fun loadDish(dishId: Int): Single<DishResult> {
        return repository.loadDish(dishId)
    }

    override fun deleteDish(dish: DishesEntity): Completable {
        return repository.deleteDish(dish)
    }
}