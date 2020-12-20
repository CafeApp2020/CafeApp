package com.cafeapp.mycafe.interface_adapters.viewmodels.tables

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cafeapp.mycafe.frameworks.room.TableEntity
import com.cafeapp.mycafe.use_case.interactors.tables.ITableInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TableViewModel(private val tableInteractor : ITableInteractor) : ViewModel() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val modifyTableAddViewState = MutableLiveData<TableViewState>()
    val tableViewState: LiveData<TableViewState> = modifyTableAddViewState

    fun addTable(tableEntity: TableEntity) {
        compositeDisposable.add(
            tableInteractor.saveTable(tableEntity)!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        modifyTableAddViewState.value =
                            TableViewState(saveOk = true, tableList = it)
                    },
                    { error ->
                        modifyTableAddViewState.value = TableViewState(error = error)
                    })
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

    fun allTable() {
       compositeDisposable.add(
            tableInteractor.allTable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        modifyTableAddViewState.value =
                            TableViewState(saveOk = true, tableList = it)
                    },
                    { error ->
                        modifyTableAddViewState.value = TableViewState(error = error)
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
                        modifyTableAddViewState.value =
                            TableViewState(saveOk = true)
                    },
                    { error ->
                        modifyTableAddViewState.value = TableViewState(error = error)
                    })
        )
    }

    fun updateTable(tableEntity: TableEntity?) {
        compositeDisposable.add(
            tableInteractor.updateTable(tableEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        modifyTableAddViewState.value =
                            TableViewState(saveOk = true)
                    },
                    { error ->
                        modifyTableAddViewState.value = TableViewState(error = error)
                    })
        )
    }
}