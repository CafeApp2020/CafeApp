package com.cafeapp.mycafe.interface_adapters.viewmodels.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cafeapp.mycafe.use_case.interactors.categories.ICategoryInteractor
import com.less.repository.db.room.CategoryEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CategoryViewModel(private val categoryInteractor: ICategoryInteractor) : ViewModel() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val modifyCategoryViewState = MutableLiveData<CategoryAddViewState>()

    val categoryViewState: LiveData<CategoryAddViewState> = modifyCategoryViewState

    override fun onCleared() {
        compositeDisposable.clear()
    }

    private fun addNewCategory(saveCategory: CategoryEntity) {
        compositeDisposable.add(
            categoryInteractor.saveCategory(saveCategory)!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        modifyCategoryViewState.value =
                            CategoryAddViewState(category = saveCategory, saveOk = true)
                    },
                    { error ->
                        modifyCategoryViewState.value =
                            CategoryAddViewState(saveErr = error, saveOk = false)
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
                        modifyCategoryViewState.value =
                            CategoryAddViewState(category = saveCategory, saveOk = true)
                    },
                    { error ->
                        modifyCategoryViewState.value =
                            CategoryAddViewState(saveErr = error, saveOk = false)
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
                        modifyCategoryViewState.value =
                            CategoryAddViewState(category = it, loadOk = true)
                    },
                    { error ->
                        modifyCategoryViewState.value =
                            CategoryAddViewState(saveErr = error, loadOk = false)
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
            categoryInteractor.getActiveCategory()!!  // break point
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {categoryList ->
                        modifyCategoryViewState.value = CategoryAddViewState(categoryList=categoryList)  // break point
                    },
                    { error ->
                        modifyCategoryViewState.value = CategoryAddViewState(error=error)
                    })
        )
    }
}