package com.cafeapp.mycafe.frameworks.view.orders.delivery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.viewpager2.widget.ViewPager2
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.frameworks.room.OrdersEntity
import com.cafeapp.mycafe.frameworks.view.BaseFragment
import com.cafeapp.mycafe.frameworks.view.delivery.SelectedOrder
import com.cafeapp.mycafe.frameworks.view.menu.categorylist.WorkMode
import com.cafeapp.mycafe.interface_adapters.viewmodels.categories.CategoryViewModel
import com.cafeapp.mycafe.interface_adapters.viewmodels.categories.CategoryViewState
import com.cafeapp.mycafe.interface_adapters.viewmodels.orders.OrderViewModel
import com.cafeapp.mycafe.interface_adapters.viewmodels.orders.OrderViewState
import com.cafeapp.mycafe.use_case.utils.MsgState
import com.cafeapp.mycafe.use_case.utils.SharedMsg
import com.cafeapp.mycafe.use_case.utils.SharedViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.scope.currentScope

class DeliveryMainFragment() : BaseFragment<OrderViewModel, OrderViewState>() {
    override val viewModel: OrderViewModel by currentScope.inject()
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.fragment_delivery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFabImageResource(android.R.drawable.ic_menu_save)
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        viewPager = view.findViewById(R.id.pager)

        val deliveryAddAdapter = DeliveryTapAdapter(this, viewModel)
        viewPager.adapter = deliveryAddAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            if (position == 0) tab.text = getString(R.string.client_title)
            if (position == 1) tab.text = getString(R.string.order_title)
        }.attach()
    }

    override fun onViewModelMsg(state: OrderViewState) {
        super.onViewModelMsg(state)
       if (state.saveOk && state.ordersEntityID > 0)
                SelectedOrder.currentOrder.id = state.ordersEntityID
        state.orderDishEntityModifyList?.let {viewPager.currentItem = 1}
    }

    private fun insertDishesAndLoadDelivery(value: Any) {
        val selectedDishMap = value as Map<OrdersEntity, MutableList<Long>>
        val iterator: Iterator<OrdersEntity> = selectedDishMap.keys.iterator()
        val order = iterator.next()
        val dishIdList = selectedDishMap.get(order)

        dishIdList?.let {
            insertDishList(order, dishIdList)
        }
    }

    private fun insertDishList(order: OrdersEntity, selectedDishList: MutableList<Long>) =
        viewModel.insertSelDishIdList(order, selectedDishList)

    override fun onMainFabClick() {
        sharedModel?.select(SharedMsg(MsgState.RETURNSELECTEDDISHLIST, 0))
    }

    override fun onSharedMsg(msg: SharedMsg) {
        if (msg.stateName == MsgState.DELEVERYOPEN ) {
          if (msg.value is Map<*, *>)
            insertDishesAndLoadDelivery(msg.value)
        }
    }
}