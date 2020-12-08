package com.cafeapp.mycafe.frameworks.view.deliveryadd

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cafeapp.mycafe.R

class CustomerOrderFragment : Fragment() {

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
    }
}