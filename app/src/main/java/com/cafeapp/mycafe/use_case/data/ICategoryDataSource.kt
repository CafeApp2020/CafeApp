package com.cafeapp.mycafe.use_case.data

import com.less.repository.db.room.CategoryEntity
import io.reactivex.Completable
import io.reactivex.Single

interface ICategoryDataSource {
    fun deleteCategory(category: CategoryEntity): Completable
    fun getActiveCategory(): Single<List<CategoryEntity?>?>?
    fun getAllCategory(): Single<List<CategoryEntity?>?>?
    fun loadCategory(categoryId: Long): Single<CategoryEntity>
    fun saveCategory(category: CategoryEntity): Single<Long>?
    fun updateCategory(categoryEntity: CategoryEntity): Completable
}