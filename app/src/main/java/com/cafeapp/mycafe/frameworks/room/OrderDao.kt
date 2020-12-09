package com.cafeapp.mycafe.frameworks.room

import androidx.room.*
import com.cafeapp.mycafe.frameworks.view.deliveryadd.OrderType
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface OrderDao {
    @Query("SELECT * FROM OrdersEntity")
    fun all(): Single<List<OrdersEntity?>?>?

    @Query("SELECT * FROM OrdersEntity where ordertype=:deliveryType")
    fun allDelivery(deliveryType: String =OrderType.DELIVERY.toString()): Single<List<OrdersEntity?>?>?

     @Delete
    fun delete(entity: OrdersEntity): Completable

    @Query("SELECT * FROM OrdersEntity where id=:orderId")
    fun getOrder(orderId: Long): Single<OrdersEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: OrdersEntity?): Single<Long>?

    @Update
    fun update(entity: OrdersEntity): Completable
}