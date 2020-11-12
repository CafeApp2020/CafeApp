package com.cafeapp.mycafe.frameworks.view.categorylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.interface_adapters.viewmodels.CategoryListViewModel
import com.cafeapp.mycafe.use_case.utils.MsgState
import com.cafeapp.mycafe.use_case.utils.SharedMsg
import com.cafeapp.mycafe.use_case.utils.SharedViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

// // Экран для отображения категорий блюд
class CategoryListFragment : Fragment() {
    private lateinit var categoryListViewModel: CategoryListViewModel
    private val sharedModel by lazy {
        activity?.let { ViewModelProvider(it).get(SharedViewModel::class.java) }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        categoryListViewModel =
                ViewModelProvider(this).get(CategoryListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_categorylist, container, false)
        val textView: TextView = root.findViewById(R.id.text_dishmenu)
        categoryListViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        val addCategoryFab : FloatingActionButton = root.findViewById(R.id.add_category_fab)
        addCategoryFab.setOnClickListener {
          sharedModel?.select(SharedMsg(MsgState.ADDCATEGORY, -1))
        }

        return root
    }
}