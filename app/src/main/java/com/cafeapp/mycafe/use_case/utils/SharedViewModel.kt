package com.cafeapp.mycafe.use_case.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// SharedViewModel - предназначен для обмена сообщениями между фрагментами
// можно почитать про это тут (Передача данных между фрагментами) : https://startandroid.ru/ru/courses/architecture-components/27-course/architecture-components/527-urok-4-viewmodel.html

class SharedViewModel : ViewModel() {
    private val selected = MutableLiveData<SharedMsg>()
    fun select(msg: SharedMsg) {
        selected.value = msg
    }

    fun getSelected(): LiveData<SharedMsg> {
        return selected
    }
}