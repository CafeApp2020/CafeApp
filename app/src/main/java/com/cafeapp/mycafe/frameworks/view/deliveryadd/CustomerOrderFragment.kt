package com.cafeapp.mycafe.frameworks.view.deliveryadd

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.use_case.utils.MsgState
import com.cafeapp.mycafe.use_case.utils.SharedMsg
import com.cafeapp.mycafe.use_case.utils.SharedViewModel
import kotlinx.android.synthetic.main.fragment_customer_order.*

class CustomerOrderFragment : Fragment() {
    private var currentOrderID:Long=0L

    private val sharedModel by lazy {
        activity?.let { ViewModelProvider(it).get(SharedViewModel::class.java) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(R.layout.fragment_customer_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
           Toast.makeText(activity, "CustomerOrderFragment", Toast.LENGTH_LONG).show()
           this.putFloat(ARG_OBJECT, 0.4f)
        }

        addDihesBtn.setOnClickListener {
            sharedModel?.select(SharedMsg(MsgState.SELECTDISHTOORDER, currentOrderID))
        }
    }
}