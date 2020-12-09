package com.cafeapp.mycafe.interface_adapters.viewmodels.orderslist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cafeapp.mycafe.interface_adapters.viewmodels.dishes.dishlist.DishListViewState
import com.cafeapp.mycafe.use_case.interactors.orderlist.IOrderInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class OrderListViewModel(private val interactor: IOrderInteractor) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val mutableOrderListViewState = MutableLiveData<OrdersListViewState>()

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
                    mutableOrderListViewState.value = OrdersListViewState(it)
                }, {
                    mutableOrderListViewState.value = OrdersListViewState(error = it)
                })
        )
    }
}