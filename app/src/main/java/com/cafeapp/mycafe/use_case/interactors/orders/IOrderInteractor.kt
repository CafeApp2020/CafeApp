package com.cafeapp.mycafe.use_case.interactors.orders

import com.cafeapp.mycafe.entities.OrderDishEntityModify
import com.cafeapp.mycafe.frameworks.room.OrdersEntity
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface IOrderInteractor {
    fun deleteOrder(order: OrdersEntity): Completable
    fun getAllOrders(): Single<List<OrdersEntity?>?>?
    fun getAllDelivery(): Single<List<OrdersEntity?>?>?
    fun loadOrder(orderId: Long): Single<OrdersEntity>
    fun saveOrder(orderEntity: OrdersEntity): Single<Long>?
    fun updateOrder(orderEntity: OrdersEntity): Completable
    fun insertOrderListId(orderId: Long, selectedDishList: MutableList<Long>): Observable<List<OrderDishEntityModify>>
    fun loadDishListForOrder(orderId: Long) : Observable<List<OrderDishEntityModify>>
    fun getTotalSum(dishList: List<OrderDishEntityModify>): Double
    fun updateOrderDishEntityModify(orderDishEntity: OrderDishEntityModify): Completable
}