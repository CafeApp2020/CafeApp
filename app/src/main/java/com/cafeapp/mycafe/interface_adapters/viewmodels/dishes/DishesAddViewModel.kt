package com.cafeapp.mycafe.interface_adapters.viewmodels.dishes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cafeapp.mycafe.use_case.interactors.dishes.IDishInteractor
import com.less.repository.db.room.DishesEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DishesAddViewModel(val dishInteractor: IDishInteractor) : ViewModel() {
    private val modifyDishViewState = MutableLiveData<DishesAddViewState>()
    val dishViewState: LiveData<DishesAddViewState> = modifyDishViewState
    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun saveDish(saveddish: DishesEntity) {
        compositeDisposable.add(
        dishInteractor.saveDish(saveddish)!!
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    modifyDishViewState.value = DishesAddViewState(dish=saveddish, saveOk = true)
                },
                { error ->
                    modifyDishViewState.value = DishesAddViewState(saveErr = error, saveOk = false)
                })
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}
