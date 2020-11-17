package com.cafeapp.mycafe.frameworks.view.categorylist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.frameworks.picasso.setImage
import com.less.repository.db.room.CategoryEntity
import kotlinx.android.synthetic.main.category_view_holder.view.*

class CategoryListRVAdapter() : RecyclerView.Adapter<CategoryListRVAdapter.CategoryViewHolder>() {

    var data : List<CategoryEntity?>? = mutableListOf()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CategoryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                    R.layout.category_view_holder,
                    parent,
                    false
            ))

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        data?.get(position)?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = data!!.size

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(data : CategoryEntity) = with(itemView){
            name.text = data.name
            setImage(data.imagepath, image)
        }
    }
}