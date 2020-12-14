package com.cafeapp.mycafe.interface_adapters.viewmodels.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cafeapp.mycafe.entities.OrderDishEntityModify
import com.cafeapp.mycafe.frameworks.room.OrdersEntity
import com.cafeapp.mycafe.frameworks.view.delivery.OrderType
import com.cafeapp.mycafe.use_case.interactors.orders.IOrderInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class OrderViewModel(private val orderInteractor: IOrderInteractor) : ViewModel() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val modifyDeliveryAddViewState = MutableLiveData<OrderViewState>()

    val orderViewState: LiveData<OrderViewState> = modifyDeliveryAddViewState

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
                        modifyDeliveryAddViewState.value= OrderViewState(saveOk = true, ordersEntityID =orderId)
                    },
                    { error ->
                        modifyDeliveryAddViewState.value= OrderViewState(error =error)
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
                        modifyDeliveryAddViewState.value= OrderViewState(saveOk = true)
                    },
                    { error ->
                        modifyDeliveryAddViewState.value= OrderViewState(error =error)
                    })
        )
    }

    fun loadDishList(order: OrdersEntity) {
        compositeDisposable.add(
            orderInteractor.loadDishListForOrder(order.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {dishList ->
                        dishList?.let {modifyDeliveryAddViewState.value= OrderViewState(
                            orderDishEntityModifyList = dishList) }
                    },
                    { error ->
                        modifyDeliveryAddViewState.value= OrderViewState(error =error)
                    })
        )
    }

    fun insertSelDishIdList(order: OrdersEntity, selectedDishList: MutableList<Long>) {
        compositeDisposable.add(
            orderInteractor.insertOrderListId(order.id, selectedDishList)!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {dishList ->
                        dishList?.let {modifyDeliveryAddViewState.value= OrderViewState(
                            orderDishEntityModifyList = dishList) }
                    },
                    { error ->
                        modifyDeliveryAddViewState.value= OrderViewState(error =error)
                    })
        )
    }

    fun loadOrder(orderId:Long) {
        compositeDisposable.add(
            orderInteractor.loadOrder(orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    modifyDeliveryAddViewState.value = OrderViewState(loadOk = true, ordersEntity=it)
                }, {
                    modifyDeliveryAddViewState.value = OrderViewState(error = it)
                })
        )
    }


    fun getOrderList() {
        compositeDisposable.add(
            orderInteractor.getAllOrders()!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    modifyDeliveryAddViewState.value = OrderViewState(orderList = it)
                }, {
                    modifyDeliveryAddViewState.value = OrderViewState(error = it)
                })
        )
    }

    fun getTotalSumm(dishList: List<OrderDishEntityModify>): Double {
       return orderInteractor.getTotalSumm(dishList)
    }
}