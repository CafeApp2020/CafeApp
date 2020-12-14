package com.cafeapp.mycafe.frameworks.view.orders.delivery

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kotlinx.android.synthetic.main.fragment_delivery_client_data.*
import java.util.*


class DeliveryClientDataFragment(val orderViewModel: OrderViewModel) : Fragment() {
    companion object {
        lateinit var deliveryDateTimeCalendar : CalendarUtility
     //  var deliveryDateTimeCalendar : Calendar=Calendar.getInstance()
      }

    private val sharedModel by lazy {
        activity?.let { ViewModelProvider(it).get(SharedViewModel::class.java) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initSharedModelObserver()
        return inflater.inflate(R.layout.fragment_delivery_client_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        deliveryDateTimeCalendar = activity?.let {
             CalendarUtility(it, deliveryDateTIT, deliveryTimeTIT)}!!

        deliveryTimeTIT.setOnFocusChangeListener { view: View, focused: Boolean ->
           if (focused) deliveryDateTimeCalendar.setTime(view)
          //  if (focused) setTime(view)
           else saveDelivery()
        }

        deliveryDateTIT.setOnFocusChangeListener { view: View, focused: Boolean ->
            if (focused) deliveryDateTimeCalendar.setDate(view)
          //  if (focused) setDate(view)
            else saveDelivery()
        }
       // setInitialDateTime()
        onFocusChange(customerNameTIT)
        onFocusChange(customerPhoneTIT)
        onFocusChange(customerAddressTIL)
    }

    private fun onFocusChange(view:View)  {
        view.setOnFocusChangeListener{ view:View, focused: Boolean ->
               if (!focused) {
                   saveDelivery()
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
            }
        }
    }

    private fun saveDelivery() {
        val customerName=customerNameTIT?.let{customerNameTIT.text.toString()} ?: ""
        val customerphone=customerPhoneTIT?.let{customerPhoneTIT.text.toString()} ?: ""
        val customeraddress=customerAddressTIT?.let{customerAddressTIT.text.toString()} ?: ""

        var orderDelivery = OrdersEntity(
            customername = customerName,
            customerphone = customerphone,
            customeraddress = customeraddress,
          //  deliverydatetime = deliveryDateTimeCalendar.calendar.time,
            ordertype= OrderType.DELIVERY
        )
        if (SelectedOrder.currentOrder.id>0)
            orderDelivery.id= SelectedOrder.currentOrder.id
        SelectedOrder.currentOrder =orderDelivery
        orderViewModel.saveDelivery(orderDelivery)
    }


  /*  // установка обработчика выбора времени
    val timePickerDialog =
        OnTimeSetListener { view, hourOfDay, minute ->
            deliveryDateTimeCalendar.add(Calendar.HOUR_OF_DAY, hourOfDay)
            deliveryDateTimeCalendar.add(Calendar.MINUTE, minute)
            setInitialDateTime()
        }

    // установка обработчика выбора даты
    val datePickerDialog =
        OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            deliveryDateTimeCalendar.add(Calendar.YEAR, year)
            deliveryDateTimeCalendar.add(Calendar.MONTH, monthOfYear)
            deliveryDateTimeCalendar.add(Calendar.DAY_OF_MONTH, dayOfMonth)
            setInitialDateTime()
        }


     // отображаем диалоговое окно для выбора даты
    fun setDate(v: View?) {
        activity?.let {
          val date = DatePickerDialog(
                it, datePickerDialog,
              deliveryDateTimeCalendar[Calendar.YEAR],
              deliveryDateTimeCalendar[Calendar.MONTH],
              deliveryDateTimeCalendar[Calendar.DAY_OF_MONTH]
            )
            date.show()
            date.datePicker
        }
    }

    // отображаем диалоговое окно для выбора времени
    fun setTime(v: View?) {
        TimePickerDialog(
            activity, timePickerDialog,
            deliveryDateTimeCalendar[Calendar.HOUR_OF_DAY],
            deliveryDateTimeCalendar[Calendar.MINUTE], true
        )
            .show()
    }

    // установка начальных даты и времени
    private fun setInitialDateTime() {
        deliveryDateTIT.setText(
            DateUtils.formatDateTime(
                activity,
                deliveryDateTimeCalendar.timeInMillis,
                DateUtils.FORMAT_SHOW_DATE
            )
        )

        deliveryTimeTIT.setText(
            DateUtils.formatDateTime(
                activity,
                deliveryDateTimeCalendar.timeInMillis,
                DateUtils.FORMAT_SHOW_TIME
            )
        )
    }*/
}