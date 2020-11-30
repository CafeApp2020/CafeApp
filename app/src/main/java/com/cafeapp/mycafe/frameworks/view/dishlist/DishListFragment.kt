package com.cafeapp.mycafe.frameworks.view.dishlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.frameworks.picasso.setImage
import com.cafeapp.mycafe.interface_adapters.viewmodels.dishes.dish.DishViewModel
import com.cafeapp.mycafe.interface_adapters.viewmodels.dishes.dishlist.DishListViewModel
import com.cafeapp.mycafe.use_case.utils.MsgState
import com.cafeapp.mycafe.use_case.utils.SharedMsg
import com.cafeapp.mycafe.use_case.utils.SharedViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.less.repository.db.room.CategoryEntity
import com.less.repository.db.room.DishesEntity
import kotlinx.android.synthetic.main.category_view_holder.*
import kotlinx.android.synthetic.main.fragment_dish.*
import kotlinx.android.synthetic.main.fragment_disheslist.view.*
import org.koin.androidx.scope.currentScope

// Экран для отображения списка блюд
class DishListFragment : Fragment() {
    var currentCategoryID: Long = 0
    private lateinit var dishListAdapter: DishListRVAdapter
    private val dishListViewModel: DishListViewModel by currentScope.inject()
    private val dishViewModel: DishViewModel by currentScope.inject()

    private val sharedModel by lazy {
        activity?.let { ViewModelProvider(it).get(SharedViewModel::class.java) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_disheslist, container, false)

        initRecyclerView(root)

        try {
            dishListViewModel.dishListViewStateToObserve.observe(viewLifecycleOwner, { state ->
                state.dishList?.let { dishList ->
                    dishListAdapter.data = dishList
                }
            })
        } catch(e: Exception){
            sharedModel?.select(SharedMsg(MsgState.CATEGORYLISTOPEN, currentCategoryID))
        }

        val fab=activity?.findViewById<FloatingActionButton>(R.id.activityFab)
        fab?.setImageResource(android.R.drawable.ic_input_add)
        fab?.setOnClickListener {
            sharedModel?.select(SharedMsg(MsgState.ADDDISH, currentCategoryID))
        }

        sharedModel?.getSelected()?.observe(viewLifecycleOwner, { msg ->
               when (msg.stateName) {
                  MsgState.DISHESLIST-> {
                      if (msg.value is CategoryEntity) {
                          currentCategoryID = msg.value.id
                          dishListViewModel.getDishList(msg.value.id)
                          msg.value?.name?.let { name ->
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
                          dishListViewModel.getDishList(msg.value)
                      }
                  }
               }
        })
        return root
    }

    private fun initRecyclerView(root: View) {
        dishListAdapter = DishListRVAdapter { id, button ->
            dishViewModel.getDish(id)
            dishViewModel.dishViewState.observe(viewLifecycleOwner, { state ->
                state.dish?.let { dish ->
                    buttonListener(dish, button, id)
                }
            })
        }

        root.dishlist_recyclerview.apply {
            adapter = dishListAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun editClick(id: Long){
        sharedModel?.select(SharedMsg(MsgState.OPENDISH, id))
    }

    private fun changeStopListClick(currentDish: DishesEntity, stopState: Boolean){
        currentDish.in_stop_list = stopState
        dishViewModel.editDish(currentDish)
    }

    private fun buttonListener(dish: DishesEntity, button: Int, id: Long){
        val currentDish: DishesEntity = dish;
        when(button){
            R.id.dishViewHolderLeftSide -> editClick(id)
            R.id.dishViewHolderAddStopButton -> changeStopListClick(currentDish, true)
            R.id.dishViewHolderRemoveStopButton -> changeStopListClick(currentDish, false)
        }
    }
}