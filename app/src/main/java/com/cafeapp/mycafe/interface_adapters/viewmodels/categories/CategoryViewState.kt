package com.cafeapp.mycafe.interface_adapters.viewmodels.categories

import com.cafeapp.mycafe.interface_adapters.viewmodels.baseviewmodel.BaseViewState
import com.less.repository.db.room.CategoryEntity

class CategoryViewState(
    val category: CategoryEntity? = null,
    val categoryList: List<CategoryEntity?>? = null,
    override val error: Throwable? = null,
    val loadOk: Boolean = false,
    val saveOk: Boolean = false
): BaseViewState()