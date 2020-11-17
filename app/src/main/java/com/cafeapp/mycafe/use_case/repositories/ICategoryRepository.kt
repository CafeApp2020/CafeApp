package com.cafeapp.mycafe.use_case.repositories

import com.cafeapp.mycafe.use_case.data.CategoryResult
import com.less.repository.db.room.CategoryEntity
import io.reactivex.Completable
import io.reactivex.Single

interface ICategoryRepository {
    fun getAllCategory(): Single<List<CategoryEntity?>?>?
    fun saveCategory(category: CategoryEntity): Single<Long>?
    fun loadCategory(categoryId: Long): Single<CategoryEntity>
    fun updateCategory(categoryEntity: CategoryEntity): Completable
    fun deleteCategory(category: CategoryEntity): Completable
}