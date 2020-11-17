package com.cafeapp.mycafe.use_case.interactors.categories

import com.less.repository.db.room.CategoryEntity
import io.reactivex.Completable
import io.reactivex.Single

interface ICategoryInteractor {
    fun getAllCategory(): Single<List<CategoryEntity?>?>?
    fun saveCategory(categoryEntity: CategoryEntity): Single<Long>?
    fun loadCategory(dishId: Long): Single<CategoryEntity>
    fun updateCategory(categoryEntity: CategoryEntity): Completable
    fun deleteCategory(dish: CategoryEntity): Completable
}