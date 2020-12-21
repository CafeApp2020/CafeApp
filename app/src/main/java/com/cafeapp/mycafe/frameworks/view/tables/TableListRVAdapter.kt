package com.cafeapp.mycafe.frameworks.view.tables

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.frameworks.room.TableEntity
import kotlinx.android.synthetic.main.dish_view_holder.view.*
import kotlinx.android.synthetic.main.table_item.view.*

class TableListRVAdapter(private val onTableListItemClickListener: OnTableListItemClickListener) :
    RecyclerView.Adapter<TableListRVAdapter.TableViewHolder>() {
    private var tableList = mutableListOf<TableEntity?>()
    private lateinit var context: Context

    fun setTableList(tableList: List<TableEntity?>?) {
        this.tableList.clear()
        this.tableList.addAll(tableList!!)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
        context = parent.context

        return TableViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.table_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {
        tableList.get(position)?.let { holder.bind(it, position) }
    }

    override fun getItemCount(): Int = tableList.size

    inner class TableViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        lateinit var tableEntity:TableEntity

        fun bind(table: TableEntity, position: Int) = with(itemView) {
            itemView.tableNameTW.text=table.tablename
            itemView.removeTableIB.setOnClickListener {
                onTableListItemClickListener.onRemoveTableButtonClick(table)
                notifyItemRemoved(position)
            }
            itemView.editTableIB.setOnClickListener {
                onTableListItemClickListener.onEditTableButtonClick(table)
            }
        }
    }
}