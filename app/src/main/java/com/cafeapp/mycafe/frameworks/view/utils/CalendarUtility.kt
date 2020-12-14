package com.cafeapp.mycafe.frameworks.view.utils

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.text.format.DateUtils
import android.view.View
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*


class CalendarUtility(
    val activity: Activity,
    val dateTIT: TextInputEditText,
    val timeTIT: TextInputEditText,
    var calendar: Calendar = Calendar.getInstance(),
) {

    companion object {
        var dateFormat: SimpleDateFormat=SimpleDateFormat("dd.MM.yyyy")
        var timeFormat: SimpleDateFormat=SimpleDateFormat("HH:mm")
        fun getDateStr(date:Date):String=dateFormat.format(date)
        fun getTimeStr(date:Date):String=timeFormat.format(date)
    }

    init {
        setInitialDateTime()
    }

    private fun setInitialDateTime() {
        dateTIT.setText(
            DateUtils.formatDateTime(
                activity,
                calendar.timeInMillis,
                DateUtils.FORMAT_SHOW_DATE
            )
        )

        timeTIT.setText(
            DateUtils.formatDateTime(
                activity,
                calendar.timeInMillis,
                DateUtils.FORMAT_SHOW_TIME
            )
        )
    }

    fun setDate(v: View?) {
        activity?.let {
            val date = DatePickerDialog(
                it, datePickerDialog,
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
            )
            date.show()
            date.datePicker
        }
    }

    val timePickerDialog =
        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            setInitialDateTime()
        }


    var datePickerDialog =
        OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            setInitialDateTime()
        }

    fun setTime(v: View?) {
        TimePickerDialog(
            activity, timePickerDialog,
            calendar[Calendar.HOUR_OF_DAY],
            calendar[Calendar.MINUTE], true
        )
            .show()
    }

}