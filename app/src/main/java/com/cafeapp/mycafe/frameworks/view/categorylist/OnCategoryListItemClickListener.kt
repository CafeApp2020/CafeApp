package com.cafeapp.mycafe.frameworks.view.categorylist

import com.less.repository.db.room.CategoryEntity

interface OnCategoryListItemClickListener {
    fun onCategoryClick(categoryId: Long)
    fun onChangeStopListStateForCategory(category: CategoryEntity, isInStopList: Boolean)
    fun onEditCategoryButtonClick(categoryId: Long)
    fun onRemoveCategoryButtonClick(category: CategoryEntity)
}