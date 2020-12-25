package com.cafeapp.mycafe.frameworks.view.menu.dishlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.frameworks.view.BaseFragment
import com.cafeapp.mycafe.frameworks.view.menu.categorylist.CategoryListFragment
import com.cafeapp.mycafe.frameworks.view.menu.categorylist.WorkMode
import com.cafeapp.mycafe.frameworks.view.utils.RecyclerViewUtil
import com.cafeapp.mycafe.interface_adapters.viewmodels.categories.CategoryViewModel
import com.cafeapp.mycafe.interface_adapters.viewmodels.categories.CategoryViewState
import com.cafeapp.mycafe.interface_adapters.viewmodels.dishes.DishViewModel
import com.cafeapp.mycafe.interface_adapters.viewmodels.dishes.DishesViewState
import com.cafeapp.mycafe.use_case.utils.MsgState
import com.cafeapp.mycafe.use_case.utils.SharedMsg
import com.cafeapp.mycafe.use_case.utils.SharedViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.less.repository.db.room.CategoryEntity
import com.less.repository.db.room.DishesEntity
import kotlinx.android.synthetic.main.fragment_dishlist.*
import kotlinx.android.synthetic.main.fragment_dishlist.view.*
import org.koin.androidx.scope.currentScope
import java.util.*

// Экран для отображения списка блюд
class DishListFragment : BaseFragment<DishViewModel, DishesViewState>() {
    private var workMode:WorkMode=WorkMode.MenuCreate // режим работы: создание/редактирование меню либо выбор блюд для заказа
    var currentCategoryID: Long = 0
    private lateinit var dishListAdapter: DishListRVAdapter
    override val viewModel: DishViewModel by currentScope.inject()

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
    }

    private fun initViews(view: View) {
        initRecyclerView()
        setFabImageResource(android.R.drawable.ic_input_add)
    }

    private fun initRecyclerView() {
        dishListAdapter = DishListRVAdapter(listener)

        dishlist_recyclerview.apply {
            adapter = dishListAdapter
            layoutManager = LinearLayoutManager(activity)
                RecyclerViewUtil.addDecorator(context, this)
            }
         dishListAdapter.updateSelectedDishList(CategoryListFragment.selectedDishListForOrder)
        }

    override fun onViewModelMsg(state: DishesViewState) {
        try {
               super.onViewModelMsg(state)
               if (state.saveOk )
                   viewModel.getDishList(currentCategoryID)
                state.dishList?.let { dList ->
                    initRecyclerView()
                    dishListAdapter.setDishList(dList)
                }
            }
           catch (e: Exception) {
            sharedModel?.select(SharedMsg(MsgState.CATEGORYLISTOPEN, currentCategoryID))
        }
    }

    private fun loadDishFromCategory(categoryIdAny:Any) {
       if (categoryIdAny is CategoryEntity) {
            currentCategoryID = categoryIdAny.id
            viewModel.getDishList(categoryIdAny.id)
           categoryIdAny.name.let { name ->
                sharedModel?.select( SharedMsg(MsgState.SETTOOLBARTITLE, name))
            }
        }
    }

    private fun loadDishFromCategoryId(categorId:Any) {
        try {
        if (categorId is Long) {
            currentCategoryID = categorId
            viewModel.getDishList(categorId)
        }
        }
        catch (e: Exception) {
            sharedModel?.select(SharedMsg(MsgState.CATEGORYLISTOPEN, currentCategoryID))
        }
    }

    private fun openForOrder(categorId:Any) {
        if (categorId is Long) {
            workMode=WorkMode.OrderSelect
            loadDishFromCategoryId(categorId)
            val fab = activity?.findViewById<FloatingActionButton>(R.id.activityFab)
            fab?.setImageResource(R.drawable.ic_list_add_check_24)
            dishListAdapter.updateSelectedDishList(CategoryListFragment.selectedDishListForOrder)
            fab?.setOnClickListener {
                 sharedModel?.select(SharedMsg(CategoryListFragment.getCurrentOrderType(),
                     mapOf(CategoryListFragment.orderEntity to CategoryListFragment.selectedDishListForOrder)))
            }
        }
    }

    private fun editClick(id: Long) {
        sharedModel?.select(SharedMsg(MsgState.OPENDISH, id))
    }

    private fun changeStopListClick(currentDish: DishesEntity, stopState: Boolean) {
        currentDish.in_stop_list = stopState
        viewModel.editDish(currentDish)
    }

    private fun removeClick(currentDish: DishesEntity) {
        currentDish.deleted = true
        viewModel.editDish(currentDish)
    }

    override fun onMainFabClick() {
        sharedModel?.select(SharedMsg(MsgState.ADDDISH, currentCategoryID))
    }

    override fun onSharedMsg(msg: SharedMsg) {
        when (msg.stateName) {
         MsgState.DISHESLIST ->
                {CategoryListFragment.selectedDishListForOrder = mutableListOf<Long>()
                 loadDishFromCategory(msg.value)
                 loadDishFromCategoryId(msg.value)}
         MsgState.OPENFORORDER -> openForOrder(msg.value)
         }
    }
}