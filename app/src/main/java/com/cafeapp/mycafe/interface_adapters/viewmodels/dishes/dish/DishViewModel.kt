package com.cafeapp.mycafe.interface_adapters.viewmodels.dishes.dish

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cafeapp.mycafe.use_case.interactors.dishes.IDishInteractor
import com.less.repository.db.room.DishesEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DishViewModel(private val dishInteractor: IDishInteractor) : ViewModel() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val modifyDishViewState = MutableLiveData<DishesViewState>()

    val dishViewState: LiveData<DishesViewState> = modifyDishViewState

    fun addNewDish(savedDish: DishesEntity) {
        compositeDisposable.add(
            dishInteractor.saveDish(savedDish)!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        modifyDishViewState.value =
                            DishesViewState(dish = savedDish, saveOk = true)
                    },
                    { error ->
                        modifyDishViewState.value =
                            DishesViewState(error = error)
                    })
        )
    }

    fun editDish(editableDish: DishesEntity) {
        compositeDisposable.add(
            dishInteractor.updateDish(editableDish)!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                       modifyDishViewState.value =
                      DishesViewState(dish = editableDish, saveOk = true)
                    },
                    { error ->
                        modifyDishViewState.value =
                            DishesViewState(error = error)
                    })
        )
    }

    fun saveDish(dish: DishesEntity) {
        if (dish.id > 0)
         editDish(dish)
        else
         addNewDish(dish)
    }

    fun getDish(dishId: Long) {
        compositeDisposable.add(
            dishInteractor.getDish(dishId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    modifyDishViewState.value = DishesViewState(dish=it, loadOk = true)
                }, {
                    modifyDishViewState.value = DishesViewState(error = it)
                })
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}