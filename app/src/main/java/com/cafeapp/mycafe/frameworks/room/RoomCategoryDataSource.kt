package com.cafeapp.mycafe.frameworks.room

import com.cafeapp.mycafe.use_case.data.CategoryResult
import com.cafeapp.mycafe.use_case.data.ICategoryDataSource
import com.less.repository.db.room.CategoryDao
import com.less.repository.db.room.CategoryEntity
import io.reactivex.Completable
import io.reactivex.Single

class RoomCategoryDataSource(val categoryDao: CategoryDao): ICategoryDataSource {
    override fun getAllCategory(): Single<List<CategoryEntity?>?>? {
        return categoryDao.all()
    }

    override fun saveCategory(category: CategoryEntity): Single<Long>? {
        return categoryDao.insert(category)
    }

    override fun loadCategory(categoryId: Int): Single<CategoryResult> {
        TODO("Not yet implemented")
    }

    override fun deleteCategory(category: CategoryEntity): Completable {
        TODO("Not yet implemented")
    }
}