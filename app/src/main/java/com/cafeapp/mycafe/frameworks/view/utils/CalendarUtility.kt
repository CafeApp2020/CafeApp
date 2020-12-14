package com.cafeapp.mycafe.frameworks.view.utils

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.text.format.DateUtils
import android.view.View
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class CalendarUtility(val activity: Activity,
                      val dateTIT: TextInputEditText,
                      val timeTIT: TextInputEditText,
                      var calendar: Calendar = Calendar.getInstance()) {

    init {
        setInitialDateTime()
    }

    // установка начальных даты и времени
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

    // отображаем диалоговое окно для выбора даты
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

    // установка обработчика выбора времени
    val timePickerDialog =
        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            calendar.add(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.add(Calendar.MINUTE, minute)
            setInitialDateTime()
        }

    // установка обработчика выбора даты
    val datePickerDialog =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calendar.add(Calendar.YEAR, year)
            calendar.add(Calendar.MONTH, monthOfYear)
            calendar.add(
                Calendar.DAY_OF_MONTH,
                dayOfMonth
            )
            setInitialDateTime()
        }


    // отображаем диалоговое окно для выбора времени
    fun setTime(v: View?) {
        TimePickerDialog(
            activity, timePickerDialog,
            calendar[Calendar.HOUR_OF_DAY],
            calendar[Calendar.MINUTE], true
        )
            .show()
    }

}