package com.cafeapp.mycafe.use_case.interactors.categories

import com.cafeapp.mycafe.use_case.data.CategoryResult
import com.less.repository.db.room.CategoryEntity
import io.reactivex.Completable
import io.reactivex.Single

interface ICategoryInteractor {
    fun getAllCategory(): Single<List<CategoryEntity?>?>?
    fun saveCategory(dish: CategoryEntity): Single<Long>?
    fun loadCategory(dishId: Int): Single<CategoryResult>
    fun deleteCategory(dish: CategoryEntity): Completable
}