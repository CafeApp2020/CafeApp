package com.cafeapp.mycafe.frameworks.view.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.use_case.utils.MsgState
import com.cafeapp.mycafe.use_case.utils.SharedMsg
import com.cafeapp.mycafe.use_case.utils.SharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_ordertype_select.*

class OrderTypeFragment: BottomSheetDialogFragment() {
    private val sharedModel by lazy {
        activity?.let { ViewModelProvider(it).get(SharedViewModel::class.java) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ordertype_select, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        select_navigation_view.setNavigationItemSelectedListener { menuItem ->
            when (menuItem!!.itemId) {
                R.id.nav_inroom ->  Toast.makeText(activity,"nav_inroom",Toast.LENGTH_LONG).show()
                R.id.nav_takeaway -> Toast.makeText(activity,"nav_takeaway",Toast.LENGTH_LONG).show()
                R.id.nav_delevery -> sharedModel?.select(SharedMsg(MsgState.DELIVERYADD , -1L))
            }
            dismiss()
            true
        }
    }
}