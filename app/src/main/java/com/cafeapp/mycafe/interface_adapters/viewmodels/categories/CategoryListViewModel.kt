package com.cafeapp.mycafe.interface_adapters.viewmodels.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cafeapp.mycafe.use_case.interactors.categories.CategoryInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CategoryListViewModel(val categoryInteractor: CategoryInteractor) : ViewModel() {
    private val modifyCategoryViewState = MutableLiveData<CategoryListViewState>()
    val categoryViewState: LiveData<CategoryListViewState> = modifyCategoryViewState
    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun getCategories(){
        compositeDisposable.add(
                categoryInteractor.getAllCategory()!!
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                {
                                    modifyCategoryViewState.value = CategoryListViewState(it)
                                },
                                { error ->
                                    modifyCategoryViewState.value = null
                                })
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}