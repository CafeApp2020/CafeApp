package com.cafeapp.mycafe.frameworks.view.dishesadd

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.interface_adapters.viewmodels.DishesAddViewModel
import com.cafeapp.mycafe.use_case.utils.MsgState
import com.cafeapp.mycafe.use_case.utils.SharedViewModel

// Экран для добавления/редактирования блюда
class DishesAddFragment : Fragment() {

    private lateinit var dishesAddViewModel: DishesAddViewModel
    private val sharedModel by lazy {
        activity?.let { ViewModelProvider(it).get(SharedViewModel::class.java) }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dishesAddViewModel =
                ViewModelProvider(this).get(DishesAddViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dishesadd, container, false)
        val textView: TextView = root.findViewById(R.id.text_table)
        dishesAddViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        sharedModel?.getSelected()?.observe(viewLifecycleOwner, { msg ->
            when (msg.stateName) {
                MsgState.ADDDISH ->
                    if (msg.value is Int)
                        Toast.makeText(activity, "Выбрано блюдо с id= " + msg.value + " для редактирования. Если id=-1 добавляем новое блюдо", Toast.LENGTH_LONG).show()
            }
        })
        return root
    }
}