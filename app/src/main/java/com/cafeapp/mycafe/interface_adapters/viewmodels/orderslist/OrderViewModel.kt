package com.cafeapp.mycafe.interface_adapters.viewmodels.orderslist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cafeapp.mycafe.use_case.interactors.orderlist.IOrderInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class OrderViewModel(private val interactor: IOrderInteractor) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val mutableOrderListViewState = MutableLiveData<OrdersViewState>()

    val orderListViewStateToObserve = mutableOrderListViewState

    override fun onCleared() {
        compositeDisposable.clear()
    }


    fun getOrderList() {
        compositeDisposable.add(
            interactor.getAllOrders()!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mutableOrderListViewState.value = OrdersViewState(it)
                }, {
                    mutableOrderListViewState.value = OrdersViewState(error = it)
                })
        )
    }


}