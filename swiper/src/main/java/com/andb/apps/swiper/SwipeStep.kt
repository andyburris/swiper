package com.andb.apps.swiper

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper

class SwipeStep(val endX: Int) {

    companion object {
        const val SIDE_EDGE = 7834
        const val SIDE_VIEW = 8283
    }

    var colorFun: (dX: Float) -> Int = { Color.TRANSPARENT }
    var color: Int = Color.TRANSPARENT
        set(value) {
            field = value
            colorFun = { value }
        }

    fun color(value: (Float) -> Int) {
        colorFun = value
    }

    var icon: Drawable? = null
    var iconColor = Color.WHITE
    var action: ((Int) -> Unit)? = null
    var marginSide = dpToPx(16)
    var side = SIDE_EDGE
    var iconPositioning: (View, Float, Int) -> Drawable? = { itemView, dX, direction ->
        icon?.mutate()?.also { icon ->
            icon.setColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP)
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


    fun getBoundedIcon(itemView: View, dX: Float, direction: Int): Drawable? {
        return iconPositioning.invoke(itemView, dX, direction)
    }
}