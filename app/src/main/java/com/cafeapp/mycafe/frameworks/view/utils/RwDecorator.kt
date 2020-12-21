package com.cafeapp.mycafe.frameworks.view.utils

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cafeapp.mycafe.R

class RecyclerViewUtil {
    companion object {
        fun addDecorator(context: Context, rw: RecyclerView) {
            val decorator = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
            decorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.items_divider)!!)
            for (i in 0 until rw.itemDecorationCount)
                rw.removeItemDecoration(rw.getItemDecorationAt(i))
            rw.addItemDecoration(decorator)
        }
    }
}