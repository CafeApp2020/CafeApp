package com.cafeapp.mycafe.frameworks.view.orders.orderList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.frameworks.room.OrdersEntity
import com.cafeapp.mycafe.frameworks.view.delivery.OrderType
import kotlinx.android.synthetic.main.order_view_holder.view.*

class OrderListRVAdapter(private val getFunc: (OrdersEntity) -> Unit) :
    RecyclerView.Adapter<OrderListRVAdapter.ViewHolder>() {
    var data: List<OrdersEntity?>? = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.order_view_holder, parent, false
        ), getFunc
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        data?.get(position).let { holder.bind(it!!) }
    }

    override fun getItemCount(): Int = data!!.size

    inner class ViewHolder(itemView: View, val getFunc: (OrdersEntity) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private var order: OrdersEntity? = null

        fun bind(data: OrdersEntity) = with(itemView) {
            order = data

            initOrder()

            itemView.setOnClickListener {
                getFunc(data)
            }
        }

        private fun initOrder() {
            when (order?.ordertype) {
                OrderType.DELIVERY -> {
                    deliveryOrder()
                }

                OrderType.TAKEAWAY -> {
                    takeAwayOrder()
                }

                OrderType.INROOM -> {
                    tableOrder()
                }
            }
        }

        private fun deliveryOrder() = with(itemView) {
            orderTypeTW.text = context.getString(R.string.order_type_delivery)
            orderAddressTW.text = order?.customeraddress
            setOrder()
        }

        private fun takeAwayOrder() = with(itemView) {
            orderTypeTW.text = context.getString(R.string.order_type_takeaway)
            setOrder()
        }

        private fun tableOrder() = with(itemView) {
            orderTypeTW.text =
                context.getString(R.string.order_type_table, order?.tableid.toString())
            setOrder()
        }

        private fun setOrder() = with(itemView) {
            customerNameTW.text = order?.customername
            orderTimeTW.text =
                context.getString(R.string.order_date_time, order?.orderdatetime.toString())

            orderCostTW.text =
                context.getString(R.string.order_cost, order?.orderdatetime.toString())

            orderPayedTW.text = if (order!!.paided)
                context.getString(R.string.payed)
            else context.getString(
                R.string.not_payed)
        }
    }
}