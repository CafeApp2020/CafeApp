package com.cafeapp.mycafe.frameworks.room

import com.cafeapp.mycafe.entities.OrderDishEntityModify
import com.cafeapp.mycafe.use_case.data.IOrderDataSource
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class OrderDataSource(val orderDao: OrderDao) : IOrderDataSource {
     override fun deleteOrder(order: OrdersEntity): Completable {
       return orderDao.delete(order)
    }

    override fun getAllOrders(): Single<List<OrdersEntity?>?>? {
        return orderDao.all()
    }

    override fun getAllDelivery(): Single<List<OrdersEntity?>?>? {
        return orderDao.allDelivery()
    }

    override fun loadOrder(orderId: Long): Single<OrdersEntity> {
        return orderDao.getOrder(orderId)
    }

    override fun saveOrder(orderEntity: OrdersEntity): Single<Long>? {
        return orderDao.insert(orderEntity)
    }

    override fun updateOrder(orderEntity: OrdersEntity): Completable {
        return orderDao.update(orderEntity)
    }

    override fun insertOrderListId(orderDishEntityList: MutableList<OrderDishEntity>): Completable {
        return orderDao.insertAll(orderDishEntityList)
    }

    override fun getOrderDishList(orderId: Long): Observable<List<OrderDishEntityModify>> {
        return orderDao.getOrderDishList(orderId)
    }
}