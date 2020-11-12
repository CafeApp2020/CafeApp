package com.cafeapp.mycafe.frameworks.view.categoryadd

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
import com.cafeapp.mycafe.interface_adapters.viewmodels.CategoryAddViewModel
import com.cafeapp.mycafe.use_case.utils.MsgState
import com.cafeapp.mycafe.use_case.utils.SharedViewModel

// Экран для добавления/редактирования категорий блюд
class CategoryAddFragment : Fragment() {

    private lateinit var categoryAddViewModel: CategoryAddViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        categoryAddViewModel =
                ViewModelProvider(this).get(CategoryAddViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_addcategory, container, false)
        val textView: TextView = root.findViewById(R.id.text_dishmenu)
        categoryAddViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        val sharedModel = activity?.let { ViewModelProvider(it).get(SharedViewModel::class.java) }
        if (sharedModel != null) {
            sharedModel.getSelected().observe(viewLifecycleOwner, { msg ->
                when (msg.stateName) {
                    MsgState.ADDCATEGORY ->
                        if (msg.value is Int)
                            Toast.makeText(activity, "Выбрана категория " + msg.value + " для редактирования. Если id=-1 добавляем нужную категорию", Toast.LENGTH_LONG).show()
                }
            })
        }
        return root
    }
}
