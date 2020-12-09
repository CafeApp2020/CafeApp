package com.cafeapp.mycafe.frameworks.view.categorylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.interface_adapters.viewmodels.categories.CategoryAddViewModel
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
    private val categoryViewModel: CategoryAddViewModel by currentScope.inject()

    private val sharedModel by lazy {
        activity?.let { ViewModelProvider(it).get(SharedViewModel::class.java) }
    }

    private val listener: OnCategoryListItemClickListener =
        object : OnCategoryListItemClickListener {
            override fun onCategoryClick(categoryId: Long) {
                onCategoryClickBehavior(categoryId)
            }

            override fun onEditCategoryButtonClick(categoryId: Long) {
                onEditCategoryButtonClickBehavior(categoryId)
            }

            override fun onRemoveCategoryButtonClick(category: CategoryEntity) {
                onRemoveCategoryButtonClickBehavior(category)
            }

            override fun onChangeStopListStateForCategory(
                category: CategoryEntity,
                isInStopList: Boolean,
            ) {
                onChangeStopListStateForCategoryBehavior(category, isInStopList)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_categorylist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        initViewModelObserver()

        categoryListViewModel.getCategories()  // break point
    }

    private fun initViewModelObserver() {
        categoryListViewModel.categoryViewState.observe(viewLifecycleOwner, { state ->
            state.categories?.let {
                categoryListAdapter.setCategoryList(it) //здесь происходит баг, break point , при нажатии кнопки назад с других экранов, выполняется почему-то два раза
            }
        })
    }

    private fun initViews(view: View) {
        initRecyclerView(view)
        initFabButton()
    }

    private fun initRecyclerView(view: View) {
        categoryListAdapter = CategoryListRVAdapter(listener)

        view.categoryListRV.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = categoryListAdapter
        }
    }

    private fun initFabButton() {
        val fab =
            activity?.findViewById<FloatingActionButton>(R.id.activityFab)

        fab?.setImageResource(android.R.drawable.ic_input_add)
        fab?.setOnClickListener {
            sharedModel?.select(SharedMsg(MsgState.ADDCATEGORY,
                -1L))
        }
    }

    private fun onChangeStopListStateForCategoryBehavior(
        category: CategoryEntity,
        isInStopList: Boolean,
    ) {
        category.isInStopList = isInStopList
        categoryViewModel.saveCategory(category)
    }

    private fun onCategoryClickBehavior(categoryId: Long) {
        sharedModel?.select(
            SharedMsg(
                MsgState.DISHESLIST,
                categoryId
            )
        )
    }

    private fun onEditCategoryButtonClickBehavior(categoryId: Long) {
        sharedModel?.select(
            SharedMsg(
                MsgState.ADDCATEGORY,
                categoryId
            )
        )
    }

    private fun onRemoveCategoryButtonClickBehavior(category: CategoryEntity) {
        category.isDeleted = true
        categoryViewModel.saveCategory(category)
    }
}