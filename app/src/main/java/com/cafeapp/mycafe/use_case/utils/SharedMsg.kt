package com.cafeapp.mycafe.use_case.utils

// MsgState - перечисление MsgState используется для указания типа сообщения
enum class MsgState {
    ADDCATEGORY,
    ADDDISH,
    DISHESLIST
}

// SharedMsg используется для обмена сообщениями между фрагментами.
// по параметру stateName фрагмент "понимает", что за сообщение, val - передаваемое значение
data class SharedMsg(val stateName: MsgState, val value: Any)
