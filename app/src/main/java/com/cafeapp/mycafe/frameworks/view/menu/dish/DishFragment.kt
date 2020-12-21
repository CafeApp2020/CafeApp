package com.cafeapp.mycafe.frameworks.view.menu.dish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.frameworks.picasso.setImage
import com.cafeapp.mycafe.frameworks.view.BaseFragment
import com.cafeapp.mycafe.interface_adapters.viewmodels.categories.CategoryViewState
import com.cafeapp.mycafe.interface_adapters.viewmodels.dishes.DishViewModel
import com.cafeapp.mycafe.interface_adapters.viewmodels.dishes.DishesViewState
import com.cafeapp.mycafe.use_case.utils.MsgState
import com.cafeapp.mycafe.use_case.utils.SharedMsg
import com.cafeapp.mycafe.use_case.utils.SharedViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.less.repository.db.room.DishesEntity
import kotlinx.android.synthetic.main.fragment_dish.*
import org.koin.androidx.scope.currentScope

class DishFragment : BaseFragment<DishViewModel, DishesViewState>() {
    private var currentDishID: Long = 0
    override val viewModel: DishViewModel by currentScope.inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_dish, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFabImageResource(android.R.drawable.ic_menu_edit)
    }

    private fun loadDish(id: Long) {
        viewModel.getDish(id)
    }

    private fun showDish(dish: DishesEntity) {
        priceTW.text =  dish.price.toString() + " ₽" // временно делаем так, далее в настройках будем прописывать единицы
        weigthTW.text = dish.weight?.toInt().toString() + " гр" // аналогично
        descriptionTW.text = dish.description
        dish.name?.let { name -> sharedModel?.select(SharedMsg(MsgState.SETTOOLBARTITLE, name)) }

        val imagePath = dish.imagepath.toString()

        if (imagePath.isNotEmpty()) {
            setImage(dish.imagepath.toString(), fragment_dish_image_imageview)
        }
    }

    override fun onMainFabClick() {
        sharedModel?.select(SharedMsg(MsgState.EDITDISH, currentDishID))
    }

    override fun onSharedMsg(msg: SharedMsg) {
        if (msg.stateName == MsgState.OPENDISH && msg.value is Long) {
           currentDishID = msg.value
          loadDish(msg.value)
        }
    }

    override fun onViewModelMsg(state: DishesViewState) {
        super.onViewModelMsg(state)
        state.dish?.let { dish ->   showDish(dish) }
    }
}