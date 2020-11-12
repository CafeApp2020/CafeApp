package com.cafeapp.mycafe.interface_adapters.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CategoryListViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Здесь будет список категории блюд"
    }
    val text: LiveData<String> = _text
}