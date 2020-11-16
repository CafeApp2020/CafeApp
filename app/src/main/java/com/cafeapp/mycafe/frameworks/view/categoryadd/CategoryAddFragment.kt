package com.cafeapp.mycafe.frameworks.view.categoryadd

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.interface_adapters.viewmodels.categories.CategoryAddViewModel
import com.cafeapp.mycafe.use_case.utils.MsgState
import com.cafeapp.mycafe.use_case.utils.SharedViewModel
import com.less.repository.db.room.CategoryEntity
import kotlinx.android.synthetic.main.fragment_addcategory.*
import kotlinx.android.synthetic.main.fragment_disheslist.view.*
import org.koin.androidx.scope.currentScope

// Экран для добавления/редактирования категорий
class CategoryAddFragment : Fragment() {

    val categoryAddViewModel: CategoryAddViewModel by currentScope.inject()
    private val sharedModel by lazy {
        activity?.let { ViewModelProvider(it).get(SharedViewModel::class.java) }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_addcategory, container, false)

        categoryAddViewModel.categoryViewState.observe(viewLifecycleOwner, {
            if (it.saveErr==null) {
                Toast.makeText(activity, getString(R.string.saveok_title), Toast.LENGTH_LONG).show()
            } else
            {
                Toast.makeText(activity, it.saveErr?.message, Toast.LENGTH_LONG).show()
            }
        })

        sharedModel?.getSelected()?.observe(viewLifecycleOwner, { msg ->
            when (msg.stateName) {
                MsgState.ADDDISH ->
                    if (msg.value is Int)
                        Toast.makeText(
                                activity,
                                "Выбрана категория с id= " + msg.value + " для редактирования. Если id=-1 добавляем новую категорию",
                                Toast.LENGTH_LONG
                        ).show()
            }
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        saveCategoryFab.setOnClickListener {
            val category: CategoryEntity = CategoryEntity(
                    name = categoryNameTIL.text.toString(),
                    description = descriptionTIT.text.toString(),
                    imagepath = ""
            )
        categoryAddViewModel.saveCategory(category)
        }
    }
}
