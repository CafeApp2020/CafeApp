package com.cafeapp.mycafe.use_case.data

import com.cafeapp.mycafe.frameworks.room.OrdersEntity
import io.reactivex.Completable
import io.reactivex.Single

interface IOrderDataSource {
    fun deleteOrder(order: OrdersEntity): Completable
    fun getAllOrders(): Single<List<OrdersEntity?>?>?
    fun getAllDelivery(): Single<List<OrdersEntity?>?>?
    fun loadOrder(orderId: Long): Single<OrdersEntity>
    fun saveOrder(orderEntity: OrdersEntity): Single<Long>?
    fun updateOrder(orderEntity: OrdersEntity): Completable
}