package com.cafeapp.mycafe.interface_adapters.viewmodels.dishes

import com.cafeapp.mycafe.interface_adapters.viewmodels.baseviewmodel.BaseViewModel
import com.cafeapp.mycafe.use_case.interactors.dishes.IDishInteractor
import com.less.repository.db.room.DishesEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DishViewModel(private val dishInteractor: IDishInteractor) : BaseViewModel() {

    fun getDishList(category_id: Long) {
        compositeDisposable.add(
            dishInteractor.getActiveDishList(category_id)!!  // break point
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    modifyViewState.value = DishesViewState(dishList=it)
                }, {
                    error ->modifyViewState.value = DishesViewState(error = error)
                })
        )
    }

    fun addNewDish(savedDish: DishesEntity) {
        compositeDisposable.add(
            dishInteractor.saveDish(savedDish)!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        modifyViewState.value =  DishesViewState(dish = savedDish, saveOk = true)
                    },
                    { error ->
                        modifyViewState.value = DishesViewState(error = error)
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
                       modifyViewState.value = DishesViewState(dish = editableDish, saveOk = true)
                    },
                    { error ->
                        modifyViewState.value =  DishesViewState(error = error)
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
                    modifyViewState.value = DishesViewState(dish=it, loadOk = true)
                }, {
                    modifyViewState.value = DishesViewState(error = it)
                })
        )
    }
}