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
import com.cafeapp.mycafe.frameworks.room.OrdersEntity
import com.cafeapp.mycafe.frameworks.view.delivery.SelectedOrder
import com.cafeapp.mycafe.interface_adapters.viewmodels.orders.OrderViewModel
import com.cafeapp.mycafe.use_case.utils.MsgState
import com.cafeapp.mycafe.use_case.utils.SharedMsg
import com.cafeapp.mycafe.use_case.utils.SharedViewModel
import kotlinx.android.synthetic.main.fragment_order_dishlist.*
import kotlinx.android.synthetic.main.fragment_order_dishlist.view.*

class OrdersDishListFragment(val orderViewModel: OrderViewModel) : Fragment() {
    private lateinit var ordersDishListRVAdapter: OrdersDishListRVAdapter

    private val sharedModel by lazy {
        activity?.let { ViewModelProvider(it).get(SharedViewModel::class.java) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       val root = inflater.inflate(R.layout.fragment_order_dishlist, container, false)
        initRecyclerView(root)
        initSharedModelObserver()

        orderViewModel.orderViewState.observe(viewLifecycleOwner) { deliveryViewState ->
            deliveryViewState.orderDishEntityModifyList?.let {
              it?.let {ordersDishListRVAdapter.setDishList(it)
                  totalSummTW.text=orderViewModel.getTotalSumm(it).toString() + " ₽"
              }
            }
        }
       return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addDihesBtn.setOnClickListener {
            sharedModel?.select(SharedMsg(MsgState.SELECTDISHTOORDER, SelectedOrder.currentOrder))
        }
    }

    private fun initSharedModelObserver() {
        sharedModel?.getSelected()?.observe(viewLifecycleOwner) { msg ->
            when (msg.stateName) {
                MsgState.DELEVERYOPEN -> {
                    if (msg.value is OrdersEntity)
                        loadDishList(msg.value)
                }
            }
        }
    }

    fun loadDishList(ordersEntity: OrdersEntity) {
        orderViewModel.loadDishList(ordersEntity)
    }

    private fun initRecyclerView(view: View) {
        ordersDishListRVAdapter = OrdersDishListRVAdapter()
        view.dishListRW.apply {
            adapter = ordersDishListRVAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}