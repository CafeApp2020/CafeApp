package com.cafeapp.mycafe.frameworks.view.categorylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.interface_adapters.viewmodels.categories.CategoryViewModel
import com.cafeapp.mycafe.use_case.utils.MsgState
import com.cafeapp.mycafe.use_case.utils.SharedMsg
import com.cafeapp.mycafe.use_case.utils.SharedViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.less.repository.db.room.CategoryEntity
import kotlinx.android.synthetic.main.fragment_categorylist.view.*
import org.koin.androidx.scope.currentScope

enum class WorkMode() {
    MenuCreate,
    OrderSelect
}

// Экран для отображения категорий блюд
class CategoryListFragment : Fragment() {
    private var workMode:WorkMode=WorkMode.MenuCreate // режим работы: создание/редактирование меню либо выбор блюд для заказа
    private lateinit var categoryListAdapter: CategoryListRVAdapter
    private val categoryListViewModel: CategoryViewModel by currentScope.inject()

    private val sharedModel by lazy {
        activity?.let { ViewModelProvider(it).get(SharedViewModel::class.java) }
    }

    companion object {
       var currentOrderID:Long =0 //текущий заказ
       var selectedDishListForOrder= mutableListOf<Long>() //ID выбранных в текущем заказе блюд
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
       initSharedModelObserver()
       categoryListViewModel.getCategories()  // break point
    }

    private fun initViewModelObserver() {
        categoryListViewModel.categoryViewState.observe(viewLifecycleOwner) { state ->
            state.error?.let {
                Toast.makeText(context,state.error.message, Toast.LENGTH_LONG).show()
                return@observe
            }
            state.categoryList?.let { categoryList ->
                categoryListAdapter.setCategoryList(categoryList) //здесь происходит баг, break point , при нажатии кнопки назад с других экранов, выполняется почему-то два раза
            }
        }
    }

    private fun initViews(view: View) {
        initRecyclerView(view)
        initFabButton()
    }

    private fun initSharedModelObserver() {
        sharedModel?.getSelected()?.observe(viewLifecycleOwner) { msg ->
            when (msg.stateName) {
                MsgState.SELECTDISHTOORDER -> {
                    if (msg.value is Long) {   // msg.value ID текущего заказа
                     //   Toast.makeText(context, "id="+msg.value, Toast.LENGTH_LONG).show()
                        currentOrderID = msg.value
                        selectedDishListForOrder.clear()
                        workMode = WorkMode.OrderSelect
                        initSelectOrderMode()
                    }
                }
            }
        }
    }

    private fun initSelectOrderMode() {
        val fab = activity?.findViewById<FloatingActionButton>(R.id.activityFab)
        fab?.setImageResource(R.drawable.ic_list_add_check_24)
        fab?.setOnClickListener {
            sharedModel?.select(SharedMsg(MsgState.RETURNSELECTEDDISHLIST, mapOf(currentOrderID to selectedDishListForOrder)))
        }
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

        if (workMode==WorkMode.OrderSelect)
            initSelectOrderMode()
        else {
            fab?.setImageResource(android.R.drawable.ic_input_add)
            fab?.setOnClickListener {
                sharedModel?.select(
                    SharedMsg(
                        MsgState.ADDCATEGORY,
                        -1L
                    )
                )
            }
        }
    }

    private fun onChangeStopListStateForCategoryBehavior(
        category: CategoryEntity,
        isInStopList: Boolean,
    ) {
        category.isInStopList = isInStopList
        categoryListViewModel.saveCategory(category)
    }

    private fun onCategoryClickBehavior(categoryId: Long) {
        sharedModel?.select(
            SharedMsg(
                if (workMode==WorkMode.MenuCreate) MsgState.DISHESLIST // открываем списко блюд либо для редактирвоания
                else MsgState.OPENFORORDER,   // либо для добавления в заказ
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
        categoryListViewModel.saveCategory(category)
    }
}