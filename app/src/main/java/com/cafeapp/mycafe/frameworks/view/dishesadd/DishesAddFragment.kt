package com.cafeapp.mycafe.frameworks.view.dishesadd

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.frameworks.picasso.setImage
import com.cafeapp.mycafe.interface_adapters.viewmodels.dishes.dish.DishViewModel
import com.cafeapp.mycafe.use_case.utils.MsgState
import com.cafeapp.mycafe.use_case.utils.SharedMsg
import com.cafeapp.mycafe.use_case.utils.SharedViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.less.repository.db.room.DishesEntity
import kotlinx.android.synthetic.main.fragment_dishesadd.descriptionTIT
import kotlinx.android.synthetic.main.fragment_dishesadd.dishNameTIT
import kotlinx.android.synthetic.main.fragment_dishesadd.fragment_dish_image_imageview
import kotlinx.android.synthetic.main.fragment_dishesadd.priceTIT
import kotlinx.android.synthetic.main.fragment_dishesadd.weightTIT
import org.koin.androidx.scope.currentScope

// Экран для добавления/редактирования блюда
class DishesAddFragment : Fragment() {
    var currentCategoryID: Long = -1L
    private var currentDishId: Long = -1L
    private val dishViewModel: DishViewModel by currentScope.inject()

    private val sharedModel by lazy {
        activity?.let { ViewModelProvider(it).get(SharedViewModel::class.java) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dishesadd, container, false)

        dishViewModel.dishViewState.observe(viewLifecycleOwner, { state ->
             state.error?.let { error ->
                Toast.makeText(activity, error?.message, Toast.LENGTH_LONG).show()
                 return@observe
            }
            if (state.saveOk) {
                Toast.makeText(activity, getString(R.string.saveok_title), Toast.LENGTH_LONG).show()
                state.dish?.category_id?.let {category_id ->
                sharedModel?.select(SharedMsg(MsgState.DISHESLIST, category_id))}
            }
            if (state.loadOk) {
                state.dish?.let { dish -> showDish(dish) }
            }
        })

        sharedModel?.getSelected()?.observe(viewLifecycleOwner, { msg ->
            when (msg.stateName) {
                MsgState.ADDDISH ->
                    if (msg.value is Long) {
                    currentCategoryID=msg.value
               }
                MsgState.EDITDISH ->
                    if (msg.value is Long) {
                       dishViewModel.getDish(msg.value)
                       currentDishId=msg.value
                    }
            }
        })

        val fab=activity?.findViewById<FloatingActionButton>(R.id.activityFab)
        if (fab != null) {fab.setImageResource(android.R.drawable.ic_menu_save)}
        fab?.setOnClickListener {
            saveDish()
        }
        return root
    }

    private fun showDish(dish: DishesEntity) {
        dish?.name?.let {name -> sharedModel?.select(SharedMsg(MsgState.SETTOOLBARTITLE, name))}
        currentCategoryID=dish.category_id
        dishNameTIT.setText(dish.name)
        priceTIT.setText(dish.price.toString())
        weightTIT.setText(dish.weight.toString())
        descriptionTIT.setText(dish.description)

        val imagePath = dish.imagepath.toString()

        if (imagePath.isNotEmpty()) {
            setImage(dish.imagepath.toString(), fragment_dish_image_imageview)
        }
    }

    fun saveDish() {
        var dish: DishesEntity? =null
        dish = DishesEntity(
            category_id = currentCategoryID,
            name = dishNameTIT.text.toString(),
            description = descriptionTIT.text.toString(),
            price = priceTIT.text.toString().toFloat(),
            weight = weightTIT.text.toString().toFloat(),
            imagepath = ""
        )

        if (currentDishId>0)  // обновляем блюдо иначе добавляем новое
            dish.id=currentDishId

        dish?.let {dishViewModel.saveDish(dish) }
        }
    }
