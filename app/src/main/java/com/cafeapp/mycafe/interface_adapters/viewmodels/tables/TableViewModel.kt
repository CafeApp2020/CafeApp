package com.cafeapp.mycafe.interface_adapters.viewmodels.tables

import com.cafeapp.mycafe.frameworks.room.TableEntity
import com.cafeapp.mycafe.interface_adapters.viewmodels.baseviewmodel.BaseViewModel
import com.cafeapp.mycafe.use_case.interactors.tables.ITableInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TableViewModel(private val tableInteractor : ITableInteractor) : BaseViewModel() {
    fun addTable(tableEntity: TableEntity) {
        compositeDisposable.add(
            tableInteractor.saveTable(tableEntity)!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                     modifyViewState.value =  TableViewState(saveOk = true, tableList = it)
                    },
                    { error ->
                        modifyViewState.value = TableViewState(error = error)
                    })
        )
    }

    fun allTable() {
       compositeDisposable.add(
            tableInteractor.allTable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        modifyViewState.value = TableViewState(saveOk = true, tableList = it)
                    },
                    { error ->
                        modifyViewState.value = TableViewState(error = error)
                    })
        )
    }

    fun removeTable(tableEntity: TableEntity) {
        compositeDisposable.add(
            tableInteractor.removeTable(tableEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        modifyViewState.value = TableViewState(saveOk = true)
                    },
                    { error -> modifyViewState.value = TableViewState(error = error)})
        )
    }

    fun updateTable(tableEntity: TableEntity?) {
        compositeDisposable.add(
            tableInteractor.updateTable(tableEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        modifyViewState.value = TableViewState(saveOk = true)
                    },
                    { error -> modifyViewState.value = TableViewState(error = error)
                    })
        )
    }
}