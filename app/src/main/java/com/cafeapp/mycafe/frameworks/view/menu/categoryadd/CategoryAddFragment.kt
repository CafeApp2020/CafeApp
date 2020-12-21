package com.cafeapp.mycafe.frameworks.view.menu.categoryadd

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.frameworks.view.BaseFragment
import com.cafeapp.mycafe.interface_adapters.viewmodels.categories.CategoryViewModel
import com.cafeapp.mycafe.interface_adapters.viewmodels.categories.CategoryViewState
import com.cafeapp.mycafe.use_case.utils.*
import com.less.repository.db.room.CategoryEntity
import kotlinx.android.synthetic.main.fragment_addcategory.*
import org.koin.androidx.scope.currentScope

// Экран для добавления/редактирования категорий
class CategoryAddFragment : BaseFragment<CategoryViewModel, CategoryViewState>() {
    override val viewModel: CategoryViewModel by currentScope.inject()
    private var currentCategoryId: Long = -1L

    private fun loadCategory(category: CategoryEntity) {
        categoryNameTIT.setText(category.name)
        descriptionTIT.setText(category.description)
        currentCategoryId = category.id
    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?, savedInstanceState: Bundle?,): View? {
        return inflater.inflate(R.layout.fragment_addcategory, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFabImageResource(android.R.drawable.ic_menu_save)
        checkEditTextFocus(categoryNameTIT, getString(R.string.enter_category_name))
    }

    override fun onViewModelMsg(state: CategoryViewState) {
        super.onViewModelMsg(state)
        when {
            state.saveOk -> {
                Toast.makeText(activity, getString(R.string.saveok_title), Toast.LENGTH_LONG).show()
                sharedModel?.select(SharedMsg(MsgState.CATEGORYLISTOPEN, -1))
            }
            state.loadOk -> {
                state.category?.let { it1 -> loadCategory(it1) }
            }
        }
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
            viewModel.saveCategory(category)
        }
    }

    private fun loadEditableCategory(category_id: Long) =   viewModel.loadCategory(category_id)

    override fun onSharedMsg(msg: SharedMsg) {
        if (msg.stateName == MsgState.ADDCATEGORY && msg.value is Long) {
            if (msg.value > -1) {  //"Выбрана категория с id= " + msg.value + " для редактирования. Если id=-1 добавляем новую категорию",
                loadEditableCategory(msg.value)
            } else currentCategoryId = -1L
        }
    }

    override fun onMainFabClick() = saveCategory()
}