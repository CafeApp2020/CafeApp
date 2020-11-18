package com.cafeapp.mycafe.frameworks.view.categoryadd

import android.content.Context
import androidx.core.content.ContextCompat
import com.cafeapp.mycafe.R
import com.less.repository.db.room.CategoryEntity

fun CategoryEntity.getColorInt(context: Context): Int = ContextCompat.getColor(
    context, getColorRes()
)

fun CategoryEntity.getColorRes(): Int = when (this.color) {
    1 -> R.color.yellow
    2 -> R.color.green
    3 -> R.color.pink
    4 -> R.color.red
    5 -> R.color.blue
    5 -> R.color.violet
    else -> R.color.white
}