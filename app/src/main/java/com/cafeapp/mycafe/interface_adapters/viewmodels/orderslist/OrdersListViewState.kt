package com.cafeapp.mycafe.interface_adapters.viewmodels.orderslist

import com.cafeapp.mycafe.frameworks.room.OrdersEntity

class OrdersListViewState(
    val orderList: List<OrdersEntity?>? = null,
    val delOk: Boolean = false,
    val error: Throwable? = null,
    val loadOk: Boolean = false,
    val saveOk: Boolean = false,
)