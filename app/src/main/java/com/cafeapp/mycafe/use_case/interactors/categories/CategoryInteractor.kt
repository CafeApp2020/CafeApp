package com.cafeapp.mycafe.use_case.interactors.categories

import com.cafeapp.mycafe.use_case.repositories.ICategoryRepository
import com.less.repository.db.room.CategoryEntity
import io.reactivex.Completable
import io.reactivex.Single

class CategoryInteractor(val repository: ICategoryRepository) : ICategoryInteractor {
    override fun removeCategory(category: CategoryEntity): Completable {
        return repository.deleteCategory(category)
    }

    override fun getActiveCategory(): Single<List<CategoryEntity?>?>? {
        return repository.getActiveCategory()  // break point
    }

    override fun getAllCategory(): Single<List<CategoryEntity?>?>? {
        return repository.getAllCategory()
    }

    override fun loadCategory(categoryId: Long): Single<CategoryEntity> {
        return repository.loadCategory(categoryId)
    }

    override fun saveCategory(category: CategoryEntity): Single<Long>? {
        return repository.saveCategory(category)
    }

    override fun updateCategory(categoryEntity: CategoryEntity): Completable {
        return repository.updateCategory(categoryEntity)
    }
}