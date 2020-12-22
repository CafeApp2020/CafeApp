package com.cafeapp.mycafe.interface_adapters.viewmodels.categories

import com.cafeapp.mycafe.interface_adapters.viewmodels.baseviewmodel.BaseViewModel
import com.cafeapp.mycafe.use_case.interactors.categories.ICategoryInteractor
import com.less.repository.db.room.CategoryEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CategoryViewModel(private val categoryInteractor: ICategoryInteractor) : BaseViewModel() {
    private fun addNewCategory(saveCategory: CategoryEntity) {
        compositeDisposable.add(
            categoryInteractor.saveCategory(saveCategory)!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        modifyViewState.value = CategoryViewState(category = saveCategory, saveOk = true)
                    },
                    { error ->
                        modifyViewState.value =  CategoryViewState(error = error, saveOk = false)
                    })
        )
    }

    private fun editableCategorySave(saveCategory: CategoryEntity) {
        compositeDisposable.add(
            categoryInteractor.updateCategory(saveCategory)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        modifyViewState.value =  CategoryViewState(category = saveCategory, saveOk = true)
                    },
                    { error ->
                        modifyViewState.value =  CategoryViewState(error = error, saveOk = false)
                    })
        )
    }

    fun loadCategory(category_id: Long) {
        compositeDisposable.add(
            categoryInteractor.loadCategory(category_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        modifyViewState.value = CategoryViewState(category = it, loadOk = true)
                    },
                    { error ->
                        modifyViewState.value =  CategoryViewState(error = error, loadOk = false)
                    })
        )
    }

    fun saveCategory(saveCategory: CategoryEntity) {
        if (saveCategory.id > 0)
            editableCategorySave(saveCategory)
        else
            addNewCategory(saveCategory)
    }

    fun getCategories() {
        compositeDisposable.add(
            categoryInteractor.getActiveCategory()!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {categoryList ->
                        modifyViewState.value = CategoryViewState(categoryList=categoryList)
                    },
                    { error ->
                        modifyViewState.value = CategoryViewState(error=error)
                    })
        )
    }
}