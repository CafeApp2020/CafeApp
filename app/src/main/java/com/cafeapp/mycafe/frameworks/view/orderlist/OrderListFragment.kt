package com.cafeapp.mycafe.frameworks.view.orderlist

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.interface_adapters.viewmodels.orderslist.OrderViewModel
import com.cafeapp.mycafe.use_case.utils.MsgState
import com.cafeapp.mycafe.use_case.utils.SharedViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.category_view_holder.*
import kotlinx.android.synthetic.main.fragment_orderlist.view.*
import org.koin.androidx.scope.currentScope
import java.lang.Exception


// Экран для отображения списка заказов
class OrderListFragment : Fragment() {
    private val orderViewModel: OrderViewModel by currentScope.inject()
    private lateinit var orderListAdapter: OrderListRVAdapter

    private val sharedModel by lazy {
        activity?.let { ViewModelProvider(it).get(SharedViewModel::class.java) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_orderlist, container, false)

        initRecyclerView(root)
        initSharedModelObserver()

        val fab=activity?.findViewById<FloatingActionButton>(R.id.activityFab)
        fab?.setImageResource(android.R.drawable.ic_input_add)
        fab?.setOnClickListener {
            val selectOrderTypeFragment = OrderTypeFragment()
            activity?.let { it1 -> selectOrderTypeFragment.show(it1.supportFragmentManager, selectOrderTypeFragment.tag) }
        }

            orderViewModel?.orderListViewStateToObserve.observe(viewLifecycleOwner) { state ->
                state.error?.let { error ->
                    Toast.makeText(activity, error?.message, Toast.LENGTH_LONG).show()
                    return@observe
                }

                state.orderList?.let { dishList ->
                    orderListAdapter.data = dishList
                }
            }

        orderViewModel.getOrderList()
        return root
    }

    private fun initSharedModelObserver() {
        sharedModel?.getSelected()?.observe(viewLifecycleOwner) { msg ->
            when (msg.stateName) {
                MsgState.RETURNSELECTEDDISHLIST -> {
                    openOrder(msg.value)
                }
            }
        }
    }

    private fun openOrder(value: Any) {
        try {
            var selecetdDishMap = value as Map<Long, MutableList<Long>>
            val iterator: Iterator<Long> = selecetdDishMap.keys.iterator()
            val orderId = iterator.next()
        }
        catch (e:Exception) {
            Log.d(TAG, "openOrder: "+ e.message)
        }
    }

    private fun initRecyclerView(root: View) {
        orderListAdapter = OrderListRVAdapter { id ->
        }

        root.orderlist_recyclerview.apply {
            adapter = orderListAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
    }
}