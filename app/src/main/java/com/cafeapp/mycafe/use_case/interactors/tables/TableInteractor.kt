package com.cafeapp.mycafe.use_case.interactors.tables

import com.cafeapp.mycafe.frameworks.room.TableEntity
import com.cafeapp.mycafe.use_case.repositories.ITableRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class TableInteractor(val tableRepository: ITableRepository): ITableInteractor {
    override fun saveTable(tableEntity: TableEntity): Observable<List<TableEntity>> {
        val addTableSingle = tableRepository.saveTable(tableEntity)
        val tableList = tableRepository.getAll()
        return Observable.merge(addTableSingle.toObservable(), tableList)
    }

    override fun allTable(): Observable<List<TableEntity>> {
       return tableRepository.getAll()
    }

    override fun removeTable(tableEntity: TableEntity): Completable {
        return tableRepository.removeTable(tableEntity)
    }

    override fun updateTable(tableEntity: TableEntity?): Completable {
        return tableRepository.updateTable(tableEntity)
    }

}