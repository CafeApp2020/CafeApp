package com.cafeapp.mycafe.interface_adapters.viewmodels.tables

import com.cafeapp.mycafe.frameworks.room.TableEntity

class TableViewState(
    val delOk: Boolean = false,
    val tableEntity: TableEntity? = null,
    val tableList: List<TableEntity?>? = null,
    val error: Throwable? = null,
    val saveOk: Boolean = false,
)