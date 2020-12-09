package com.cafeapp.mycafe.frameworks.view.dishlist

import com.less.repository.db.room.DishesEntity

interface OnDishListItemClickListener {
    fun onChangeStopListStateForDish(dish: DishesEntity, isInStopList: Boolean)
    fun onDishClick(dishId: Long)
    fun onRemoveDishButtonClick(dish: DishesEntity)
}