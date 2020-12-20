package com.cafeapp.mycafe.frameworks.room

import androidx.room.*

@Entity(indices = arrayOf(Index(value = arrayOf("id"), unique = true)))
class TableEntity(
    @field:ColumnInfo(name = "tablename")
    var tablename: String,

    @field:ColumnInfo(name = "zalid")
    var zalid: Long=0
    ) {
        @field:PrimaryKey(autoGenerate = true)
        @field:ColumnInfo(name = "id")
        var id: Long = 0
}



