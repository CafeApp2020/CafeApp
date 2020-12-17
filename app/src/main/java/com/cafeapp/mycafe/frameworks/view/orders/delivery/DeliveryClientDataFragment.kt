package com.cafeapp.mycafe.frameworks.view.orders.delivery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.frameworks.room.OrdersEntity
import com.cafeapp.mycafe.frameworks.view.delivery.OrderType
import com.cafeapp.mycafe.frameworks.view.delivery.SelectedOrder
import com.cafeapp.mycafe.frameworks.view.utils.CalendarUtility
import com.cafeapp.mycafe.interface_adapters.viewmodels.orders.OrderViewModel
import com.cafeapp.mycafe.use_case.utils.MsgState
import com.cafeapp.mycafe.use_case.utils.SharedViewModel
import com.cafeapp.mycafe.use_case.utils.isError
import kotlinx.android.synthetic.main.fragment_delivery_client_data.*

class DeliveryClientDataFragment(private val orderViewModel: OrderViewModel) : Fragment() {
    companion object {
        lateinit var deliveryDateTimeCalendar: CalendarUtility
    }

    private val sharedModel by lazy {
        activity?.let { ViewModelProvider(it).get(SharedViewModel::class.java) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        initSharedModelObserver()
        return inflater.inflate(R.layout.fragment_delivery_client_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        deliveryDateTimeCalendar = activity?.let {
            CalendarUtility(it, deliveryDateTIT, deliveryTimeTIT)
        }!!

        deliveryTimeTIT.setOnFocusChangeListener { view: View, focused: Boolean ->
            if (focused) deliveryDateTimeCalendar.setTime(view)
            else saveDelivery()
        }

        deliveryDateTIT.setOnFocusChangeListener { view: View, focused: Boolean ->
            if (focused) deliveryDateTimeCalendar.setDate(view)
            else saveDelivery()
        }

        onFocusChange(customerNameTIT, getString(R.string.enter_customer_name))
        onFocusChange(customerPhoneTIT, getString(R.string.enter_phone_number))
        onFocusChange(customerAddressTIT, getString(R.string.enter_address))
    }

    private fun onFocusChange(textView: TextView, message: String) {
        textView.setOnFocusChangeListener { view: View, focused: Boolean ->
            if (!focused) {
                if (!isError(textView, message)) {
                    saveDelivery()
                }
            }
        }
    }

    private fun initSharedModelObserver() {
        sharedModel?.getSelected()?.observe(viewLifecycleOwner) { msg ->
            when (msg.stateName) {
                MsgState.DELIVERYADD -> {
                    SelectedOrder.currentOrder = OrdersEntity()
                    saveDelivery()
                }
                MsgState.DELEVERYOPEN -> {
                    if (msg.value is OrdersEntity)
                        viewSetData(msg.value)
                    if (msg.value is Map<*, *>)
                        loadDeliveryFromDishes(msg.value)
                }
            }
        }
    }

    private fun loadDeliveryFromDishes(value: Any) {
        val selectedDishMap = value as Map<OrdersEntity, MutableList<Long>>
        val iterator: Iterator<OrdersEntity> = selectedDishMap.keys.iterator()
        val order = iterator.next()
        viewSetData(order)
    }

    private fun viewSetData(order: OrdersEntity) {
        order.let {
            SelectedOrder.currentOrder = order
            customerNameTIT.setText(order.customername)
            customerPhoneTIT.setText(order.customerphone)
            customerAddressTIT.setText(order.customeraddress)
            deliveryDateTIT.setText(order.deliverydatetime?.let { it1 ->
                CalendarUtility.getDateStr(it1)
            })
            deliveryTimeTIT.setText(order.deliverydatetime?.let { it1 ->
                CalendarUtility.getTimeStr(it1)
            })
        }
    }

    private fun saveDelivery() {
        val customerName = customerNameTIT?.let { customerNameTIT.text.toString() } ?: ""
        val customerPhone = customerPhoneTIT?.let { customerPhoneTIT.text.toString() } ?: ""
        val customerAddress = customerAddressTIT?.let { customerAddressTIT.text.toString() } ?: ""

        val orderDelivery = OrdersEntity(
            customername = customerName,
            customerphone = customerPhone,
            customeraddress = customerAddress,
            deliverydatetime = deliveryDateTimeCalendar.calendar.time,
            ordertype = OrderType.DELIVERY
        )

        if (SelectedOrder.currentOrder.id > 0)
            orderDelivery.id = SelectedOrder.currentOrder.id

        SelectedOrder.currentOrder = orderDelivery
        orderViewModel.saveDelivery(orderDelivery)
    }
}