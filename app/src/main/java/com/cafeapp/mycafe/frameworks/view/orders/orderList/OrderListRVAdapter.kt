package com.cafeapp.mycafe.frameworks.view.orders.orderList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.frameworks.room.OrdersEntity
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
            deliveryOrder(data)
         }

        fun deliveryOrder(data: OrdersEntity) = with(itemView) {
            orderTypeTW.text = context.getString(R.string.order_type_delivery)
            orderAddressTW.text = data.customeraddress
            setOrder(data)
        }

        fun takeAwayOrder(data: OrdersEntity) = with(itemView) {
            orderTypeTW.text = context.getString(R.string.order_type_takeaway)
            setOrder(data)
        }

        fun tableOrder(data: OrdersEntity) = with(itemView) {
            orderTypeTW.text = context.getString(R.string.order_type_table, data.tableid.toString())
            setOrder(data);
        }

        fun setOrder(data: OrdersEntity) = with(itemView) {
            customerNameTW.text = data.customername
            orderTimeTW.text = context.getString(R.string.order_date_time, data.orderdatetime.toString())
            orderCostTW.text = context.getString(R.string.order_cost, data.orderdatetime.toString())
            orderPayedTW.text = if(data.paided)
                context.getString(R.string.payed)
            else context.getString(
                R.string.not_payed)
        }
    }
}