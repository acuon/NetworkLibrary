package com.acuon.networklibrary.utils.extensions

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.acuon.networklibrary.utils.GridLayoutManagerWrapper

fun RecyclerView.gridView(context: Context, spanCount: Int) {
    layoutManager = GridLayoutManagerWrapper(context, spanCount)
}

fun RecyclerView.addDecoration(decorator: RecyclerView.ItemDecoration) {
    if (itemDecorationCount == 0) addItemDecoration(decorator)
}

fun createDecorator(value: Int): RecyclerView.ItemDecoration {
    return createDecorator(value, value, value, value)
}

fun createDecorator(top: Int, bottom: Int, left: Int, right: Int): RecyclerView.ItemDecoration {
    return object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            with(outRect) {
                this.top = top.dp
                this.bottom = bottom.dp
                this.left = left.dp
                this.right = right.dp
            }
        }
    }
}

fun createGridDecorator(
    value: Int,
    spanCount: Int,
    includeEdge: Boolean
): RecyclerView.ItemDecoration {
    return createGridDecorator(value, value, value, value, spanCount, includeEdge)
}

fun createGridDecorator(
    top: Int,
    bottom: Int,
    left: Int,
    right: Int,
    spanCount: Int,
    includeEdge: Boolean
): RecyclerView.ItemDecoration {
    return object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view) // item position
            val column = position % spanCount // item column

            if (includeEdge) {
                outRect.left =
                    left - column * left / spanCount // spacing - column * ((1f / spanCount) * spacing)
                outRect.right =
                    (column + 1) * right / spanCount // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = top
                }
                outRect.bottom = bottom // item bottom
            } else {
                outRect.left = column * left / spanCount // column * ((1f / spanCount) * spacing)
                outRect.right =
                    right - (column + 1) * right / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = top // item top
                }
            }
        }
    }
}
