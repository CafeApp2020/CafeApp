package com.cafeapp.mycafe.frameworks.view.tables

import com.cafeapp.mycafe.frameworks.room.TableEntity

interface OnTableListItemClickListener {
    fun onEditTableButtonClick(tableEntity: TableEntity)
    fun onRemoveTableButtonClick(tableEntity: TableEntity)
}