package com.cafeapp.mycafe.frameworks.view.dishlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.interface_adapters.viewmodels.dishes.dish.DishViewModel
import com.cafeapp.mycafe.interface_adapters.viewmodels.dishes.dishlist.DishListViewModel
import com.cafeapp.mycafe.use_case.utils.MsgState
import com.cafeapp.mycafe.use_case.utils.SharedMsg
import com.cafeapp.mycafe.use_case.utils.SharedViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.less.repository.db.room.CategoryEntity
import com.less.repository.db.room.DishesEntity
import kotlinx.android.synthetic.main.fragment_dishlist.view.*
import org.koin.androidx.scope.currentScope

// Экран для отображения списка блюд
class DishListFragment : Fragment() {
    var currentCategoryID: Long = 0
    private lateinit var dishListAdapter: DishListRVAdapter

    private val listener: OnDishListItemClickListener =
        object : OnDishListItemClickListener {
            override fun onChangeStopListStateForDish(dish: DishesEntity, isInStopList: Boolean) {
                changeStopListClick(dish, isInStopList)
            }

            override fun onDishClick(dishId: Long) {
                editClick(dishId)
            }

            override fun onRemoveDishButtonClick(dish: DishesEntity) {
                removeClick(dish)
            }
        }

    private val dishListViewModel: DishListViewModel by currentScope.inject()
    private val dishViewModel: DishViewModel by currentScope.inject()

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
            dishListViewModel.dishListViewStateToObserve.observe(viewLifecycleOwner, { state ->
                state.dishList?.let { dishList ->
                    dishListAdapter.setDishList(dishList) //здесь происходит баг, break point, при нажатии кнопки назад с других экранов, не запрашивает данные у бд
                }
            })
        } catch (e: Exception) {
            sharedModel?.select(SharedMsg(MsgState.CATEGORYLISTOPEN, currentCategoryID))
        }
    }

    private fun initSharedModelObserver() {
        sharedModel?.getSelected()?.observe(viewLifecycleOwner, { msg ->
            when (msg.stateName) {
                MsgState.DISHESLIST -> {
                    if (msg.value is CategoryEntity) {
                        currentCategoryID = msg.value.id
                        dishListViewModel.getDishList(msg.value.id) // break point
                        msg.value.name.let { name ->
                            sharedModel?.select(
                                SharedMsg(
                                    MsgState.SETTOOLBARTITLE,
                                    name
                                )
                            )
                        }
                    }

                    if (msg.value is Long) {
                        currentCategoryID = msg.value
                        dishListViewModel.getDishList(msg.value)  // break point
                    }
                }
            }
        })
    }

    private fun editClick(id: Long) {
        sharedModel?.select(SharedMsg(MsgState.OPENDISH, id))
    }

    private fun changeStopListClick(currentDish: DishesEntity, stopState: Boolean) {
        currentDish.in_stop_list = stopState
        dishViewModel.editDish(currentDish)
    }

    private fun removeClick(currentDish: DishesEntity) {
        currentDish.deleted = true
        dishViewModel.editDish(currentDish)
    }
}