package com.cafeapp.mycafe.frameworks.view.categorylist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.frameworks.picasso.setImage
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.less.repository.db.room.CategoryEntity
import kotlinx.android.synthetic.main.category_view_holder.view.*

// пердаем адаптеру лямду getIdFunc, которая отрабатывает в CategoryListFragment при нажатии на категорию
class CategoryListRVAdapter(val getIdFunc: (CategoryEntity) -> Unit) :
    RecyclerView.Adapter<CategoryListRVAdapter.CategoryViewHolder>() {

    private val viewBinderHelper: ViewBinderHelper = ViewBinderHelper()

    var data: List<CategoryEntity?>? = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CategoryViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.category_view_holder,
            parent,
            false
        ), getIdFunc
    )

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentCategory: CategoryEntity? = data?.get(position)
        viewBinderHelper.bind(holder.swipeRevealLayout, currentCategory?.id.toString())

        data?.get(position)?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = data!!.size

    class CategoryViewHolder(itemView: View, val getIdFunc: (CategoryEntity) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        val swipeRevealLayout: SwipeRevealLayout = itemView.swipe_categoryViewHolder

        fun bind(data: CategoryEntity) = with(itemView) {
            name.text = data.name

            categoryViewHolderLeftSide.setOnClickListener {
                getIdFunc(data)
            }

            if (data.imagepath.isNotEmpty())
                setImage(data.imagepath, image)
        }
    }
}