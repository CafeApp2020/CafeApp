package com.cafeapp.mycafe.frameworks.view.orders.orderList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.frameworks.room.OrdersEntity
import com.cafeapp.mycafe.frameworks.view.BaseFragment
import com.cafeapp.mycafe.frameworks.view.delivery.OrderType
import com.cafeapp.mycafe.frameworks.view.menu.categorylist.WorkMode
import com.cafeapp.mycafe.frameworks.view.orders.OrderTypeFragment
import com.cafeapp.mycafe.interface_adapters.viewmodels.categories.CategoryViewModel
import com.cafeapp.mycafe.interface_adapters.viewmodels.categories.CategoryViewState
import com.cafeapp.mycafe.interface_adapters.viewmodels.orders.OrderViewModel
import com.cafeapp.mycafe.interface_adapters.viewmodels.orders.OrderViewState
import com.cafeapp.mycafe.use_case.utils.MsgState
import com.cafeapp.mycafe.use_case.utils.SharedMsg
import com.cafeapp.mycafe.use_case.utils.SharedViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.category_view_holder.*
import kotlinx.android.synthetic.main.fragment_orderlist.view.*
import org.koin.androidx.scope.currentScope

// Экран для отображения списка заказов
class OrderListFragment : BaseFragment<OrderViewModel, OrderViewState>() {
    private lateinit var orderListAdapter: OrderListRVAdapter
    override val viewModel: OrderViewModel by currentScope.inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val root = inflater.inflate(R.layout.fragment_orderlist, container, false)
        initRecyclerView(root)
        setFabImageResource(android.R.drawable.ic_input_add)
        viewModel.getOrderList()
        return root
    }

    private fun openOrder(ordersEntity: OrdersEntity) {
        when (ordersEntity.ordertype) {
            OrderType.DELIVERY -> {
                sharedModel?.select(SharedMsg(MsgState.DELEVERYOPEN, ordersEntity))
            }

            OrderType.TAKEAWAY -> {
                sharedModel?.select(SharedMsg(MsgState.TAKEAWAYOPEN, ordersEntity))
            }

            OrderType.INROOM -> {
            }
        }
    }

    private fun initRecyclerView(root: View) {
        orderListAdapter = OrderListRVAdapter {
            openOrder(it)
        }
        root.orderlist_recyclerview.apply {
            adapter = orderListAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
    }

    override fun onMainFabClick() {
        val selectOrderTypeFragment = OrderTypeFragment()
        activity?.let { frActivity ->
            selectOrderTypeFragment.show(frActivity.supportFragmentManager,
                selectOrderTypeFragment.tag)
        }
    }

    override fun onViewModelMsg(state: OrderViewState) {
        super.onViewModelMsg(state)
        if (state.loadOk)
            state.ordersEntity?.let { orderEntity ->
                openOrder(orderEntity)
            }

        state.orderList?.let { dishList ->
            orderListAdapter.data = dishList
        }
    }

    override fun onSharedMsg(msg: SharedMsg) {}
}