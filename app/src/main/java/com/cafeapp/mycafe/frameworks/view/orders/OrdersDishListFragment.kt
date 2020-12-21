package com.cafeapp.mycafe.frameworks.view.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.entities.OrderDishEntityModify
import com.cafeapp.mycafe.frameworks.room.OrdersEntity
import com.cafeapp.mycafe.frameworks.view.delivery.SelectedOrder
import com.cafeapp.mycafe.interface_adapters.viewmodels.orders.OrderViewModel
import com.cafeapp.mycafe.use_case.utils.MsgState
import com.cafeapp.mycafe.use_case.utils.SharedMsg
import com.cafeapp.mycafe.use_case.utils.SharedViewModel
import kotlinx.android.synthetic.main.fragment_order_dishlist.*
import kotlinx.android.synthetic.main.fragment_order_dishlist.view.*

class OrdersDishListFragment(private val orderViewModel: OrderViewModel)
    : Fragment(), OnOrderDishListener {
    private lateinit var ordersDishListRVAdapter: OrdersDishListRVAdapter

    private val sharedModel by lazy {
        activity?.let { ViewModelProvider(it).get(SharedViewModel::class.java) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val root = inflater.inflate(R.layout.fragment_order_dishlist, container, false)
        initRecyclerView(root)
        initSharedModelObserver()

        orderViewModel.orderViewState.observe(viewLifecycleOwner) { orderViewState ->
            orderViewState.orderDishEntityModifyList?.let {
                it.let {
                    ordersDishListRVAdapter.setDishList(it)
                    totalSummTW.text = orderViewModel.getTotalSum(it).toString() + " ₽"
                }
            }
            if (orderViewState.saveOk&&SelectedOrder.currentOrder.paided)
                sharedModel?.select(SharedMsg(MsgState.RETURNSELECTEDDISHLIST, 0))
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addDihesBtn.setOnClickListener {
            sharedModel?.select(SharedMsg(MsgState.SELECTDISHTOORDER, SelectedOrder.currentOrder))
        }
        payedButton?.setOnClickListener {
            SelectedOrder.currentOrder.paided=true
            orderViewModel.changePayedStatus(SelectedOrder.currentOrder)
        }
    }

    private fun initSharedModelObserver() {
        sharedModel?.getSelected()?.observe(viewLifecycleOwner) { msg ->
            when (msg.stateName) {
                MsgState.DELEVERYOPEN -> {
                    if (msg.value is OrdersEntity)
                        loadDishList(msg.value)
                }

                MsgState.TAKEAWAYOPEN -> {
                    if (msg.value is OrdersEntity)
                        loadDishList(msg.value)
                }
            }
        }
    }

    private fun loadDishList(ordersEntity: OrdersEntity) {
        orderViewModel.loadDishList(ordersEntity)
    }

    private fun initRecyclerView(view: View) {
        ordersDishListRVAdapter = OrdersDishListRVAdapter(OrdersDishListFragment@this)

        view.dishListRW.apply {
            adapter = ordersDishListRVAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDishCountChangeButtonClick(orderDishEntity: OrderDishEntityModify) {
        orderViewModel.updateOrderDishEntityModify(orderDishEntity)
    }


}