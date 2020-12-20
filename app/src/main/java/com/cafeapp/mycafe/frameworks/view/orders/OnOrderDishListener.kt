package com.cafeapp.mycafe.frameworks.view.orders

import com.cafeapp.mycafe.entities.OrderDishEntityModify

interface OnOrderDishListener {
    fun onDishCountChangeButtonClick(orderDishEntity: OrderDishEntityModify)
}