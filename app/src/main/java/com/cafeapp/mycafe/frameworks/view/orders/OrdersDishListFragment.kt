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
import com.cafeapp.mycafe.frameworks.view.BaseFragment
import com.cafeapp.mycafe.frameworks.view.delivery.SelectedOrder
import com.cafeapp.mycafe.interface_adapters.viewmodels.orders.OrderViewModel
import com.cafeapp.mycafe.interface_adapters.viewmodels.orders.OrderViewState
import com.cafeapp.mycafe.use_case.utils.MsgState
import com.cafeapp.mycafe.use_case.utils.SharedMsg
import com.cafeapp.mycafe.use_case.utils.SharedViewModel
import kotlinx.android.synthetic.main.fragment_order_dishlist.*
import kotlinx.android.synthetic.main.fragment_order_dishlist.view.*
import org.koin.androidx.scope.currentScope

class OrdersDishListFragment(orderViewModel: OrderViewModel): BaseFragment<OrderViewModel, OrderViewState>() , OnOrderDishListener {
    private lateinit var ordersDishListRVAdapter: OrdersDishListRVAdapter
    override val viewModel = orderViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val root = inflater.inflate(R.layout.fragment_order_dishlist, container, false)
        initRecyclerView(root)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addDihesBtn.setOnClickListener {
            sharedModel?.select(SharedMsg(MsgState.SELECTDISHTOORDER, SelectedOrder.currentOrder))
        }
        payedButton?.setOnClickListener {
            SelectedOrder.currentOrder.paided=true
            viewModel.changePayedStatus(SelectedOrder.currentOrder)
        }
    }

    private fun loadDishList(ordersEntity: OrdersEntity) {
        viewModel.loadDishList(ordersEntity)
    }

    private fun initRecyclerView(view: View) {
        ordersDishListRVAdapter = OrdersDishListRVAdapter(OrdersDishListFragment@this)

        view.dishListRW.apply {
            adapter = ordersDishListRVAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDishCountChangeButtonClick(orderDishEntity: OrderDishEntityModify) {
        viewModel.updateOrderDishEntityModify(orderDishEntity)
    }

    override fun onViewModelMsg(state: OrderViewState) {
        super.onViewModelMsg(state)
        state.orderDishEntityModifyList?.let {
            it.let {
                ordersDishListRVAdapter.setDishList(it)
                totalSummTW.text = viewModel.getTotalSum(it).toString() + " ₽"
            }
        }
        if (state.saveOk&&SelectedOrder.currentOrder.paided)
            sharedModel?.select(SharedMsg(MsgState.RETURNSELECTEDDISHLIST, 0))
    }

    override fun onMainFabClick() {
    }

    override fun onSharedMsg(msg: SharedMsg) {
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