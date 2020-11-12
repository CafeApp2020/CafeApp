package com.cafeapp.mycafe.interface_adapters.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DishesAddViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "Здесь будет экран для редактирования/добавления блюда"
    }
    val text: LiveData<String> = _text
}