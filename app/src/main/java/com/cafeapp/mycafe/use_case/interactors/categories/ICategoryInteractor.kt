package com.cafeapp.mycafe.use_case.interactors.categories

import com.less.repository.db.room.CategoryEntity
import io.reactivex.Completable
import io.reactivex.Single

interface ICategoryInteractor {
    fun deleteCategory(dish: CategoryEntity): Completable
    fun getAllCategory(): Single<List<CategoryEntity?>?>?
    fun loadCategory(dishId: Long): Single<CategoryEntity>
    fun saveCategory(categoryEntity: CategoryEntity): Single<Long>?
    fun updateCategory(categoryEntity: CategoryEntity): Completable
}