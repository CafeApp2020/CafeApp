package com.cafeapp.mycafe.frameworks.view.dishlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.frameworks.picasso.setImage
import com.less.repository.db.room.DishesEntity
import kotlinx.android.synthetic.main.dish_view_holder.view.*

class DishListRVAdapter(val getIdFunc: (Long) -> Unit) :
    RecyclerView.Adapter<DishListRVAdapter.ViewHolder>() {
    var data: List<DishesEntity?>? = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.dish_view_holder, parent, false
        ), getIdFunc
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        data?.get(position).let { holder.bind(it!!) }
    }

    override fun getItemCount(): Int = data!!.size

    inner class ViewHolder(itemView: View, val getIdFunc: (Long) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(data: DishesEntity) = with(itemView) {
            var price: String = data.price.toString() + " ₽"
            dish_name_textview.text = data.name
            dish_price_textview.text = price

            var weight = data.weight.toString()
            val imagePath = data.imagepath.toString()

            if (weight.isNotEmpty()) {
                weight = "$weight гр"
                dish_weight_textview.text = weight
            }

            if (imagePath.isNotEmpty()) {
                setImage(imagePath, dish_image_imageview)
            }

            dishViewHolderLeftSide.setOnClickListener {
                getIdFunc(data.id)
            }
        }
    }
}