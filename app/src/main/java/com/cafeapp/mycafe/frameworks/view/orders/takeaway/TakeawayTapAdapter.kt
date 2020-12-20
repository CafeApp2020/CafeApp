package com.cafeapp.mycafe.frameworks.view.orders.takeaway

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cafeapp.mycafe.frameworks.view.orders.OrdersDishListFragment
import com.cafeapp.mycafe.interface_adapters.viewmodels.orders.OrderViewModel

class TakeawayTapAdapter(fragment: Fragment, private val orderViewModel: OrderViewModel) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 1) {
            OrdersDishListFragment(orderViewModel)
        } else {
            TakeawayClientDataFragment(orderViewModel)
        }
    }
}