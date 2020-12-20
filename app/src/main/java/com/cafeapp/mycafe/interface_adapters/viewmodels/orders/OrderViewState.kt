package com.cafeapp.mycafe.interface_adapters.viewmodels.orders

import com.cafeapp.mycafe.entities.OrderDishEntityModify
import com.cafeapp.mycafe.frameworks.room.OrdersEntity

class OrderViewState(
    val delOk: Boolean = false,
    val ordersEntityID: Long = 0,
    val orderDishEntityModifyList: List<OrderDishEntityModify>? = null,
    val ordersEntity: OrdersEntity? = null,
    val orderList: List<OrdersEntity?>? = null,
    val error: Throwable? = null,
    val loadOk: Boolean = false,
    val saveOk: Boolean = false,
)