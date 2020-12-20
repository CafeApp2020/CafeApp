package com.cafeapp.mycafe.frameworks.room

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
    interface TablesDao {
        @Query("SELECT * FROM TableEntity")
        fun getAll(): Observable<List<TableEntity>>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun addTable(tableEntity: TableEntity): Completable

        @Delete
        fun removeTable(tableEntity: TableEntity): Completable

        @Update
        fun updateTable(tableEntity: TableEntity?): Completable
}
