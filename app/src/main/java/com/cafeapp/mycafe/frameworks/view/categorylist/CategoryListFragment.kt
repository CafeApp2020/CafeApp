package com.cafeapp.mycafe.frameworks.view.categorylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.interface_adapters.viewmodels.categories.CategoryListViewModel
import com.cafeapp.mycafe.use_case.utils.MsgState
import com.cafeapp.mycafe.use_case.utils.SharedMsg
import com.cafeapp.mycafe.use_case.utils.SharedViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.less.repository.db.room.CategoryEntity
import kotlinx.android.synthetic.main.fragment_categorylist.view.*
import org.koin.androidx.scope.currentScope

// Экран для отображения категорий блюд
class CategoryListFragment : Fragment() {
    private lateinit var categoryListAdapter: CategoryListRVAdapter
    private val categoryListViewModel: CategoryListViewModel by currentScope.inject()

    private val sharedModel by lazy {
        activity?.let { ViewModelProvider(it).get(SharedViewModel::class.java) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_categorylist, container, false)

        categoryListAdapter = CategoryListRVAdapter { category, button ->
            onButtonClickListener(category, button)
        }

        categoryListViewModel.categoryViewState.observe(viewLifecycleOwner, { state ->
            state.categories?.let {
                categoryListAdapter.data = it
            }
        })

        with(root) {
            categoryListRV.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = categoryListAdapter
            }

            val fab = activity?.findViewById<FloatingActionButton>(R.id.activityFab)

            fab?.setImageResource(android.R.drawable.ic_input_add)
            fab?.setOnClickListener {
                sharedModel?.select(SharedMsg(MsgState.ADDCATEGORY, -1L))
            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryListViewModel.getCategories()
    }

    private fun onButtonClickListener(category: CategoryEntity, button: Int) {
        when (button) {
            R.id.categoryViewHolderLeftSide -> onCategoryClick(category.id)
            R.id.categoryViewHolderEditButton -> onEditCategoryButtonClick(category.id)
        }
    }

    private fun onCategoryClick(categoryId: Long) {
        sharedModel?.select(
            SharedMsg(
                MsgState.DISHESLIST,
                categoryId
            )
        )
    }

    private fun onEditCategoryButtonClick(categoryId: Long) {
        sharedModel?.select(
            SharedMsg(
                MsgState.ADDCATEGORY,
                categoryId
            )
        )
    }
}