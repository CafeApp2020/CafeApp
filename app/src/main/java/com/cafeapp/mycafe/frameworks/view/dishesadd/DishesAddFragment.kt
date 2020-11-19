package com.cafeapp.mycafe.frameworks.view.dishesadd

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.interface_adapters.viewmodels.dishes.dishesadd.DishesAddViewModel
import com.cafeapp.mycafe.use_case.utils.MsgState
import com.cafeapp.mycafe.use_case.utils.SharedViewModel
import com.less.repository.db.room.DishesEntity
import kotlinx.android.synthetic.main.fragment_dishesadd.*
import org.koin.androidx.scope.currentScope

// Экран для добавления/редактирования блюда
class DishesAddFragment : Fragment() {
    private val dishesAddViewModel: DishesAddViewModel by currentScope.inject()

    private val sharedModel by lazy {
        activity?.let { ViewModelProvider(it).get(SharedViewModel::class.java) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dishesadd, container, false)

        dishesAddViewModel.dishViewState.observe(viewLifecycleOwner, { state ->
            state.saveErr?.let { error ->
                Toast.makeText(activity, error?.message, Toast.LENGTH_LONG).show()
            } ?: let {
                Toast.makeText(activity, getString(R.string.saveok_title), Toast.LENGTH_LONG).show()
            }
        })

        sharedModel?.getSelected()?.observe(viewLifecycleOwner, { msg ->
            when (msg.stateName) {
                MsgState.ADDDISH ->
                    if (msg.value is Int)
                        Toast.makeText(
                            activity,
                            "Выбрано блюдо с id= " + msg.value + " для редактирования. Если id=-1 добавляем новое блюдо",
                            Toast.LENGTH_LONG
                        ).show()
            }
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        saveDishFab.setOnClickListener {
            val dish = DishesEntity(
                category_id = 1,
                name = dishNameTIT.text.toString(),
                description = descriptionTIT.text.toString(),
                price = priceTIT.text.toString().toFloat(),
                weight = weightTIT.text.toString().toFloat(),
                imagepath = ""
            )

            dishesAddViewModel.saveDish(dish)
        }
    }
}