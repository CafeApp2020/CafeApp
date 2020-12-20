package com.cafeapp.mycafe.use_case.repositories

import com.cafeapp.mycafe.frameworks.room.TableEntity
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface ITableRepository {
    fun saveTable(tableEntity: TableEntity): Completable
    fun getAll(): Observable<List<TableEntity>>
    fun removeTable(tableEntity: TableEntity): Completable
    fun updateTable(tableEntity: TableEntity?): Completable
}