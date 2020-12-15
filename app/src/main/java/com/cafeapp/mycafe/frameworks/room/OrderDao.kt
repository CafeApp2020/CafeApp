package com.cafeapp.mycafe.frameworks.room

import androidx.room.*
import com.cafeapp.mycafe.entities.OrderDishEntityModify
import com.cafeapp.mycafe.frameworks.view.delivery.OrderType
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface OrderDao {
    @Query("SELECT * FROM OrdersEntity")
    fun all(): Single<List<OrdersEntity?>?>?

    @Query("SELECT * FROM OrdersEntity where ordertype=:deliveryType")
    fun allDelivery(deliveryType: String = OrderType.DELIVERY.toString()): Single<List<OrdersEntity?>?>?

    @Delete
    fun delete(entity: OrdersEntity): Completable

    @Query("SELECT * FROM OrdersEntity where id=:orderId")
    fun getOrder(orderId: Long): Single<OrdersEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: OrdersEntity?): Single<Long>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(OrderDishEntity: List<OrderDishEntity>?): Completable

    @Update
    fun update(entity: OrdersEntity): Completable

    @Query("SELECT OrderDishEntity.order_id, OrderDishEntity.dish_id, " +
                  "OrderDishEntity.dishCount, DishesEntity.name as dishName, DishesEntity.price as dishPrice" +
                  " FROM OrderDishEntity Left Join DishesEntity on OrderDishEntity.dish_id=DishesEntity.id" +
                  " where OrderDishEntity.order_id=:orderId")
    fun getOrderDishList(orderId: Long): Observable<List<OrderDishEntityModify>>
}