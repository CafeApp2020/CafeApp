package com.cafeapp.mycafe.use_case.utils

// MsgState - перечисление MsgState используется для указания типа сообщения
enum class MsgState {
    ADDCATEGORY, // открыть окно добавления/редактирования новой категории
    ADDDISH, //  открыть окно добавления/редактрования блюда
    CATEGORYLISTOPEN,  //   открываем окно со списком категорий
    OPENDISH, // открываем окно с блюдом для просмотра
    DISHESLIST, // открыть окно списка блюд
    EDITDISH,   // открываем окно с блюдом для редактирования
    SETTOOLBARTITLE, // заголовок в верхнем toolbar
    DELIVERYADD,  // открываем окно добавления доставки
    DELEVERYOPEN, // открывем существующую доставку
    TAKEAWAYADD, // открываем окно добавления заказа на вынос
    TAKEAWAYOPEN, // открываем существующий заказ на вынос
    SELECTDISHTOORDER, // открываем окно с категориями блюд в режиме добавления блюд к заказам
    OPENFORORDER, // открываем список блюд в режиме добавления к заказу
    RETURNSELECTEDDISHLIST // возвращаем заказу список выбранных блюд
}

// SharedMsg используется для обмена сообщениями между фрагментами.
// по параметру stateName фрагмент "понимает", что за сообщение, val - передаваемое значение
data class SharedMsg(val stateName: MsgState, val value: Any)
