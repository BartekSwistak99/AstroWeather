package com.example.astroweather

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(var margin:Int): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if(parent.getChildAdapterPosition(view) %2 == 1)
            outRect.left = margin
        else
            outRect.right = margin
    }
}