package com.cafeapp.mycafe.frameworks.view.dish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.frameworks.picasso.setImage
import com.cafeapp.mycafe.interface_adapters.viewmodels.dishes.dish.DishViewModel
import com.cafeapp.mycafe.use_case.utils.MsgState
import com.cafeapp.mycafe.use_case.utils.SharedViewModel
import com.less.repository.db.room.DishesEntity
import kotlinx.android.synthetic.main.fragment_dish.*
import org.koin.androidx.scope.currentScope

class DishFragment : Fragment() {
    private val dishViewModel: DishViewModel by currentScope.inject()

    private val sharedModel by lazy {
        activity?.let { ViewModelProvider(it).get(SharedViewModel::class.java) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dish, container, false)

        dishViewModel.dishViewStateToObserve.observe(viewLifecycleOwner, { state ->
            state.dish?.let { dish ->
                showDish(dish)
            }
        })

        sharedModel?.getSelected()?.observe(viewLifecycleOwner, { msg ->
            when (msg.stateName) {
                MsgState.DISH ->
                    if (msg.value is Long) {
                        loadDish(msg.value)
                    }
            }
        })

        return root
    }

    private fun loadDish(id: Long) {
        dishViewModel.getDish(id)
    }

    private fun showDish(dish: DishesEntity) {
        dishNameTIT.setText(dish.name)
        priceTIT.setText(dish.price.toString())
        weightTIT.setText(dish.weight.toString())
        descriptionTIT.setText(dish.description)

        val imagePath = dish.imagepath.toString()

        if (imagePath.isNotEmpty()) {
            setImage(dish.imagepath.toString(), fragment_dish_image_imageview)
        }
    }
}