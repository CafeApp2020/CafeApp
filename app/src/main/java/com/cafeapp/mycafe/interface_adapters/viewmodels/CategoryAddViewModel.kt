package com.cafeapp.mycafe.interface_adapters.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CategoryAddViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Экран для добавления/редактирования категорий блюд"
    }
    val text: LiveData<String> = _text
}