package com.cafeapp.mycafe.interface_adapters.viewmodels.categories

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.use_case.interactors.categories.ICategoryInteractor
import com.less.repository.db.room.CategoryEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CategoryAddViewModel(private val categoryInteractor: ICategoryInteractor) : ViewModel() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val modifyCategoryViewState = MutableLiveData<CategoryAddViewState>()

    val categoryViewState: LiveData<CategoryAddViewState> = modifyCategoryViewState

    override fun onCleared() {
        compositeDisposable.clear()
    }

    private fun addNewCategory(savecategory: CategoryEntity) {
        compositeDisposable.add(
            categoryInteractor.saveCategory(savecategory)!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        modifyCategoryViewState.value =
                            CategoryAddViewState(category = savecategory, saveOk = true)
                    },
                    { error ->
                        modifyCategoryViewState.value =
                            CategoryAddViewState(saveErr = error, saveOk = false)
                    })
        )
    }

    private fun editableCategorySave(savecategory: CategoryEntity) {
        compositeDisposable.add(
            categoryInteractor.updateCategory(savecategory)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        modifyCategoryViewState.value =
                            CategoryAddViewState(category = savecategory, saveOk = true)
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

    fun saveCategory(savecategory: CategoryEntity) {
        if (savecategory.id > 0)
            editableCategorySave(savecategory)
        else
            addNewCategory(savecategory)
    }
}