package com.cafeapp.mycafe.frameworks.room

import com.cafeapp.mycafe.use_case.data.DishResult
import com.cafeapp.mycafe.use_case.data.IDishDataSource
import com.less.repository.db.room.DishesDao
import com.less.repository.db.room.DishesEntity
import io.reactivex.Completable
import io.reactivex.Single

class RoomDishDataSource(val dishesDao: DishesDao) : IDishDataSource {
    override fun deleteDish(dish: DishesEntity): Completable {
        TODO("Not yet implemented")
    }

    override fun getData(categoryId: Long): Single<List<DishesEntity?>?>? {
        return dishesDao.getDishList(categoryId)
    }

    override fun getDish(dishId: Long): Single<DishesEntity> {
        return dishesDao.getDish(dishId)
    }

    override fun saveDish(dish: DishesEntity): Single<Long>? {
        return dishesDao.insert(dish)
    }
}