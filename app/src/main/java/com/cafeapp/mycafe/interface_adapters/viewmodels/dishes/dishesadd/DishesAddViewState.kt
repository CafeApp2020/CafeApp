package com.cafeapp.mycafe.interface_adapters.viewmodels.dishes.dishesadd

import com.less.repository.db.room.DishesEntity

class DishesAddViewState(
    val delError: Throwable? = null,
    val delOk: Boolean = false,
    val dish: DishesEntity? = null,
    val error: Throwable? = null,
    val saveErr: Throwable? = null,
    val saveOk: Boolean = false
)