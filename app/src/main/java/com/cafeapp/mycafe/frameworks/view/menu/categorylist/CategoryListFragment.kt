package com.cafeapp.mycafe.frameworks.view.menu.categorylist

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.frameworks.room.OrdersEntity
import com.cafeapp.mycafe.frameworks.view.BaseFragment
import com.cafeapp.mycafe.frameworks.view.delivery.OrderType
import com.cafeapp.mycafe.frameworks.view.utils.RecyclerViewUtil
import com.cafeapp.mycafe.interface_adapters.viewmodels.categories.CategoryViewModel
import com.cafeapp.mycafe.interface_adapters.viewmodels.categories.CategoryViewState
import com.cafeapp.mycafe.interface_adapters.viewmodels.tables.TableViewState
import com.cafeapp.mycafe.use_case.utils.MsgState
import com.cafeapp.mycafe.use_case.utils.SharedMsg
import com.cafeapp.mycafe.use_case.utils.SharedViewModel
import com.cafeapp.mycafe.use_case.utils.isError
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.less.repository.db.room.CategoryEntity
import kotlinx.android.synthetic.main.dialog_add_category.view.*
import kotlinx.android.synthetic.main.fragment_categorylist.*
import kotlinx.android.synthetic.main.fragment_categorylist.view.*
import org.koin.androidx.scope.currentScope

enum class WorkMode { MenuCreate, OrderSelect}

// Экран для отображения категорий блюд
class CategoryListFragment() : BaseFragment<CategoryViewModel, CategoryViewState>() {
    private var workMode: WorkMode =  WorkMode.MenuCreate // режим работы: создание/редактирование меню либо выбор блюд для заказа
    private lateinit var categoryListAdapter: CategoryListRVAdapter
    override val viewModel: CategoryViewModel by currentScope.inject()
    private var categoryListRwFirstInit = true
    private lateinit var gDialog: AlertDialog
    private var categoryListLastElement: Int = 0

    companion object {
        var orderEntity: OrdersEntity? = null
        var selectedDishListForOrder = mutableListOf<Long>() //ID выбранных в текущем заказе блюд
        fun getCurrentOrderType(): MsgState {
            return when (orderEntity?.ordertype) {
                OrderType.DELIVERY -> MsgState.DELEVERYOPEN
                OrderType.TAKEAWAY -> MsgState.TAKEAWAYOPEN
                else -> MsgState.DELEVERYOPEN
            }
        }
    }

    private val listener: OnCategoryListItemClickListener =
        object : OnCategoryListItemClickListener {
            override fun onCategoryClick(categoryId: Long) =
                onCategoryClickBehavior(categoryId)

            override fun onEditCategoryButtonClick(categoryId: Long) =
                onEditCategoryButtonClickBehavior(categoryId)

            override fun onRemoveCategoryButtonClick(category: CategoryEntity) =
                onRemoveCategoryButtonClickBehavior(category)

            override fun onChangeStopListStateForCategory(
                category: CategoryEntity,
                isInStopList: Boolean,
            ) =  onChangeStopListStateForCategoryBehavior(category, isInStopList)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_categorylist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        viewModel.getCategories()
    }

    override fun onViewModelMsg(state: CategoryViewState) {
        super.onViewModelMsg(state)
        state.category?.let {
            viewModel.getCategories()
        }
        state.categoryList?.let { categoryList ->
            initRecyclerView()
            categoryListLastElement = categoryList.size
            categoryListAdapter.setCategoryList(categoryList)
        }
    }

    private fun initSelectOrderMode() {
       val fab = activity?.findViewById<FloatingActionButton>(R.id.activityFab)
        fab?.setImageResource(R.drawable.ic_list_add_check_24)
        fab?.setOnClickListener {
            sharedModel?.select(SharedMsg(getCurrentOrderType(),
                mapOf(orderEntity to selectedDishListForOrder)))
        }
    }

    private fun initViews() {
        initRecyclerView()
        setFabImageResource(if (workMode == WorkMode.OrderSelect) R.drawable.ic_list_add_check_24
                            else android.R.drawable.ic_input_add)
    }

    private fun initRecyclerView() {
        categoryListAdapter = CategoryListRVAdapter(listener)

        categoryListRV.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = categoryListAdapter
            RecyclerViewUtil.addDecorator(context, this)
            categoryListRwFirstInit=false
        }
    }

    private fun onChangeStopListStateForCategoryBehavior(
        category: CategoryEntity,
        isInStopList: Boolean,
    ) {
        category.isInStopList = isInStopList
        viewModel.saveCategory(category)
    }

    private fun onCategoryClickBehavior(categoryId: Long) {
        sharedModel?.select(SharedMsg(if (workMode == WorkMode.MenuCreate) MsgState.DISHESLIST // открываем списко блюд либо для редактирвоания
                else MsgState.OPENFORORDER,   // либо для добавления в заказ
                categoryId))
    }

    private fun onEditCategoryButtonClickBehavior(categoryId: Long) {
        sharedModel?.select(SharedMsg(MsgState.ADDCATEGORY, categoryId))
    }

    private fun onRemoveCategoryButtonClickBehavior(category: CategoryEntity) {
        category.isDeleted = true
        viewModel.saveCategory(category)
    }

    private fun showAddCategoryDialog(defaultText: String): AlertDialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        val view: View = inflater.inflate(R.layout.dialog_add_category, null)
        builder.setView(view)
        view.categoryNameTIT.setText(defaultText)
        val dialog = builder.create()
        view.cancelBtn.setOnClickListener {
            gDialog.dismiss()
        }
        view.saveCatBtn.setOnClickListener {
            view.categoryNameTIT?.let {
                if(!isError(view.categoryNameTIT, getString(R.string.text_category_name))){
                    addNewCategory(it.text.toString())
                    gDialog.dismiss()}
            }
        }
        return dialog
    }

    private fun addNewCategory(name: String) {
        val categoryEntity = CategoryEntity(
            name = name,
            description = "",
            imagepath = "")
        viewModel.saveCategory(categoryEntity)
        categoryListAdapter.notifyItemInserted(categoryListLastElement)
    }

    override fun onMainFabClick() {
        if (workMode == WorkMode.OrderSelect)
           sharedModel?.select(SharedMsg(getCurrentOrderType(), mapOf(orderEntity to selectedDishListForOrder)))
        else {
           gDialog = showAddCategoryDialog("")
           gDialog.show()
        }
    }

    override fun onSharedMsg(msg: SharedMsg) {
        if (msg.stateName==MsgState.SELECTDISHTOORDER&&msg.value is OrdersEntity)
           {  orderEntity = msg.value
              selectedDishListForOrder.clear()
               workMode = WorkMode.OrderSelect
              initSelectOrderMode()
            }
        }
}