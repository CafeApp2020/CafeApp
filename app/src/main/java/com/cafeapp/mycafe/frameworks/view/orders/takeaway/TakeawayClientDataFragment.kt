package com.cafeapp.mycafe.frameworks.view.orders.takeaway

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
import kotlinx.android.synthetic.main.fragment_takeaway_client_data.*

class TakeawayClientDataFragment(private val orderViewModel: OrderViewModel) : Fragment() {
    companion object {
        private lateinit var takeawayDateTimeCalender: CalendarUtility
    }

    private val sharedViewModel by lazy {
        activity?.let {
            ViewModelProvider(it).get(SharedViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_takeaway_client_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSharedViewModelObserver()
        initDateTimeCalendar()

        checkDateFocus()
        checkTimeFocus()
        checkSwitch()

        onFocusChange(fragment_takeaway_client_data_customerNameTIT,
            getString(R.string.enter_customer_name))
        onFocusChange(fragment_takeaway_client_data_customerPhoneTIT,
            getString(R.string.enter_phone_number))
    }

    private fun checkSwitch() {
        fragment_takeaway_client_data_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                fragment_takeaway_client_data_date_time_container.visibility = View.GONE
                saveOrder()
            } else {
                fragment_takeaway_client_data_date_time_container.visibility = View.VISIBLE
                saveOrder()
            }
        }
    }

    private fun onFocusChange(textView: TextView, message: String) {
        textView.setOnFocusChangeListener { view: View, focused: Boolean ->
            if (!focused) {
                if (!isError(textView, message)) {
                    saveOrder()
                }
            }
        }
    }

    private fun initSharedViewModelObserver() {
        sharedViewModel?.getSelected()?.observe(viewLifecycleOwner) { msg ->
            when (msg.stateName) {
                MsgState.TAKEAWAYADD -> {
                    SelectedOrder.currentOrder = OrdersEntity()
                    saveOrder()
                }

                MsgState.TAKEAWAYOPEN -> {
                    if (msg.value is OrdersEntity)
                        initViews(msg.value)

                    if (msg.value is Map<*, *>)
                        loadOrderFromDishes(msg.value)
                }
            }
        }
    }

    private fun initDateTimeCalendar() {
        takeawayDateTimeCalender = activity?.let {
            CalendarUtility(it,
                fragment_takeaway_client_data_dateTIT,
                fragment_takeaway_client_data_timeTIT)
        }!!
    }

    private fun loadOrderFromDishes(value: Any) {
        val selectedDishMap = value as Map<OrdersEntity, MutableList<Long>>
        val iterator: Iterator<OrdersEntity> = selectedDishMap.keys.iterator()
        val order = iterator.next()

        initViews(order)
    }

    private fun checkTimeFocus() {
        fragment_takeaway_client_data_timeTIT.setOnFocusChangeListener { view: View, focused: Boolean ->
            if (focused)
                takeawayDateTimeCalender.setTime(view)
            else
                saveOrder()

        }
    }

    private fun checkDateFocus() {
        fragment_takeaway_client_data_dateTIT.setOnFocusChangeListener { view: View, focused: Boolean ->
            if (focused)
                takeawayDateTimeCalender.setDate(view)
            else
                saveOrder()

        }
    }

    private fun initViews(order: OrdersEntity) {
        SelectedOrder.currentOrder = order

        val name: TextView = fragment_takeaway_client_data_customerNameTIT
        val phone: TextView = fragment_takeaway_client_data_customerPhoneTIT
        val switch = fragment_takeaway_client_data_switch

        name.text = order.customername
        phone.text = order.customerphone
        switch.isChecked = order.isNearTime!!

        fragment_takeaway_client_data_dateTIT.setText(order.dateTime?.let {
            CalendarUtility.getDateStr(it)
        })

        fragment_takeaway_client_data_timeTIT.setText(order.dateTime?.let {
            CalendarUtility.getTimeStr(it)
        })
    }

    private fun saveOrder() {
        val customerName =
            fragment_takeaway_client_data_customerNameTIT?.let { it.text.toString() }
                ?: ""
        val customerPhone =
            fragment_takeaway_client_data_customerPhoneTIT?.let { it.text.toString() }
                ?: ""

        val order = OrdersEntity(
            customername = customerName,
            customerphone = customerPhone,
            dateTime = takeawayDateTimeCalender.calendar.time,
            ordertype = OrderType.TAKEAWAY
        )

        if (SelectedOrder.currentOrder.id > 0)
            order.id = SelectedOrder.currentOrder.id

        SelectedOrder.currentOrder = order
        order.isNearTime = fragment_takeaway_client_data_switch?.isChecked

        orderViewModel.saveOrder(order)
    }
}