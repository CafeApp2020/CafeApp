package com.cafeapp.mycafe.frameworks.view.dishlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.interface_adapters.viewmodels.dishes.dishlist.DishListViewModel
import com.cafeapp.mycafe.use_case.utils.MsgState
import com.cafeapp.mycafe.use_case.utils.SharedMsg
import com.cafeapp.mycafe.use_case.utils.SharedViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_disheslist.view.*
import org.koin.androidx.scope.currentScope

// Экран для отображения списка блюд
class DishListFragment : Fragment() {
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
            sharedModel?.select(SharedMsg(MsgState.ADDDISH, -1))
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dishListViewModel.getDishList(1) // пока что в базе есть только категории с id = 1, поэтому их и выводим
    }

    private fun initRecyclerView(root: View) {
        dishListAdapter = DishListRVAdapter { id ->
            sharedModel?.select(SharedMsg(MsgState.DISH, id))
        }

        root.dishlist_recyclerview.apply {
            adapter = dishListAdapter
            layoutManager = GridLayoutManager(activity, 4)
        }
    }
}