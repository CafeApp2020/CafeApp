package com.cafeapp.mycafe.interface_adapters.viewmodels.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cafeapp.mycafe.use_case.interactors.categories.CategoryInteractor
import com.less.repository.db.room.CategoryEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CategoryAddViewModel(val categoryInteractor: CategoryInteractor) : ViewModel() {
    private val modifyCategoryViewState = MutableLiveData<CategoryAddViewState>()
    val categoryViewState: LiveData<CategoryAddViewState> = modifyCategoryViewState
    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun saveCategory(savecategory: CategoryEntity){
        compositeDisposable.add(
                categoryInteractor.saveCategory(savecategory)!!
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                {
                                 modifyCategoryViewState.value = CategoryAddViewState(category = savecategory, saveOk = true)
                                },
                                { error ->
                                    modifyCategoryViewState.value = CategoryAddViewState(saveErr = error, saveOk = false)
                                })
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}