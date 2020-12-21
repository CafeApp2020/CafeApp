package com.cafeapp.mycafe.frameworks.view.menu.dishlist

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

class DishListRVAdapter(private val listener: OnDishListItemClickListener) :
    RecyclerView.Adapter<DishListRVAdapter.ViewHolder>() {
    private var dishList = mutableListOf<DishesEntity?>()
    private lateinit var context: Context
    private var selectedDishListForOrder  = mutableListOf<Long>()

    var DishesEntity.isChecked : Boolean   // блюдо выделено для добавления к заказу
        get() = selectedDishListForOrder.contains(this?.id)
        set(value) {}

    fun setDishList(dishList: List<DishesEntity?>) {
        updateData(dishList)
    }

    fun updateSelectedDishList(selectedDishListForOrder:MutableList<Long>) {
        this.selectedDishListForOrder=selectedDishListForOrder
        notifyDataSetChanged()
    }

    fun updateData(updateDishList: List<DishesEntity?>) {
        this.dishList.clear()
        this.dishList.addAll(updateDishList!!)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.dish_view_holder, parent, false
            ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        dishList.get(position).let { holder.bind(it!!, position) }
    }

    override fun getItemCount(): Int = dishList.size

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        lateinit var dish: DishesEntity

        fun bind(dish: DishesEntity, position: Int) {
            this.dish = dish

            setDishName()
            setDishPrice()
            setDishWeight()
            setDishImage()
            showDishInStopList()
            showCheckedDish()

            onDishClickBehavior()
            onAddInStopListButtonClickBehavior()
            onRemoveFromStopListButtonClickBehavior()
            onRemoveDishClickBehavior(position)
        }

        private fun onDishClickBehavior() = with(itemView) {
            dish_view_holder_leftSide.setOnClickListener {
                val dishId = dish.id
                listener.onDishClick(dishId)
            }
        }

        private fun onAddInStopListButtonClickBehavior() = with(itemView) {
            dish_view_holder_addInStopList_IB.setOnClickListener {
                showStopListButtons()
                listener.onChangeStopListStateForDish(dish, true)
            }
        }

        private fun onRemoveFromStopListButtonClickBehavior() = with(itemView) {
            dish_view_holder_removeFromStopList_IB.setOnClickListener {
                hideStopListButtons()
                listener.onChangeStopListStateForDish(dish, false)
            }
        }

        private fun onRemoveDishClickBehavior(position: Int) = with(itemView) {
            dish_view_holder_removeDish_IB.setOnClickListener {
                showAlert(position)
            }
        }

        private fun showAlert(position: Int) {
            val builder = AlertDialog.Builder(context)
            builder.setMessage(context.getString(R.string.want_to_delete_dish))
                .setPositiveButton(context.getString(R.string.delete)) { _, _ ->
                    listener.onRemoveDishButtonClick(dish)
                    notifyItemRemoved(position)
                }
                .setNegativeButton(context.getString(R.string.cancel)) { dialog, _ ->
                    dialog.dismiss()
                }
            builder.create().show()
        }

        private fun setDishName() = with(itemView) {
            dish_name_textview.text = dish.name
        }

        private fun setDishPrice() = with(itemView) {
            val price: String = dish.price.toString() + " ₽"
            dish_price_textview.text = price
        }

        private fun setDishWeight() = with(itemView) {
            var weight = dish.weight?.toInt().toString()

            if (weight.isNotEmpty()) {
                weight = "$weight гр"
                dish_weight_textview.text = weight
            }
        }

        private fun setDishImage() = with(itemView) {
            val imagePath = dish.imagepath.toString()

            if (imagePath.isNotEmpty()) {
                setImage(imagePath, dish_image_imageview)
            }
        }

        private fun showDishInStopList() {
            if (dish.in_stop_list)
                showStopListButtons()
            else
                hideStopListButtons()
        }

        private fun showCheckedDish() = with(itemView) {
            if (dish.isChecked)
             checked_IV.visibility = View.VISIBLE
            else
             checked_IV.visibility =View.INVISIBLE
        }

        private fun showStopListButtons() = with(itemView) {
            dish_view_holder_addInStopList_IB.visibility = View.INVISIBLE
            dish_view_holder_inStopList_IV.visibility = View.VISIBLE
            dish_view_holder_removeFromStopList_IB.visibility = View.VISIBLE
        }

        private fun hideStopListButtons() = with(itemView) {
            dish_view_holder_inStopList_IV.visibility = View.INVISIBLE
            dish_view_holder_removeFromStopList_IB.visibility = View.INVISIBLE
            dish_view_holder_addInStopList_IB.visibility = View.VISIBLE
        }
    }
}