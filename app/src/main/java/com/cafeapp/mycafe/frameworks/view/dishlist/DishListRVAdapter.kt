package com.cafeapp.mycafe.frameworks.view.dishlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.frameworks.picasso.setImage
import com.less.repository.db.room.DishesEntity
import kotlinx.android.synthetic.main.dish_view_holder.view.*

class DishListRVAdapter(private val getDishFunc: (DishesEntity, Int) -> Unit) :
    RecyclerView.Adapter<DishListRVAdapter.ViewHolder>() {
    private var currentData: MutableList<DishesEntity?>? = mutableListOf()
    private lateinit var context: Context
    var data: List<DishesEntity?>? = mutableListOf()
        set(value) {
            field = value
            for(dish in data!!) dish?.let{ if(!it.deleted) currentData?.add(it) }
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.dish_view_holder, parent, false
            ), getDishFunc)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        currentData?.get(position).let { holder.bind(it!!, position) }
    }

    override fun getItemCount(): Int = currentData!!.size

    inner class ViewHolder(itemView: View, val getDishFunc: (DishesEntity, Int) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(data: DishesEntity, position: Int) = with(itemView) {
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
                getDishFunc(data, it.id)
            }

            dishViewHolderAddStopButton.setOnClickListener {
                getDishFunc(data, it.id)
                dishViewHolderStopImageView.visibility = View.VISIBLE
                dishViewHolderRemoveStopButton.visibility = View.VISIBLE
                dishViewHolderAddStopButton.visibility = View.GONE
            }

            dishViewHolderRemoveStopButton.setOnClickListener {
                getDishFunc(data, it.id)
                dishViewHolderStopImageView.visibility = View.GONE
                dishViewHolderRemoveStopButton.visibility = View.GONE
                dishViewHolderAddStopButton.visibility = View.VISIBLE
            }

            dishViewHolderDeleteButton.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setMessage(context.getString(R.string.want_to_delete_dish))
                    .setPositiveButton(context.getString(R.string.delete)) { _, _ ->
                        currentData?.remove(data)
                        getDishFunc(data, it.id)
                        notifyItemRemoved(position)
                    }
                    .setNegativeButton(context.getString(R.string.cancel)){ dialog, _ ->
                        dialog.dismiss()
                    }
                builder.create().show()
            }
        }
    }
}