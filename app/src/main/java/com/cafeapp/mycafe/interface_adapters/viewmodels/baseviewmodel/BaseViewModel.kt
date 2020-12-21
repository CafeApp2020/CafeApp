package com.cafeapp.mycafe.interface_adapters.viewmodels.baseviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel: ViewModel() {
    protected val compositeDisposable: CompositeDisposable = CompositeDisposable()
    protected val modifyViewState = MutableLiveData<BaseViewState>()

    val viewState: LiveData<BaseViewState> = modifyViewState

    override fun onCleared() {
        compositeDisposable.clear()
    }
}
