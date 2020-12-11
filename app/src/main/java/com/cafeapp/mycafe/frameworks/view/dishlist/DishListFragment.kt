package com.cafeapp.mycafe.frameworks.view.dishlist

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
import com.cafeapp.mycafe.frameworks.view.categorylist.CategoryListFragment
import com.cafeapp.mycafe.frameworks.view.categorylist.WorkMode
import com.cafeapp.mycafe.interface_adapters.viewmodels.dishes.DishViewModel
import com.cafeapp.mycafe.use_case.utils.MsgState
import com.cafeapp.mycafe.use_case.utils.SharedMsg
import com.cafeapp.mycafe.use_case.utils.SharedViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.less.repository.db.room.CategoryEntity
import com.less.repository.db.room.DishesEntity
import kotlinx.android.synthetic.main.fragment_dishlist.view.*
import org.koin.androidx.scope.currentScope
import java.util.*
import kotlin.collections.HashMap

// Экран для отображения списка блюд
class DishListFragment : Fragment() {
    private var workMode:WorkMode=WorkMode.MenuCreate // режим работы: создание/редактирование меню либо выбор блюд для заказа
    var currentCategoryID: Long = 0
    private lateinit var dishListAdapter: DishListRVAdapter

    private val listener: OnDishListItemClickListener =
        object : OnDishListItemClickListener {
            override fun onChangeStopListStateForDish(dish: DishesEntity, isInStopList: Boolean) {
                changeStopListClick(dish, isInStopList)
            }

            override fun onDishClick(dishId: Long) {
                if (workMode==WorkMode.MenuCreate)
                    editClick(dishId)
                else
                    addDishToOrder(dishId)
            }

            override fun onRemoveDishButtonClick(dish: DishesEntity) {
                removeClick(dish)
            }
        }

    private fun addDishToOrder(dishId:Long) {
        with (CategoryListFragment.selectedDishListForOrder) {
          if (!remove(dishId))
              add(dishId)
            dishListAdapter.updateSelectedDishList(this)
        }
   }

    private val dishListViewModel: DishViewModel by currentScope.inject()

    private val sharedModel by lazy {
        activity?.let { ViewModelProvider(it).get(SharedViewModel::class.java) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_dishlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        initViewModelObserver()
        initSharedModelObserver()
    }

    private fun initViews(view: View) {
        initRecyclerView(view)
        initFabButton()
    }

    private fun initRecyclerView(view: View) {
        dishListAdapter = DishListRVAdapter(listener)

        view.dishlist_recyclerview.apply {
            adapter = dishListAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun initFabButton() {
        val fab = activity?.findViewById<FloatingActionButton>(R.id.activityFab)

        fab?.setImageResource(android.R.drawable.ic_input_add)
        fab?.setOnClickListener {
            sharedModel?.select(SharedMsg(MsgState.ADDDISH, currentCategoryID))
        }
    }

    private fun initViewModelObserver() {
        try {
             dishListViewModel.dishViewState.observe(viewLifecycleOwner) { state ->
                 if (state.error!=null) {Toast.makeText(context,state.error.message,Toast.LENGTH_LONG).show()
                     return@observe
                 }
                 if (state.saveOk )
                     dishListViewModel.getDishList(currentCategoryID)
                 state.dishList?.let { dList ->
                     dishListAdapter.setDishList(dList)
                 }
             }
        } catch (e: Exception) {
            sharedModel?.select(SharedMsg(MsgState.CATEGORYLISTOPEN, currentCategoryID))
        }
    }

    private fun loadDishFromCategory(categoryIdAny:Any) {
        if (categoryIdAny is CategoryEntity) {
            currentCategoryID = categoryIdAny.id
            dishListViewModel.getDishList(categoryIdAny.id)
            categoryIdAny.name.let { name ->
                sharedModel?.select(
                    SharedMsg(
                        MsgState.SETTOOLBARTITLE,
                        name
                    )
                )
            }
        }
    }

    private fun loadDishFromCategoryId(categorId:Any) {
        if (categorId is Long) {
            currentCategoryID = categorId
            dishListViewModel.getDishList(categorId)  // break point
        }
    }

    private fun openForOrder(categorId:Any) {
        if (categorId is Long) {
          //  Toast.makeText(context, "id=" + categorId, Toast.LENGTH_LONG).show()
            workMode=WorkMode.OrderSelect
            loadDishFromCategoryId(categorId)
            val fab = activity?.findViewById<FloatingActionButton>(R.id.activityFab)
            fab?.setImageResource(R.drawable.ic_list_add_check_24)
            dishListAdapter.updateSelectedDishList(CategoryListFragment.selectedDishListForOrder)
            fab?.setOnClickListener {
                 sharedModel?.select(SharedMsg(MsgState.RETURNSELECTEDDISHLIST, mapOf(CategoryListFragment.currentOrderID to CategoryListFragment.selectedDishListForOrder)))
            }
        }
    }

    private fun initSharedModelObserver() {
        sharedModel?.getSelected()?.observe(viewLifecycleOwner) { msg ->
            when (msg.stateName) {
                MsgState.DISHESLIST -> {
                    loadDishFromCategory(msg.value)
                    loadDishFromCategoryId(msg.value)
                }
                MsgState.OPENFORORDER -> openForOrder(msg.value)
             }
          }
        }

    private fun editClick(id: Long) {
        sharedModel?.select(SharedMsg(MsgState.OPENDISH, id))
    }

    private fun changeStopListClick(currentDish: DishesEntity, stopState: Boolean) {
        currentDish.in_stop_list = stopState
        dishListViewModel.editDish(currentDish)
    }

    private fun removeClick(currentDish: DishesEntity) {
        currentDish.deleted = true
        dishListViewModel.editDish(currentDish)
    }
}