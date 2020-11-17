package com.cafeapp.mycafe.interface_adapters.viewmodels.dishes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DishesListViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Здесь будет список блюд"
    }
    val text: LiveData<String> = _text
}