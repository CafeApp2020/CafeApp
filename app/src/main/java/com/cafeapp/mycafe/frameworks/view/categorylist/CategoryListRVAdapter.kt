package com.cafeapp.mycafe.frameworks.view.categorylist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.frameworks.picasso.setImage
import com.less.repository.db.room.CategoryEntity
import kotlinx.android.synthetic.main.category_view_holder.view.*

// пердаем адаптеру лямду getIdFunc, которая отрабатывает в CategoryListFragment при нажатии на категорию
class CategoryListRVAdapter(private val getCategoryFunc: (CategoryEntity, Int) -> Unit) :
    RecyclerView.Adapter<CategoryListRVAdapter.CategoryViewHolder>() {

    private lateinit var context: Context

    private var dataList: MutableList<CategoryEntity?>? = mutableListOf()

    fun setData(data: List<CategoryEntity?>?) {
        dataList?.clear()

        data?.map {
            dataList?.add(it)
        }

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        context = parent.context

        return CategoryViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.category_view_holder,
                parent,
                false
            ), getCategoryFunc
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        dataList?.get(position)?.let { holder.bind(it, position) }
    }

    override fun getItemCount(): Int = dataList!!.size

    inner class CategoryViewHolder(
        itemView: View,
        val getCategoryFunc: (CategoryEntity, Int) -> Unit
    ) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(data: CategoryEntity, position: Int) = with(itemView) {
            name.text = data.name

            onCategoryClickBehavior(this, data)

            onEditCategoryButtonClickBehavior(this, data)

            onRemoveCategoryButtonClickBehavior(this, data, position)

            if (data.imagepath.isNotEmpty())
                setImage(data.imagepath, image)
        }

        private fun onCategoryClickBehavior(view: View, data: CategoryEntity) {
            view.categoryViewHolderLeftSide.setOnClickListener {
                getCategoryFunc(data, it.id)
            }
        }

        private fun onEditCategoryButtonClickBehavior(view: View, data: CategoryEntity) {
            view.categoryViewHolderEditButton.setOnClickListener {
                getCategoryFunc(data, it.id)
            }
        }

        private fun onRemoveCategoryButtonClickBehavior(
            view: View,
            data: CategoryEntity,
            position: Int
        ) {
            view.categoryViewHolderRemoveButton.setOnClickListener {
                showAlert(it.id, data, position)
            }
        }

        private fun showAlert(buttonId: Int, data: CategoryEntity, position: Int) {
            val builder = AlertDialog.Builder(context)

            builder.setMessage(context.getString(R.string.want_to_delete_category))
                .setPositiveButton(context.getString(R.string.delete)) { _, _ ->
                    removeCategory(data, position)
                    getCategoryFunc(data, buttonId)
                }
                .setNegativeButton(context.getString(R.string.cancel)) { dialog, _ ->
                    dialog.dismiss()
                }
            builder.create().show()
        }

        private fun removeCategory(data: CategoryEntity, position: Int) {
            dataList?.remove(data)
            notifyItemRemoved(position)
        }
    }
}