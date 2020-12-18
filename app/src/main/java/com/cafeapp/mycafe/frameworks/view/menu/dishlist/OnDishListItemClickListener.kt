package com.cafeapp.mycafe.frameworks.view.menu.dishlist

import com.less.repository.db.room.DishesEntity

interface OnDishListItemClickListener {
    fun onChangeStopListStateForDish(dish: DishesEntity, isInStopList: Boolean)
    fun onDishClick(dishId: Long)
    fun onRemoveDishButtonClick(dish: DishesEntity)
}