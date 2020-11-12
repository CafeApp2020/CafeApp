package com.cafeapp.mycafe.frameworks.view.dishlist

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
import com.cafeapp.mycafe.interface_adapters.viewmodels.DishesListViewModel
import com.cafeapp.mycafe.use_case.utils.MsgState
import com.cafeapp.mycafe.use_case.utils.SharedMsg
import com.cafeapp.mycafe.use_case.utils.SharedViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

// Экран для отображения списка блюд
class DishListFragment : Fragment() {

    private lateinit var dishesListViewModel: DishesListViewModel
    private val sharedModel by lazy {
        activity?.let { ViewModelProvider(it).get(SharedViewModel::class.java) }
    }


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dishesListViewModel =
                ViewModelProvider(this).get(DishesListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_disheslist, container, false)
         val textView: TextView = root.findViewById(R.id.text)
        dishesListViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        val addCategoryFab : FloatingActionButton = root.findViewById(R.id.add_dish_fab)
        addCategoryFab.setOnClickListener {
            sharedModel?.select(SharedMsg(MsgState.ADDDISH, -1))
        }

        return root
    }
}
