package com.cafeapp.mycafe.interface_adapters.viewmodels.categories

import com.less.repository.db.room.CategoryEntity

class CategoryAddViewState(
    val category: CategoryEntity? = null,
    val categoryList: List<CategoryEntity?>? = null,
    val delError: Throwable? = null,
    val delOk: Boolean = false,
    val error: Throwable? = null,
    val loadOk: Boolean = false,
    val saveErr: Throwable? = null,
    val saveOk: Boolean = false
)