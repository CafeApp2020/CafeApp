package com.cafeapp.mycafe.frameworks.view.orders.takeaway

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.frameworks.room.OrdersEntity
import com.cafeapp.mycafe.frameworks.view.BaseFragment
import com.cafeapp.mycafe.frameworks.view.delivery.SelectedOrder
import com.cafeapp.mycafe.interface_adapters.viewmodels.categories.CategoryViewState
import com.cafeapp.mycafe.interface_adapters.viewmodels.orders.OrderViewModel
import com.cafeapp.mycafe.interface_adapters.viewmodels.orders.OrderViewState
import com.cafeapp.mycafe.use_case.utils.MsgState
import com.cafeapp.mycafe.use_case.utils.SharedMsg
import com.cafeapp.mycafe.use_case.utils.SharedViewModel
import com.cafeapp.mycafe.use_case.utils.isError
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_takeaway_client_data.*
import kotlinx.android.synthetic.main.fragment_takeaway_client_data.view.*
import org.koin.androidx.scope.currentScope

class TakeawayMainFragment : BaseFragment<OrderViewModel, OrderViewState>() {
    override val viewModel: OrderViewModel by currentScope.inject()
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_takeaway, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFabImageResource(android.R.drawable.ic_menu_save)
        initTabLayout()
    }

    private fun initTabLayout() {
        val tabLayout = view?.findViewById<TabLayout>(R.id.fragment_takeaway_tab_layout)
        viewPager = view?.findViewById(R.id.fragment_takeaway_pager)!!

        val takeawayAdapter = TakeawayTapAdapter(this, viewModel)
        viewPager.adapter = takeawayAdapter

        TabLayoutMediator(tabLayout!!, viewPager) { tab, position ->
            if (position == 0) tab.text = getString(R.string.client_title)
            if (position == 1) tab.text = getString(R.string.order_title)
        }.attach()
    }

    override fun onViewModelMsg(state: OrderViewState) {
        super.onViewModelMsg(state)
        if (state.saveOk &&  state.ordersEntityID > 0)
            SelectedOrder.currentOrder.id = state.ordersEntityID
        state.orderDishEntityModifyList?.let { viewPager.currentItem = 1}
    }

    private fun insertDishesAndLoadOrder(value: Any) {
        val selectedDishMap = value as Map<OrdersEntity, MutableList<Long>>
        val iterator: Iterator<OrdersEntity> = selectedDishMap.keys.iterator()
        val order = iterator.next()
        val dishIdList = selectedDishMap.get(order)
        dishIdList?.let {
            insertDishList(order, dishIdList)
        }
    }

    private fun insertDishList(order: OrdersEntity, selectedDishList: MutableList<Long>) {
        viewModel.insertSelDishIdList(order, selectedDishList)
    }

    override fun onMainFabClick() {
        sharedModel?.select(SharedMsg(MsgState.RETURNSELECTEDDISHLIST, 0))
    }

    override fun onSharedMsg(msg: SharedMsg) {
        if (msg.stateName == MsgState.TAKEAWAYOPEN && msg.value is Map<*, *>)
              insertDishesAndLoadOrder(msg.value)
    }
}