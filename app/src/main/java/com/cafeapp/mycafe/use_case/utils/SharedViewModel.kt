package com.cafeapp.mycafe.use_case.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// SharedViewModel - предназначен для обмена сообщениями между фрагментами

class SharedViewModel : ViewModel() {
    private val selected = MutableLiveData<SharedMsg>()
    fun select(msg: SharedMsg) {
        selected.value = msg
    }

    fun getSelected(): LiveData<SharedMsg> {
        return selected
    }
}