package com.andb.apps.swiper

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeStep(val endX: Int, val viewTypes: List<Int>) {

    companion object {
        const val SIDE_EDGE = 7834
        const val SIDE_VIEW = 8283
    }

    /**
     * Function to determine background color of swipe based on viewHolder and x position of swipe.
     * @see SwipeStep.color to set constant value
     */
    var colorFun: (viewHolder: RecyclerView.ViewHolder, dX: Float) -> Int = { _, _ -> Color.BLACK }

    /**
     * Set a constant background color for the swipe.
     * @see SwipeStep.colorFun to set a dynamic value
     */
    fun color(color: Int) {
        colorFun = { _, _ -> color }
    }

    /**
     * Function to determine icon of swipe based on viewHolder and x position of swipe.
     * @see SwipeStep.icon to set constant value
     */
    var iconFun: (viewHolder: RecyclerView.ViewHolder, dX: Float) -> Drawable? = { _, _ -> null }

    /**
     * Set a constant icon for the swipe.
     * @see SwipeStep.iconFun to set a dynamic value
     */
    fun icon(drawable: Drawable) {
        iconFun = { _, _ -> drawable }
    }

    /**
     * Function to determine color for icon of swipe based on viewHolder and x position of swipe.
     * @see SwipeStep.iconColor to set constant value
     */
    var iconColorFun: (viewHolder: RecyclerView.ViewHolder, dX: Float) -> Int =
        { _, _ -> Color.WHITE }

    /**
     * Set a constant color for icon of swipe.
     * @see SwipeStep.iconColorFun to set a dynamic value
     */
    fun iconColor(color: Int) {
        iconColorFun = { _, _ -> color }
    }

    /**
     * Action when swipe is completed at this step
     */
    var action: ((RecyclerView.ViewHolder) -> Unit)? = null

    /**
     * Set action for when swipe is completed at this step
     */
    fun action(block: (viewHolder: RecyclerView.ViewHolder) -> Unit) {
        action = block
    }

    /**
     * Set padding from whichever side is chosen in px. Defaults to 16dp (converted to px)
     */
    var marginSide = dpToPx(16)

    /**
     * Set side that icon is aligned to. SIDE_EDGE is the edge of the screen (static positioning) and SIDE_VIEW is the edge of the view that is being swiped (moves along with view).
     * @see SIDE_EDGE
     * @see SIDE_VIEW
     */
    var side = SIDE_EDGE

    private val iconPositioning: (RecyclerView.ViewHolder, Float, Int) -> Drawable? = { vh, dX, direction ->
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


    internal fun getBoundedIcon(viewHolder: RecyclerView.ViewHolder, dX: Float, direction: Int): Drawable? {
        return iconPositioning.invoke(viewHolder, dX, direction)
    }

}