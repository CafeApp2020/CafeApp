package com.cafeapp.mycafe.frameworks.view.deliveryadd

const val ARG_OBJECT = "object"
const val ARG_DATE = "selected_date"

class OrderType {
    companion object {
       val INROOM:Int=1
       val TAKEAWAY:Int=2
       val  DELIVERY:Int=3
    }
}