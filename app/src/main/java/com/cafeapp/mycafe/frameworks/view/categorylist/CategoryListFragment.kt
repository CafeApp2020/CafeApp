package com.cafeapp.mycafe.frameworks.view.categorylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.interface_adapters.viewmodels.categories.CategoryListViewModel
import com.cafeapp.mycafe.use_case.utils.MsgState
import com.cafeapp.mycafe.use_case.utils.SharedMsg
import com.cafeapp.mycafe.use_case.utils.SharedViewModel
import kotlinx.android.synthetic.main.fragment_categorylist.*
import kotlinx.android.synthetic.main.fragment_categorylist.view.*
import org.koin.androidx.scope.currentScope

// Экран для отображения категорий блюд
class CategoryListFragment() : Fragment() {
    private val categoryListViewModel: CategoryListViewModel by currentScope.inject()
    private lateinit var adapter: CategoryListRVAdapter

    private val sharedModel by lazy {
        activity?.let { ViewModelProvider(it).get(SharedViewModel::class.java) }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_categorylist, container, false)
        adapter = CategoryListRVAdapter {
            id ->  sharedModel?.select(SharedMsg(MsgState.ADDCATEGORY, id)) // если id>1 открываем фрагмент CategoryAddFragment для редактирования
        }

        categoryListViewModel.categoryViewState.observe(viewLifecycleOwner, {
            if(it.categories != null){
                adapter.data = it.categories
            }
        })

        root.categoryListRV.layoutManager = GridLayoutManager(activity, 2)
        root.categoryListRV.adapter = adapter

        root.addCategoryFab.setOnClickListener {
            sharedModel?.select(SharedMsg(MsgState.ADDCATEGORY, -1L))
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryListViewModel.getCategories()
    }
}