package com.cafeapp.mycafe.frameworks.view.menu.categoryadd

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.interface_adapters.viewmodels.categories.CategoryViewModel
import com.cafeapp.mycafe.use_case.utils.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.less.repository.db.room.CategoryEntity
import kotlinx.android.synthetic.main.fragment_addcategory.*
import org.koin.androidx.scope.currentScope

// Экран для добавления/редактирования категорий
class CategoryAddFragment : Fragment() {
    private val categoryViewModel: CategoryViewModel by currentScope.inject()
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
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_addcategory, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCategoryViewModelObserver()
        initSharedViewModelObserver()
        initFab()

        checkEditTextFocus(categoryNameTIT, getString(R.string.enter_category_name))
    }

    private fun initFab() {
        val fab = activity?.findViewById<FloatingActionButton>(R.id.activityFab)

        fab?.setImageResource(android.R.drawable.ic_menu_save)
        fab?.setOnClickListener {
            saveCategory()
        }
    }

    private fun initSharedViewModelObserver() {
        sharedModel?.getSelected()?.observe(viewLifecycleOwner, { msg ->
            when (msg.stateName) {
                MsgState.ADDCATEGORY ->
                    if (msg.value is Long)
                        if (msg.value > -1) {  //"Выбрана категория с id= " + msg.value + " для редактирования. Если id=-1 добавляем новую категорию",
                            loadEditableCategory(msg.value)
                        } else currentCategoryId = -1L
            }
        })
    }

    private fun initCategoryViewModelObserver() {
        categoryViewModel.categoryViewState.observe(viewLifecycleOwner, {
            when {
                it.saveErr != null -> {
                    Toast.makeText(activity, it.saveErr.message, Toast.LENGTH_LONG).show()
                }
                it.saveOk -> {
                    Toast.makeText(activity, getString(R.string.saveok_title), Toast.LENGTH_LONG)
                        .show()
                    sharedModel?.select(
                        SharedMsg(
                            MsgState.CATEGORYLISTOPEN,
                            -1
                        )
                    )  // сообщаем активити о том, что нужно открыть окно со списком категорий
                }

                it.loadOk -> {
                    it.category?.let { it1 -> loadCategory(it1) }
                }
            }
        })
    }

    private fun saveCategory() {
        if (!isError(categoryNameTIT, getString(R.string.enter_category_name))) {
            val category = CategoryEntity(
                name = categoryNameTIT.text.toString(),
                description = descriptionTIT.text.toString(),
                imagepath = ""
            )

            if (currentCategoryId > 0)
                category.id = currentCategoryId

            categoryViewModel.saveCategory(category)
        }
    }

    private fun loadEditableCategory(category_id: Long) {
        categoryViewModel.loadCategory(category_id)
    }
}