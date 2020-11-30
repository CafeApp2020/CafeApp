package com.cafeapp.mycafe.frameworks.view.dishlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.frameworks.picasso.setImage
import com.less.repository.db.room.DishesEntity
import kotlinx.android.synthetic.main.dish_view_holder.view.*

class DishListRVAdapter(val getIdFunc: (Long, Int) -> Unit) :
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

    inner class ViewHolder(itemView: View, val getIdFunc: (Long, Int) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(data: DishesEntity) = with(itemView) {
            val price: String = data.price.toString() + " ₽"
            dish_name_textview.text = data.name
            dish_price_textview.text = price
            if(data.in_stop_list){
                dishViewHolderStopImageView.visibility = View.VISIBLE
                dishViewHolderRemoveStopButton.visibility = View.VISIBLE
                dishViewHolderAddStopButton.visibility = View.GONE
            }

            var weight = data.weight?.toInt().toString()
            val imagePath = data.imagepath.toString()

            if (weight.isNotEmpty()) {
                weight = "$weight гр"
                dish_weight_textview.text = weight
            }

            if (imagePath.isNotEmpty()) {
                setImage(imagePath, dish_image_imageview)
            }

            dishViewHolderLeftSide.setOnClickListener {
                getIdFunc(data.id, it.id)
            }

            dishViewHolderAddStopButton.setOnClickListener {
                getIdFunc(data.id, it.id)
                dishViewHolderStopImageView.visibility = View.VISIBLE
                dishViewHolderRemoveStopButton.visibility = View.VISIBLE
                dishViewHolderAddStopButton.visibility = View.GONE
            }

            dishViewHolderRemoveStopButton.setOnClickListener {
                getIdFunc(data.id, it.id)
                dishViewHolderStopImageView.visibility = View.GONE
                dishViewHolderRemoveStopButton.visibility = View.GONE
                dishViewHolderAddStopButton.visibility = View.VISIBLE
            }
        }
    }
}