package com.cafeapp.mycafe.frameworks.view.menuscreens.dish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.frameworks.picasso.setImage
import com.cafeapp.mycafe.interface_adapters.viewmodels.dishes.DishViewModel
import com.cafeapp.mycafe.use_case.utils.MsgState
import com.cafeapp.mycafe.use_case.utils.SharedMsg
import com.cafeapp.mycafe.use_case.utils.SharedViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.less.repository.db.room.DishesEntity
import kotlinx.android.synthetic.main.fragment_dish.*
import org.koin.androidx.scope.currentScope

class DishFragment : Fragment() {
    private var currentDishID: Long = 0
    private val dishViewModel: DishViewModel by currentScope.inject()

    private val sharedModel by lazy {
        activity?.let { ViewModelProvider(it).get(SharedViewModel::class.java) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dish, container, false)

        dishViewModel.dishViewState.observe(viewLifecycleOwner, { state ->
            state.dish?.let { dish ->
                showDish(dish)
            }
        })

        sharedModel?.getSelected()?.observe(viewLifecycleOwner, { msg ->
            when (msg.stateName) {
                MsgState.OPENDISH ->
                    if (msg.value is Long) {
                        currentDishID = msg.value
                        loadDish(msg.value)
                    }
            }
        })

        val fab = activity?.findViewById<FloatingActionButton>(R.id.activityFab)
        fab?.setImageResource(android.R.drawable.ic_menu_edit)
        fab?.setOnClickListener {
            sharedModel?.select(SharedMsg(MsgState.EDITDISH, currentDishID))
        }

        return root
    }

    private fun loadDish(id: Long) {
        dishViewModel.getDish(id)
    }

    private fun showDish(dish: DishesEntity) {
        priceTW.text =
            dish.price.toString() + " ₽" // временно делаем так, далее в настройках будем прописывать единицы
        weigthTW.text = dish.weight?.toInt().toString() + " гр" // аналогично
        descriptionTW.text = dish.description
        dish.name?.let { name -> sharedModel?.select(SharedMsg(MsgState.SETTOOLBARTITLE, name)) }

        val imagePath = dish.imagepath.toString()

        if (imagePath.isNotEmpty()) {
            setImage(dish.imagepath.toString(), fragment_dish_image_imageview)
        }
    }
}