package com.cafeapp.mycafe.frameworks.view.categoryadd

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.interface_adapters.viewmodels.categories.CategoryAddViewModel
import com.cafeapp.mycafe.use_case.utils.MsgState
import com.cafeapp.mycafe.use_case.utils.SharedMsg
import com.cafeapp.mycafe.use_case.utils.SharedViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.less.repository.db.room.CategoryEntity
import kotlinx.android.synthetic.main.fragment_addcategory.*
import org.koin.androidx.scope.currentScope

// Экран для добавления/редактирования категорий
class CategoryAddFragment : Fragment() {
    private val categoryAddViewModel: CategoryAddViewModel by currentScope.inject()

    private var color: Int = 0
    private var currentCategoryId: Long = -1L

    private val sharedModel by lazy {
        activity?.let { ViewModelProvider(it).get(SharedViewModel::class.java) }
    }

    private fun loadCategory(category: CategoryEntity) {
        categoryNameTIT.setText(category.name)
        descriptionTIT.setText(category.description)

        currentCategoryId = category.id
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_addcategory, container, false)

        categoryAddViewModel.categoryViewState.observe(viewLifecycleOwner, {
            if (it.saveErr != null) {
                Toast.makeText(activity, it.saveErr?.message, Toast.LENGTH_LONG).show()
            } else if (it.saveOk) {
                Toast.makeText(activity, getString(R.string.saveok_title), Toast.LENGTH_LONG).show()
                sharedModel?.select(
                    SharedMsg(
                        MsgState.CATEGORYLISTOPEN,
                        -1
                    )
                )  // сообщаем активити о том, что нужно открыть окно со списком категорий
            } else if (it.loadOk) {
                it.category?.let { it1 -> loadCategory(it1) }
            }
        })

        sharedModel?.getSelected()?.observe(viewLifecycleOwner, { msg ->
            when (msg.stateName) {
                MsgState.ADDCATEGORY ->
                    if (msg.value is Long)
                        if (msg.value > -1) {  //"Выбрана категория с id= " + msg.value + " для редактирования. Если id=-1 добавляем новую категорию",
                            loadEditableCategory(msg.value)
                        } else currentCategoryId = -1L
            }
        })

        val fab=activity?.findViewById<FloatingActionButton>(R.id.activityFab)
        if (fab != null) {fab.setImageResource(android.R.drawable.ic_menu_save)}
        fab?.setOnClickListener {
            saveCategory()
        }

        return root
    }

    fun saveCategory() {
        val category = CategoryEntity(
            name = categoryNameTIT.text.toString(),
            description = descriptionTIT.text.toString(),
            imagepath = "",
            color = color
        )

        if (currentCategoryId > 0)
            category.id = currentCategoryId
        categoryAddViewModel.saveCategory(category)
    }


    private fun loadEditableCategory(category_id: Long) {
        categoryAddViewModel.loadCategory(category_id)
    }
}

