package com.andb.apps.swiper

import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.min

class Swiper : ItemTouchHelper.Callback() {
    var swipeX = 0f
    var swipeThreshold: (RecyclerView.ViewHolder) -> Float = { vh -> vh.itemView.width.toFloat() + 1f }

    private val rightToLeft = SwipeDirection(SwipeDirection.MULTIPLIER_RIGHT_TO_LEFT)
    private val leftToRight = SwipeDirection(SwipeDirection.MULTIPLIER_LEFT_TO_RIGHT)

    /** Create steps for right to left swipes **/
    fun rightToLeft(block: SwipeDirection.() -> Unit) {
        rightToLeft.apply(block)
        rightToLeft.list.sortBy { it.endX }
    }

    /** Create steps for left to right swipes **/
    fun leftToRight(block: SwipeDirection.() -> Unit) {
        leftToRight.apply(block)
        leftToRight.list.sortBy { it.endX }
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) =
        false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        //TODO: parametrize swipe vs return
        (viewHolder.itemView.parent as RecyclerView).adapter?.notifyItemChanged(viewHolder.adapterPosition)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        val direction = if (swipeX > 0) ItemTouchHelper.RIGHT else ItemTouchHelper.LEFT
        when (direction) {
            ItemTouchHelper.LEFT -> {
                Log.d("ith", "clearView direction: left, swipeX: $swipeX > threshold: ${rightToLeft.threshold}?")
                if (abs(swipeX) >= rightToLeft.threshold) {
                    Log.d("ith", "swipe triggered left, swipeX: $swipeX, options: ${rightToLeft.list.map { it.endX }} ")
                    val currentStep = rightToLeft.list.filter { abs(swipeX) <= it.endX }.minBy { it.endX }
                    currentStep?.action?.invoke(viewHolder)
                }
            }
            ItemTouchHelper.RIGHT -> {
                Log.d("ith", "clearView direction: right")
                if (swipeX >= leftToRight.threshold) {
                    val currentStep = leftToRight.list.filter { swipeX <= it.endX }.minBy { it.endX }
                    currentStep?.action?.invoke(viewHolder)
                }
            }
        }

        super.clearView(recyclerView, viewHolder)
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return defaultValue * 400
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dXIn: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

        if (dXIn == 0f) return

        val direction = if (dXIn > 0) leftToRight else rightToLeft

        //get friction-adjusted x-value if not bigger than max for this direction
        val dXNew = min(
            abs(dXIn * direction.friction),
            direction.list.map { it.endX }.maxBy { it }?.toFloat() ?: 0f
        )

        val step = direction.list.filter { dXNew <= it.endX }.minBy { it.endX }
            ?: throw Exception("If this line is called, dXNew should be below or equal for minBy (dXNew = $dXNew, steps are ${direction.list.map { it.endX }})")

        createBackgroundDrawable(step.colorFun(viewHolder, dXNew), viewHolder.itemView).draw(c)

        step.getBoundedIcon(viewHolder, dXNew, if(dXIn > 0) ItemTouchHelper.RIGHT else ItemTouchHelper.LEFT)?.draw(c)

        if (isCurrentlyActive) {
            swipeX = dXNew * direction.multiplier
        }
        //Log.d("ith", "swipeX: $swipeX")

        super.onChildDraw(c, recyclerView, viewHolder, dXNew * direction.multiplier, dY, actionState, isCurrentlyActive)
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return swipeThreshold(viewHolder)
    }

    private fun createBackgroundDrawable(color: Int, itemView: View): GradientDrawable {
        val background = GradientDrawable()
        background.setColor(color)
        itemView.apply {
            background.setBounds(left, top, right, bottom)
        }
        return background
    }

    //defines the enabled move directions in each state (idle, swiping, dragging).
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return makeFlag(
            ItemTouchHelper.ACTION_STATE_SWIPE,
            getDirectionFlags()
        )
    }

    private fun getDirectionFlags(): Int {
        val left = rightToLeft.list.isNotEmpty()
        val right = leftToRight.list.isNotEmpty()
        return when {
            left && right -> ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            left -> ItemTouchHelper.LEFT
            right -> ItemTouchHelper.RIGHT
            else -> 0
        }
    }
}