package com.cafeapp.mycafe.interface_adapters.viewmodels.delivery

import com.cafeapp.mycafe.frameworks.room.OrdersEntity

class DeliveryAddViewState(
    val delOk: Boolean = false,
    val ordersEntity: OrdersEntity? = null,
    val error: Throwable? = null,
    val loadOk: Boolean = false,
    val saveOk: Boolean = false,
)