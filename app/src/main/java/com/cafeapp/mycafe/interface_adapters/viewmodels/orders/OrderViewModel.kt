package com.cafeapp.mycafe.interface_adapters.viewmodels.orders

import com.cafeapp.mycafe.entities.OrderDishEntityModify
import com.cafeapp.mycafe.frameworks.room.OrdersEntity
import com.cafeapp.mycafe.interface_adapters.viewmodels.baseviewmodel.BaseViewModel
import com.cafeapp.mycafe.use_case.interactors.orders.IOrderInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class OrderViewModel(private val orderInteractor: IOrderInteractor) : BaseViewModel() {

    fun saveOrder(orderDelivery: OrdersEntity) {
        if (orderDelivery.id == 0L)
            addNewOrder(orderDelivery)
        else
            editOrder(orderDelivery)
    }

    private fun addNewOrder(orderDelivery: OrdersEntity) {
        compositeDisposable.add(
            orderInteractor.saveOrder(orderDelivery)!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { orderId ->
                        modifyViewState.value =
                            OrderViewState(saveOk = true, ordersEntityID = orderId)
                    },
                    { error ->
                        modifyViewState.value = OrderViewState(error = error)
                    })
        )
    }

    private fun editOrder(orderDelivery: OrdersEntity) {
        compositeDisposable.add(
            orderInteractor.updateOrder(orderDelivery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        modifyViewState.value = OrderViewState(saveOk = true)
                    },
                    { error ->
                        modifyViewState.value = OrderViewState(error = error)
                    })
        )
    }

    fun loadDishList(order: OrdersEntity) {
        compositeDisposable.add(
            orderInteractor.loadDishListForOrder(order.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { dishList ->
                        dishList?.let {
                            modifyViewState.value = OrderViewState(
                                orderDishEntityModifyList = dishList)
                        }
                    },
                    { error ->
                        modifyViewState.value = OrderViewState(error = error)
                    })
        )
    }

    fun insertSelDishIdList(order: OrdersEntity, selectedDishList: MutableList<Long>) {
        compositeDisposable.add(
            orderInteractor.insertOrderListId(order.id, selectedDishList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { dishList ->
                        dishList?.let {
                            modifyViewState.value = OrderViewState(
                                orderDishEntityModifyList = dishList)
                        }
                    },
                    { error ->
                        modifyViewState.value = OrderViewState(error = error)
                    })
        )
    }

    fun getOrderList() {
        compositeDisposable.add(
            orderInteractor.getAllOrders()!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    modifyViewState.value = OrderViewState(orderList = it)
                }, {
                    modifyViewState.value = OrderViewState(error = it)
                })
        )
    }

    fun getTotalSum(dishList: List<OrderDishEntityModify>): Double {
        return orderInteractor.getTotalSum(dishList)
    }

    fun changePayedStatus(order: OrdersEntity) {
        editOrder(order)
    }

    fun updateOrderDishEntityModify(orderDishEntity: OrderDishEntityModify) {
        compositeDisposable.add(
            orderInteractor.updateOrderDishEntityModify(orderDishEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    modifyViewState.value = OrderViewState(saveOk = true)
                }, {
                    modifyViewState.value = OrderViewState(error = it)
                })
        )
    }
}