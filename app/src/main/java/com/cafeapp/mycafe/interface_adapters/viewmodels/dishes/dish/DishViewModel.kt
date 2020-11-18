package com.cafeapp.mycafe.interface_adapters.viewmodels.dishes.dish

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cafeapp.mycafe.use_case.interactors.dishes.IDishInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DishViewModel(private val interactor: IDishInteractor) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val mutableDishViewState = MutableLiveData<DishViewState>()

    val dishViewStateToObserve = mutableDishViewState

    override fun onCleared() {
        compositeDisposable.clear()
    }

    fun getDish(dishId: Long) {
        compositeDisposable.add(
            interactor.getDish(dishId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mutableDishViewState.value = DishViewState(it)
                }, {
                    mutableDishViewState.value = null
                })
        )
    }
}