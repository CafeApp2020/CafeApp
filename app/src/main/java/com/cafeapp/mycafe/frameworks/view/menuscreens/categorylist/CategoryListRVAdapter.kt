package com.cafeapp.mycafe.frameworks.view.menuscreens.categorylist

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

class CategoryListRVAdapter(private val listener: OnCategoryListItemClickListener) :
    RecyclerView.Adapter<CategoryListRVAdapter.CategoryViewHolder>() {
    private var categoryList = mutableListOf<CategoryEntity?>()
    private lateinit var context: Context

    fun setCategoryList(categoryList: List<CategoryEntity?>?) {
        this.categoryList.clear()  // break point
        this.categoryList.addAll(categoryList!!) // см.какие данные приходят, актуальные или нет
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        context = parent.context

        return CategoryViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.category_view_holder,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        categoryList.get(position)?.let { holder.bind(it, position) }
    }

    override fun getItemCount(): Int = categoryList.size

    inner class CategoryViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(category: CategoryEntity, position: Int) = with(itemView) {
            setCategoryName(category)
            showCategoryInStopList(category)
            setImage(category)

            onAddInStopListButtonClickBehavior(category)
            onCategoryClickBehavior(category)
            onEditCategoryButtonClickBehavior(category)
            onRemoveCategoryButtonClickBehavior(category, position)
            onRemoveFromStopListButtonClickBehavior(category)
        }

        private fun onAddInStopListButtonClickBehavior(category: CategoryEntity) {
            itemView.category_view_holder_addInStopList_IB.setOnClickListener {
                showStopListButtons()
                listener.onChangeStopListStateForCategory(category, true)
            }
        }

        private fun onCategoryClickBehavior(category: CategoryEntity) {
            itemView.category_view_holder_leftSide.setOnClickListener {
                listener.onCategoryClick(category.id)
            }
        }

        private fun onEditCategoryButtonClickBehavior(category: CategoryEntity) {
            itemView.category_view_holder_edit_IB.setOnClickListener {
                val categoryId = category.id
                listener.onEditCategoryButtonClick(categoryId)
            }
        }

        private fun onRemoveCategoryButtonClickBehavior(category: CategoryEntity, position: Int) {
            itemView.category_view_holder_removeCategory_IB.setOnClickListener {
                showAlert(category, position)
            }
        }

        private fun onRemoveFromStopListButtonClickBehavior(category: CategoryEntity) {
            itemView.category_view_holder_removeFromStopList_IB.setOnClickListener {
                hideStopListButtons()
                listener.onChangeStopListStateForCategory(category, false)
            }
        }

        private fun removeCategoryFromList(category: CategoryEntity, position: Int) {
            categoryList.remove(category)
            notifyItemRemoved(position)
        }

        private fun setCategoryName(category: CategoryEntity) {
            itemView.name.text = category.name
        }

        private fun setImage(category: CategoryEntity) {
            if (category.imagepath.isNotEmpty())
                setImage(category.imagepath, itemView.category_view_holder_image_IV)
        }

        private fun showAlert(data: CategoryEntity, position: Int) {
            val builder = AlertDialog.Builder(context)

            builder.setMessage(context.getString(R.string.want_to_delete_category))
                .setPositiveButton(context.getString(R.string.delete)) { _, _ ->
                    removeCategoryFromList(data, position)
                    listener.onRemoveCategoryButtonClick(data)
                }
                .setNegativeButton(context.getString(R.string.cancel)) { dialog, _ ->
                    dialog.dismiss()
                }

            builder.create().show()
        }

        private fun showCategoryInStopList(data: CategoryEntity) {
            if (data.isInStopList) {
                showStopListButtons()
            }
        }

        private fun showStopListButtons() = with(itemView) {
            category_view_holder_addInStopList_IB.visibility = View.INVISIBLE
            category_view_holder_inStopList_IV.visibility = View.VISIBLE
            category_view_holder_removeFromStopList_IB.visibility = View.VISIBLE
        }

        private fun hideStopListButtons() = with(itemView) {
            category_view_holder_inStopList_IV.visibility = View.INVISIBLE
            category_view_holder_removeFromStopList_IB.visibility = View.INVISIBLE
            category_view_holder_addInStopList_IB.visibility = View.VISIBLE
        }
    }
}