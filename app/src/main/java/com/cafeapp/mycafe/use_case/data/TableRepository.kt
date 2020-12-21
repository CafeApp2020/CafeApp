package com.cafeapp.mycafe.use_case.data

import com.cafeapp.mycafe.frameworks.room.TableEntity
import com.cafeapp.mycafe.use_case.repositories.ITableRepository
import io.reactivex.Completable
import io.reactivex.Observable


class TableRepository(val tableDataSource :ITableDataSource):ITableRepository {
    override fun saveTable(tableEntity: TableEntity): Completable {
        return tableDataSource.saveTable(tableEntity)
    }

    override fun getAll(): Observable<List<TableEntity>> {
        return tableDataSource.getAll()
    }

    override fun removeTable(tableEntity: TableEntity): Completable {
        return tableDataSource.removeTable(tableEntity)
    }

    override fun updateTable(tableEntity: TableEntity?): Completable {
        return tableDataSource.updateTable(tableEntity)
    }
}