package com.andb.apps.swiper

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeStep(val endX: Int) {

    companion object {
        const val SIDE_EDGE = 7834
        const val SIDE_VIEW = 8283
    }

    var colorFun: (viewHolder: RecyclerView.ViewHolder, dX: Float) -> Int = { _, _ -> Color.BLACK }

    fun color(color: Int) {
        colorFun = { _, _ -> color }
    }

    var iconFun: (viewHolder: RecyclerView.ViewHolder, dX: Float) -> Drawable? = { _, _ -> null }
    fun icon(drawable: Drawable) {
        iconFun = { _, _ -> drawable }
    }

    var iconColorFun: (viewHolder: RecyclerView.ViewHolder, dX: Float) -> Int = { _, _ -> Color.WHITE }
    fun iconColor(color: Int){
        iconColorFun = { _, _ -> color }
    }

    var action: ((RecyclerView.ViewHolder) -> Unit)? = null
    fun action(block: (viewHolder: RecyclerView.ViewHolder) -> Unit){
        action = block
    }

    var marginSide = dpToPx(16)
    var side = SIDE_EDGE

    var iconPositioning: (RecyclerView.ViewHolder, Float, Int) -> Drawable? = { vh, dX, direction ->
        val itemView = vh.itemView
        iconFun.invoke(vh, dX)?.mutate()?.also { icon ->
            icon.setColorFilter(iconColorFun.invoke(vh, dX), PorterDuff.Mode.SRC_ATOP)
            val top = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
            val bottom = top + icon.intrinsicHeight
            when (direction) {
                ItemTouchHelper.RIGHT -> {
                    when (side) {
                        SIDE_EDGE -> {
                            val left = itemView.left + marginSide
                            icon.setBounds(left, top, left + icon.intrinsicWidth, bottom)
                        }
                        else -> {
                            val right = (itemView.left + dX - marginSide).toInt()
                            icon.setBounds(right - icon.intrinsicWidth, top, right, bottom)
                        }
                    }
                }
                else -> {
                    when (side) {
                        SIDE_EDGE -> {
                            val right = itemView.right - marginSide
                            icon.setBounds(right - icon.intrinsicWidth, top, right, bottom)
                        }
                        else -> {
                            val left = (itemView.right - dX + marginSide).toInt()
                            icon.setBounds(left, top, left + icon.intrinsicWidth, bottom)
                        }
                    }
                }
            }
        }
    }


    fun getBoundedIcon(viewHolder: RecyclerView.ViewHolder, dX: Float, direction: Int): Drawable? {
        return iconPositioning.invoke(viewHolder, dX, direction)
    }

}