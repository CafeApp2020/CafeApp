package com.cafeapp.mycafe.frameworks.view.delivery

import com.cafeapp.mycafe.frameworks.room.OrdersEntity

class OrderType {
    companion object {
        val INROOM: Int = 1
        val TAKEAWAY: Int = 2
        val DELIVERY: Int = 3
    }
}

class SelectedOrder {
    companion object {
       // lateinit var currentOrder: OrdersEntity
       var currentOrder = OrdersEntity()
    }
}