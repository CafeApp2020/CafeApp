package com.cafeapp.mycafe.frameworks.view.orderlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.interface_adapters.viewmodels.OrderListViewModel

// Экран для отображения списка заказов
class OrderListFragment : Fragment() {
    private lateinit var orderListViewModel: OrderListViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        orderListViewModel =
                ViewModelProvider(this).get(OrderListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_orderlist, container, false)
        val textView: TextView = root.findViewById(R.id.text_orders)
        orderListViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}