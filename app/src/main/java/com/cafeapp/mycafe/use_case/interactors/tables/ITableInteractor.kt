package com.cafeapp.mycafe.use_case.interactors.tables

import com.cafeapp.mycafe.frameworks.room.TableEntity
import io.reactivex.Completable
import io.reactivex.Observable

interface ITableInteractor {
    fun saveTable(tableEntity: TableEntity): Observable<List<TableEntity>>
    fun allTable(): Observable<List<TableEntity>>
    fun removeTable(tableEntity: TableEntity): Completable
    fun updateTable(tableEntity: TableEntity?): Completable
}