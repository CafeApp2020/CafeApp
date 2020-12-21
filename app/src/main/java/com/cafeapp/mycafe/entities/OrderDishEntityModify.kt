package com.cafeapp.mycafe.entities

// класс для маппинга совместных данных таблиц OrderDishEntity и DishesEntity
data class OrderDishEntityModify(
    var orderDishEntityId:Long=0,
    var order_id: Long = 0,
    var dish_id: Long = 0,
    var dishCount: Int = 0,
    var dishName: String? =null,
    var dishPrice: Float? = null
)