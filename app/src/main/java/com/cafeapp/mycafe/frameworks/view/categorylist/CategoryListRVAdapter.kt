package com.cafeapp.mycafe.frameworks.view.categorylist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.frameworks.picasso.setImage
import com.cafeapp.mycafe.frameworks.view.categoryadd.getColorInt
import com.less.repository.db.room.CategoryEntity
import kotlinx.android.synthetic.main.category_view_holder.view.*

// пердаем адаптеру лямду getIdFunc, которая отрабатывает в CategoryListFragment при нажатии на категорию
class CategoryListRVAdapter(val getIdFunc: (Long) -> Unit) :
    RecyclerView.Adapter<CategoryListRVAdapter.CategoryViewHolder>() {
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
        data?.get(position)?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = data!!.size

    class CategoryViewHolder(itemView: View, val getIdFunc: (Long) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(data: CategoryEntity) = with(itemView) {
            name.text = data.name

            setBackgroundColor(data.getColorInt(context))
            
            setOnClickListener {
                getIdFunc(data.id)
            }

            if (data.imagepath.isNotEmpty())
                setImage(data.imagepath, image)
        }
    }
}