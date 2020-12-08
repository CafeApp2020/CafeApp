package com.cafeapp.mycafe.use_case.interactors.orderlist

import com.cafeapp.mycafe.frameworks.room.OrdersEntity
import com.cafeapp.mycafe.use_case.repositories.IOrderRepository
import io.reactivex.Completable
import io.reactivex.Single

class OrderInteractor(val repository: IOrderRepository) : IOrderInteractor {
    override fun deleteOrder(order: OrdersEntity): Completable {
        return repository.deleteOrder(order)
    }

    override fun getAllOrders(): Single<List<OrdersEntity?>?>? {
        return repository.getAllOrders()
    }

    override fun getAllDelivery(): Single<List<OrdersEntity?>?>? {
        return repository.getAllDelivery()
    }

    override fun loadOrder(orderId: Long): Single<OrdersEntity> {
        return repository.loadOrder(orderId)
    }

    override fun saveOrder(orderEntity: OrdersEntity): Single<Long>? {
        return repository.saveOrder(orderEntity)
    }

    override fun updateOrder(orderEntity: OrdersEntity): Completable {
        return repository.updateOrder(orderEntity)
    }
}