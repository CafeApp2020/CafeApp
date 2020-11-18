package com.cafeapp.mycafe.interface_adapters.viewmodels.dishes.dishesadd

import com.less.repository.db.room.DishesEntity

class DishesAddViewState(val dish: DishesEntity? = null,
                    val error: Throwable? = null,
                    val delOk:Boolean=false ,
                    val delError:Throwable?=null,
                    val saveOk:Boolean=false,
                    val saveErr:Throwable?=null) {
}