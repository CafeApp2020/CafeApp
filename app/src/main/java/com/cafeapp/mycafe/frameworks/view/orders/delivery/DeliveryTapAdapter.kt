package com.cafeapp.mycafe.frameworks.view.orders.delivery

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cafeapp.mycafe.frameworks.view.orders.OrdersDishListFragment
import com.cafeapp.mycafe.interface_adapters.viewmodels.orders.OrderViewModel

class DeliveryTapAdapter(fragment: Fragment, val orderViewModel: OrderViewModel) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment = if (position==1)
            OrdersDishListFragment(orderViewModel)
        else
            DeliveryClientDataFragment(orderViewModel)
        return fragment
    }
}