package com.cafeapp.mycafe.frameworks.view.orderlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.frameworks.picasso.setImage
import com.cafeapp.mycafe.frameworks.room.OrdersEntity
import com.less.repository.db.room.DishesEntity
import kotlinx.android.synthetic.main.dish_view_holder.view.*
import kotlinx.android.synthetic.main.order_view_holder.view.*

class OrderListRVAdapter(val getIdFunc: (Long) -> Unit) :
    RecyclerView.Adapter<OrderListRVAdapter.ViewHolder>() {
    var data: List<OrdersEntity?>? = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.order_view_holder, parent, false
        ), getIdFunc
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        data?.get(position).let { holder.bind(it!!) }
    }

    override fun getItemCount(): Int = data!!.size

    inner class ViewHolder(itemView: View, val getIdFunc: (Long) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(data: OrdersEntity) = with(itemView) {
            customerNameTW.text=data.customername
            customerTelNumberTW.text=data.customerphone
         }
    }
}