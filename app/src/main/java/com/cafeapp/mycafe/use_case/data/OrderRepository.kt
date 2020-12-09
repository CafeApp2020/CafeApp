package com.cafeapp.mycafe.use_case.data

import com.cafeapp.mycafe.frameworks.room.OrdersEntity
import com.cafeapp.mycafe.use_case.repositories.IOrderRepository
import io.reactivex.Completable
import io.reactivex.Single

class OrderRepository(val dataSource: IOrderDataSource) : IOrderRepository {
    override fun deleteOrder(order: OrdersEntity): Completable {
        return dataSource.deleteOrder(order)
    }

    override fun getAllOrders(): Single<List<OrdersEntity?>?>? {
        return dataSource.getAllOrders()
    }

    override fun getAllDelivery(): Single<List<OrdersEntity?>?>? {
        return dataSource.getAllDelivery()
    }

    override fun loadOrder(orderId: Long): Single<OrdersEntity> {
        return dataSource.loadOrder(orderId)
    }

    override fun saveOrder(orderEntity: OrdersEntity): Single<Long>? {
        return dataSource.saveOrder(orderEntity)
    }

    override fun updateOrder(orderEntity: OrdersEntity): Completable {
        return dataSource.updateOrder(orderEntity)
    }
}