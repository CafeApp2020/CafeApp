package com.cafeapp.mycafe.use_case.data

import com.cafeapp.mycafe.use_case.repositories.IDishRepository
import com.less.repository.db.room.DishesEntity
import io.reactivex.Completable
import io.reactivex.Single

class DishRepository(val dataSource: IDishDataSource) : IDishRepository {
    override fun deleteDish(dish: DishesEntity): Completable {
        return dataSource.deleteDish(dish)
    }

    override fun getData(category_id: Long): Single<List<DishesEntity?>?>? {
        return dataSource.getData(category_id)
    }

    override fun getDish(dishId: Long): Single<DishesEntity> {
        return dataSource.getDish(dishId)
    }

    override fun saveDish(dish: DishesEntity): Single<Long>? {
        return dataSource.saveDish(dish)
    }
}