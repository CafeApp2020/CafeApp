package com.cafeapp.mycafe.frameworks.view.deliveryadd

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.frameworks.room.OrdersEntity
import com.cafeapp.mycafe.interface_adapters.viewmodels.delivery.DeliveryAddViewModel
import com.cafeapp.mycafe.use_case.utils.SharedViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_customerdata.*
import org.koin.androidx.scope.currentScope
import java.text.SimpleDateFormat
import java.util.*


class DeliveryAddFragment : Fragment() {
    private var currentDeliveryId:Long = 0
    private val deliveryAddViewModel: DeliveryAddViewModel by currentScope.inject()

    private val sharedModel by lazy {
        activity?.let { ViewModelProvider(it).get(SharedViewModel::class.java) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_delivery, container, false)
        val fab=activity?.findViewById<FloatingActionButton>(R.id.activityFab)
        if (fab != null) {fab.setImageResource(android.R.drawable.ic_menu_save)}
        fab?.setOnClickListener {
            saveDelivery()
        }

        deliveryAddViewModel.deliveryAddViewState.observe(viewLifecycleOwner, {
            when {
                it.saveOk -> {
                    Toast.makeText(activity, getString(R.string.saveok_title), Toast.LENGTH_LONG)
                        .show()
                }
            }
        })

        return root
    }

    private fun saveDelivery() {
        var orderDelivery = OrdersEntity(
            customername = customerNameTIT.text.toString(),
            customerphone = customerPhoneTIT.text.toString(),
            customeraddress = customerAddressTIT.text.toString()
        )
        if (currentDeliveryId>0)
            orderDelivery.id=currentDeliveryId
        deliveryAddViewModel.saveDelivery(orderDelivery)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        val viewPager = view.findViewById<ViewPager2>(R.id.pager)

        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
           // Toast.makeText(activity, "DeliveryAddFragment", Toast.LENGTH_LONG).show()
            this.putFloat(ARG_OBJECT, 0.4f)
        }

        val deliveryAddAdapter = DeliveryAddAdapter(this)
        viewPager.adapter = deliveryAddAdapter

         TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            if (position==0)  tab.text = getString(R.string.client_title)
            if (position==1)  tab.text = getString(R.string.order_title)
          }.attach()
    }
}