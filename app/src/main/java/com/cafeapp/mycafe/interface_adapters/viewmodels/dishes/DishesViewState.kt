package com.cafeapp.mycafe.interface_adapters.viewmodels.dishes

import com.cafeapp.mycafe.interface_adapters.viewmodels.baseviewmodel.BaseViewState
import com.less.repository.db.room.DishesEntity

class DishesViewState(
    val dish: DishesEntity? = null,
    val dishList: List<DishesEntity?>? = null,
    override val error: Throwable? = null,
    val saveOk: Boolean = false,
    val loadOk: Boolean = false
): BaseViewState()