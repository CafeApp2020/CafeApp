package com.cafeapp.mycafe.frameworks.room

import com.cafeapp.mycafe.use_case.data.ITableDataSource
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class TableDataSource(val tablesDao: TablesDao): ITableDataSource {
    override fun saveTable(tableEntity: TableEntity): Completable {
       return tablesDao.addTable(tableEntity)
    }

    override fun getAll(): Observable<List<TableEntity>> {
        return tablesDao.getAll()
    }

    override fun removeTable(tableEntity: TableEntity): Completable {
        return tablesDao.removeTable(tableEntity)
    }

    override fun updateTable(tableEntity: TableEntity?): Completable {
        return tablesDao.updateTable(tableEntity)
    }
}