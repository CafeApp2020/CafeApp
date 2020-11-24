package com.cafeapp.mycafe.interface_adapters.viewmodels.dishes.dish

import com.less.repository.db.room.DishesEntity

class DishesViewState(
    val delOk: Boolean = false,
    val dish: DishesEntity? = null,
    val error: Throwable? = null,
    val loadOk: Boolean = false,
    val saveOk: Boolean = false,
)