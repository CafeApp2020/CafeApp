package com.cafeapp.mycafe.interface_adapters.viewmodels.orders

import com.cafeapp.mycafe.entities.OrderDishEntityModify
import com.cafeapp.mycafe.frameworks.room.OrdersEntity
import com.cafeapp.mycafe.interface_adapters.viewmodels.baseviewmodel.BaseViewState

class OrderViewState(
    val ordersEntityID: Long = 0,
    val orderDishEntityModifyList: List<OrderDishEntityModify>? = null,
    val ordersEntity: OrdersEntity? = null,
    val orderList: List<OrdersEntity?>? = null,
    override val error: Throwable? = null,
    val loadOk: Boolean = false,
    val saveOk: Boolean = false,
): BaseViewState()