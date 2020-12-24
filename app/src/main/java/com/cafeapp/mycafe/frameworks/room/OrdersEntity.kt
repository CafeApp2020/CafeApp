package com.cafeapp.mycafe.frameworks.room

import androidx.room.*
import java.util.*


@Entity(indices = arrayOf(Index(value = arrayOf("id"), unique = true)))
class OrdersEntity(
    @field:ColumnInfo(name = "orderdatetime")
    // автообновляемое поле дата и время заказа
    var orderdatetime: Date? = null,

    @field:ColumnInfo(name = "oficiantid")
    var oficiantid: Int? = null,

    @field:ColumnInfo(name = "customername")
    // имя клиента
    var customername: String? = null,

    @field:ColumnInfo(name = "customerphone")
    // телефон заказчика
    var customerphone: String? = null,

    @field:ColumnInfo(name = "address")
    // адрес доставки
    var customeraddress: String? = null,

    @field:ColumnInfo(name = "datetime")
    // дата-время доставки
    var dateTime: Date? = null,

    @field:ColumnInfo(name = "isNearTime")
    var isNearTime: Boolean? = true,

    @field:ColumnInfo(name = "tableid")
    // id стола из таблицы со столами
    var tableid: Int? = null,

    @field:ColumnInfo(name = "ordertype")
    // тип заказа: 1 - в зале, 2 - с собой, 3 - доставка
    var ordertype: Int? = null,

    @field:ColumnInfo(name = "paided")
    // зака оплачен/не оплачен
    var paided: Boolean = false,
) {
    @field:PrimaryKey(autoGenerate = true)
    @field:ColumnInfo(name = "id")
    var id: Long = 0
}
