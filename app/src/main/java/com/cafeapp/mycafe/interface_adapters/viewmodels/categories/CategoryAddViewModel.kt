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

class CategoryAddViewModel(val categoryInteractor: ICategoryInteractor) : ViewModel() {
    private val modifyCategoryViewState = MutableLiveData<CategoryAddViewState>()
    val categoryViewState: LiveData<CategoryAddViewState> = modifyCategoryViewState
    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun saveCategory(savecategory: CategoryEntity){
       if (savecategory.id>0)
           editableCategorySave(savecategory)
       else
           addNewCategory(savecategory)
    }

    fun editableCategorySave(savecategory: CategoryEntity) {
        compositeDisposable.add(
                categoryInteractor.updateCategory(savecategory)!!
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

    fun addNewCategory(savecategory: CategoryEntity) {
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

    fun loadCategory(category_id: Long) {
        compositeDisposable.add(
                categoryInteractor.loadCategory(category_id)!!
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                {
                                    modifyCategoryViewState.value = CategoryAddViewState(category = it, loadOk = true)
                                },
                                { error ->
                                    modifyCategoryViewState.value = CategoryAddViewState(saveErr = error, loadOk = false)
                                })
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

    fun getColorFromButton(view: View) : Int{
        return when(view.id){
            R.id.yellow_button -> 1;
            R.id.green_button -> 2;
            R.id.pink_button -> 3;
            R.id.red_button -> 4;
            R.id.blue_button -> 5;
            R.id.violet_button -> 6;
            else -> 0;
        }
    }
}