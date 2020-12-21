package com.cafeapp.mycafe.frameworks.view.orders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.entities.OrderDishEntityModify
import kotlinx.android.synthetic.main.order_dish_view_holder.view.*

class OrdersDishListRVAdapter(val listener: OnOrderDishListener) :
    RecyclerView.Adapter<OrdersDishListRVAdapter.ViewHolder>() {
    var data: List<OrderDishEntityModify?>? = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun setDishList(updateDishList: List<OrderDishEntityModify?>) {
        this.data = updateDishList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.order_dish_view_holder, parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        data?.get(position).let { holder.bind(it!!) }
    }

    override fun getItemCount(): Int = data!!.size

    inner class ViewHolder(itemView: View) :   RecyclerView.ViewHolder(itemView) {
        lateinit var orderDishEntityModify: OrderDishEntityModify

        fun bind(data: OrderDishEntityModify) = with(itemView) {
            orderDishEntityModify=data
            setData(data)
            onDishCountChangeClick()
        }

        private fun onDishCountChangeClick() = with(itemView) {
            incDishCount.setOnClickListener {dishCountChange(orderDishEntityModify.dishCount++)}
            decDishCount.setOnClickListener {dishCountChange(orderDishEntityModify.dishCount--)}
        }

        private fun dishCountChange(count:Int) {
            itemView.dishCountTV.text= orderDishEntityModify.dishCount.toString()
            listener.onDishCountChangeButtonClick(orderDishEntityModify)
        }

        private fun setData(data: OrderDishEntityModify) = with(itemView) {
            dishNameTW.text = data.dishName
            priceTW.text = data.dishPrice.toString() + " ₽"
            dishCountTV.text = data.dishCount.toString()
        }
    }
}