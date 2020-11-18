package com.cafeapp.mycafe.interface_adapters.viewmodels.dishes.dishlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cafeapp.mycafe.use_case.interactors.dishes.IDishInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DishListViewModel(private val interactor: IDishInteractor) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val mutableDishListViewState = MutableLiveData<DishListViewState>()

    val dishListViewStateToObserve = mutableDishListViewState

    override fun onCleared() {
        compositeDisposable.clear()
    }

    fun getDishList(category_id: Long) {
        compositeDisposable.add(
            interactor.getData(category_id)!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mutableDishListViewState.value = DishListViewState(it)
                }, {
                    mutableDishListViewState.value = null
                })
        )
    }
}