package com.cafeapp.mycafe.use_case.interactors.orders

import com.cafeapp.mycafe.entities.OrderDishEntityModify
import com.cafeapp.mycafe.frameworks.room.OrderDishEntity
import com.cafeapp.mycafe.frameworks.room.OrdersEntity
import com.cafeapp.mycafe.use_case.repositories.IOrderRepository
import io.reactivex.Completable
import io.reactivex.Observable
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

    override fun insertOrderListId(
        orderId: Long,
        selectedDishList: MutableList<Long>,
    ) : Observable<List<OrderDishEntityModify>> {
        var orderDishEntityList = mutableListOf<OrderDishEntity>()
        with(selectedDishList) {
            for (i in 0..size - 1)
                orderDishEntityList.add(OrderDishEntity(order_id = orderId,
                    dish_id = get(i), dishCount = 1))
        }
        val insertedList = repository.insertOrderListId(orderDishEntityList) // вставляем в базу данных id выбранных блюд
        val orderList = repository.getOrderDishList(orderId)     // и сразу запрашиваем все блюда по этому заказу
        return Observable.concat(insertedList.toObservable(), orderList)
    }
}