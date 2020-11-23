package com.cafeapp.mycafe.frameworks.view.utils

import android.os.Bundle
import android.system.Os.close
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
import kotlinx.android.synthetic.main.fragment_bottomsheet.*

class BottomNavigationDrawerFragment: BottomSheetDialogFragment() {
    private val sharedModel by lazy {
        activity?.let { ViewModelProvider(it).get(SharedViewModel::class.java) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bottomsheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        navigation_view.setNavigationItemSelectedListener { menuItem ->
            when (menuItem!!.itemId) {
                R.id.nav_tablelist -> Toast.makeText(activity,"nav_tablelist",Toast.LENGTH_LONG).show()
                R.id.nav_categorylist -> sharedModel?.select(SharedMsg(MsgState.CATEGORYLISTOPEN, -1L))
                R.id.nav_orderlist -> Toast.makeText(activity,"nav_orderlist",Toast.LENGTH_LONG).show()
                R.id.nav_disheslist -> sharedModel?.select(SharedMsg(MsgState.DISHESLIST, -1L))
            }
            dismiss()
            true
        }
    }
}