package com.cafeapp.mycafe.use_case.interactors.dishes

import com.cafeapp.mycafe.use_case.repositories.IDishRepository
import com.less.repository.db.room.DishesEntity
import io.reactivex.Completable
import io.reactivex.Single

class DishInteractor(val repository: IDishRepository) : IDishInteractor {
    override fun deleteDish(dish: DishesEntity): Completable {
        return repository.deleteDish(dish)
    }

    override fun getData(category_id: Long): Single<List<DishesEntity?>?>? {
        return repository.getData(category_id)
    }

    override fun getDish(dishId: Long): Single<DishesEntity> {
        return repository.getDish(dishId)
    }

    override fun saveDish(dish: DishesEntity): Single<Long>? {
        return repository.saveDish(dish)
    }
}