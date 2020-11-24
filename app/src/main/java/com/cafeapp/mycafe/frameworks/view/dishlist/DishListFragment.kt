package com.cafeapp.mycafe.frameworks.view.dishlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.interface_adapters.viewmodels.dishes.dishlist.DishListViewModel
import com.cafeapp.mycafe.use_case.utils.MsgState
import com.cafeapp.mycafe.use_case.utils.SharedMsg
import com.cafeapp.mycafe.use_case.utils.SharedViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.less.repository.db.room.CategoryEntity
import kotlinx.android.synthetic.main.fragment_disheslist.view.*
import org.koin.androidx.scope.currentScope

// Экран для отображения списка блюд
class DishListFragment : Fragment() {
    var currentCategoryID: Long = 0
    private lateinit var dishListAdapter: DishListRVAdapter
    private val dishListViewModel: DishListViewModel by currentScope.inject()

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

        dishListViewModel.dishListViewStateToObserve.observe(viewLifecycleOwner, { state ->
            state.dishList?.let { dishList ->
                dishListAdapter.data = dishList
            }
        })

        val fab=activity?.findViewById<FloatingActionButton>(R.id.activityFab)
        if (fab != null) {fab.setImageResource(android.R.drawable.ic_input_add)}
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
        dishListAdapter = DishListRVAdapter { id ->
            sharedModel?.select(SharedMsg(MsgState.OPENDISH, id))
        }

        root.dishlist_recyclerview.apply {
            adapter = dishListAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}