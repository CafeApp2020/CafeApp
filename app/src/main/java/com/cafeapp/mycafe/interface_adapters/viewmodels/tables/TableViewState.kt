package com.cafeapp.mycafe.interface_adapters.viewmodels.tables

import com.cafeapp.mycafe.frameworks.room.TableEntity
import com.cafeapp.mycafe.interface_adapters.viewmodels.baseviewmodel.BaseViewState

class TableViewState(val tableList: List<TableEntity?>? = null,
                     val tableEntity: TableEntity? = null,
                     val saveOk: Boolean = false,
                     override val error: Throwable? = null)
                     : BaseViewState()