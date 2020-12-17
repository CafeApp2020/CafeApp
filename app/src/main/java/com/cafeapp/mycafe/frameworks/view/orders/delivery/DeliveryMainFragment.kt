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
import com.cafeapp.mycafe.frameworks.view.delivery.SelectedOrder
import com.cafeapp.mycafe.interface_adapters.viewmodels.orders.OrderViewModel
import com.cafeapp.mycafe.use_case.utils.MsgState
import com.cafeapp.mycafe.use_case.utils.SharedMsg
import com.cafeapp.mycafe.use_case.utils.SharedViewModel
import com.cafeapp.mycafe.use_case.utils.isError
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_delivery_client_data.*
import org.koin.androidx.scope.currentScope

class DeliveryMainFragment : Fragment() {
    private val orderViewModel: OrderViewModel by currentScope.inject()
    private lateinit var viewPager: ViewPager2

    private val sharedModel by lazy {
        activity?.let { ViewModelProvider(it).get(SharedViewModel::class.java) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.fragment_delivery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFub()
        initSharedModelObserver()
        initOrderViewModelObserver()

        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        viewPager = view.findViewById(R.id.pager)

        val deliveryAddAdapter = DeliveryTapAdapter(this, orderViewModel)
        viewPager.adapter = deliveryAddAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            if (position == 0) tab.text = getString(R.string.client_title)
            if (position == 1) tab.text = getString(R.string.order_title)
        }.attach()
    }

    private fun initFub() {
        val fab = activity?.findViewById<FloatingActionButton>(R.id.activityFab)

        with(fab!!) {
            setImageResource(android.R.drawable.ic_menu_save)
            setOnClickListener {
                if (!isError(customerNameTIT, getString(R.string.enter_customer_name)) &&
                    !isError(customerPhoneTIT, getString(R.string.enter_phone_number)) &&
                    !isError(customerAddressTIT, getString(R.string.enter_address))
                ) {
                    sharedModel?.select(SharedMsg(MsgState.RETURNSELECTEDDISHLIST, 0))
                }
            }
        }
    }

    private fun initSharedModelObserver() {
        sharedModel?.getSelected()?.observe(viewLifecycleOwner) { msg ->
            when (msg.stateName) {
                MsgState.DELEVERYOPEN -> {
                    if (msg.value is Map<*, *>)
                        insestDihesAndloadDelivery(msg.value)
                }
            }
        }
    }

    private fun initOrderViewModelObserver() {
        orderViewModel.orderViewState.observe(viewLifecycleOwner) { deliveryViewState ->
            when {
                deliveryViewState.saveOk -> {
                    if (deliveryViewState.ordersEntityID > 0) {
                        SelectedOrder.currentOrder.id = deliveryViewState.ordersEntityID
                    }
                }
            }
            deliveryViewState.orderDishEntityModifyList?.let {
                viewPager.currentItem = 1
            }
        }
    }

    private fun insestDihesAndloadDelivery(value: Any) {
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