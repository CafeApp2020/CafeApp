package com.cafeapp.mycafe.interface_adapters.viewmodels.delivery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cafeapp.mycafe.frameworks.room.OrdersEntity
import com.cafeapp.mycafe.frameworks.view.deliveryadd.OrderType
import com.cafeapp.mycafe.use_case.interactors.orderlist.IOrderInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DeliveryViewModel(private val orderInteractor: IOrderInteractor) : ViewModel() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val modifyDeliveryAddViewState = MutableLiveData<DeliveryViewState>()

    val deliveryViewState: LiveData<DeliveryViewState> = modifyDeliveryAddViewState

    override fun onCleared() {
        compositeDisposable.clear()
    }

    fun saveDelivery(orderDelivery: OrdersEntity) {
        orderDelivery.ordertype= OrderType.DELIVERY
        if (orderDelivery.id==0L)
            addNewDelivery(orderDelivery)
        else
            editDelivery (orderDelivery)
    }

    private fun addNewDelivery(orderDelivery: OrdersEntity) {
        compositeDisposable.add(
            orderInteractor.saveOrder(orderDelivery)!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { orderId ->
                        modifyDeliveryAddViewState.value= DeliveryViewState(saveOk = true)
                    },
                    { error ->
                        modifyDeliveryAddViewState.value= DeliveryViewState(error=error)
                    })
        )
    }

    private fun editDelivery(orderDelivery: OrdersEntity) {
        compositeDisposable.add(
            orderInteractor.updateOrder(orderDelivery)!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        modifyDeliveryAddViewState.value= DeliveryViewState(saveOk = true)
                    },
                    { error ->
                        modifyDeliveryAddViewState.value= DeliveryViewState(error=error)
                    })
        )
    }

}