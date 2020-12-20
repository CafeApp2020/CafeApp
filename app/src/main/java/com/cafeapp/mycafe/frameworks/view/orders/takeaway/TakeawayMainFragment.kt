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
import com.cafeapp.mycafe.frameworks.view.delivery.SelectedOrder
import com.cafeapp.mycafe.interface_adapters.viewmodels.orders.OrderViewModel
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

class TakeawayMainFragment : Fragment() {
    private val orderViewModel: OrderViewModel by currentScope.inject()
    private lateinit var viewPager: ViewPager2

    private val sharedViewModel by lazy {
        activity?.let {
            ViewModelProvider(it).get(SharedViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_takeaway, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFab()
        initSharedViewModelObserver()
        initOrderViewModelObserver()
        initTabLayout()
    }

    private fun initTabLayout() {
        val tabLayout = view?.findViewById<TabLayout>(R.id.fragment_takeaway_tab_layout)
        viewPager = view?.findViewById(R.id.fragment_takeaway_pager)!!

        val takeawayAdapter = TakeawayTapAdapter(this, orderViewModel)
        viewPager.adapter = takeawayAdapter

        TabLayoutMediator(tabLayout!!, viewPager) { tab, position ->
            if (position == 0) tab.text = getString(R.string.client_title)
            if (position == 1) tab.text = getString(R.string.order_title)
        }.attach()
    }

    private fun initFab() {
        val fab = activity?.findViewById<FloatingActionButton>(R.id.activityFab)

        with(fab!!) {
            setImageResource(android.R.drawable.ic_menu_save)
            setOnClickListener {
                sharedViewModel?.select(SharedMsg(MsgState.RETURNSELECTEDDISHLIST, 0))
            }
        }
    }

    private fun initSharedViewModelObserver() {
        sharedViewModel?.getSelected()?.observe(viewLifecycleOwner) {
            when (it.stateName) {
                MsgState.TAKEAWAYOPEN -> {
                    if (it.value is Map<*, *>)
                        insertDishesAndLoadOrder(it.value)
                }
            }
        }
    }

    private fun initOrderViewModelObserver() {
        orderViewModel.orderViewState.observe(viewLifecycleOwner) {
            when {
                it.saveOk -> {
                    if (it.ordersEntityID > 0) {
                        SelectedOrder.currentOrder.id = it.ordersEntityID
                    }
                }
            }

            it.orderDishEntityModifyList?.let {
                viewPager.currentItem = 1
            }
        }
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
        orderViewModel.insertSelDishIdList(order, selectedDishList)
    }
}