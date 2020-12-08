package com.cafeapp.mycafe.frameworks.view.deliveryadd

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cafeapp.mycafe.R
import kotlinx.android.synthetic.main.fragment_customerdata.*
import java.util.*


class CustomerDataFragment : Fragment() {
    var deliveryDateTime = Calendar.getInstance()

    var dateAndTime: Calendar = Calendar.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(R.layout.fragment_customerdata, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {

            Toast.makeText(activity, "CustomerDataFragment", Toast.LENGTH_LONG).show()
        }

        arguments?.takeIf { it.containsKey(ARG_DATE) }?.apply {
            Toast.makeText(activity, "DeliveryAddFragment", Toast.LENGTH_LONG).show()
            this.putString(ARG_OBJECT, deliveryDateTime.toString())
        }

        deliveryTimeTIT.setOnFocusChangeListener { view: View, b: Boolean ->
           if (b) setTime(view)
        }

        deliveryDateTIT.setOnFocusChangeListener { view: View, b: Boolean ->
            if (b) setDate(view)
        }
        setInitialDateTime()
    }

    // установка обработчика выбора времени
    val timePickerDialog =
        OnTimeSetListener { view, hourOfDay, minute ->
            deliveryDateTime.add(Calendar.HOUR_OF_DAY, hourOfDay)
            deliveryDateTime.add(Calendar.MINUTE, minute)
            setInitialDateTime()
        }

    // установка обработчика выбора даты
    val datePickerDialog =
        OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            deliveryDateTime.add(Calendar.YEAR, year)
            deliveryDateTime.add(Calendar.MONTH, monthOfYear)
            deliveryDateTime.add(Calendar.DAY_OF_MONTH, dayOfMonth)
            setInitialDateTime()
        }

    // отображаем диалоговое окно для выбора даты
    fun setDate(v: View?) {
        activity?.let {
          val date = DatePickerDialog(
                it, datePickerDialog,
                dateAndTime[Calendar.YEAR],
                dateAndTime[Calendar.MONTH],
                dateAndTime[Calendar.DAY_OF_MONTH]
            )
            date.show()
            date.datePicker
        }
    }

    // отображаем диалоговое окно для выбора времени
    fun setTime(v: View?) {
        TimePickerDialog(
            activity, timePickerDialog,
            dateAndTime[Calendar.HOUR_OF_DAY],
            dateAndTime[Calendar.MINUTE], true
        )
            .show()
    }

    // установка начальных даты и времени
    private fun setInitialDateTime() {
        deliveryDateTIT.setText(
            DateUtils.formatDateTime(
                activity,
                dateAndTime.timeInMillis,
                DateUtils.FORMAT_SHOW_DATE
            )
        )

        deliveryTimeTIT.setText(
            DateUtils.formatDateTime(
                activity,
                dateAndTime.timeInMillis,
                DateUtils.FORMAT_SHOW_TIME
            )
        )
    }
}