package com.cafeapp.mycafe.interface_adapters.viewmodels.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cafeapp.mycafe.use_case.interactors.categories.ICategoryInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CategoryListViewModel(private val categoryInteractor: ICategoryInteractor) : ViewModel() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val modifyCategoryViewState = MutableLiveData<CategoryListViewState>()

    val categoryViewState: LiveData<CategoryListViewState> = modifyCategoryViewState

    override fun onCleared() {
        compositeDisposable.clear()
    }

    fun getCategories() {
        compositeDisposable.add(
            categoryInteractor.getActiveCategory()!!
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
}