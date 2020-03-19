package com.andb.apps.swiper

import android.content.res.Resources
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class SwipeDirection(val multiplier: Int) {
    val steps = mutableListOf<SwipeStep>()

    /**Set the view to either be swiped across the screen when an action is run or to return to its original position**/
    var fullySwipeable: (Int) -> Boolean = { true }

    /**Length (in dp) a swipe needs to travel to trigger action**/
    var threshold = dpToPx(16)
        set(value) {
            field = dpToPx(value)
        }
    var friction = 1f

    /**Add a step within the swipe, going from whatever the next lowest step position is until the position (in dp) of this**/
    fun step(absoluteDp: Int, vararg viewTypes: Int, block: SwipeStep.() -> Unit) {
        val step = SwipeStep(dpToPx(absoluteDp), viewTypes.toList())
        step.apply(block)
        steps.add(step)
    }

    /**Add a step within the swipe, going from the highest step position is until the end of the swipe**/
    fun endStep(vararg viewTypes: Int, block: SwipeStep.() -> Unit) {
        val step = SwipeStep(Resources.getSystem().displayMetrics.widthPixels, viewTypes.toList())
        step.apply(block)
        steps.add(step)
    }

    internal fun getStepBy(dX: Float, viewHolder: RecyclerView.ViewHolder): SwipeStep? {
        return steps
            .filter { step -> abs(dX) <= step.endX }
            .filter { step -> step.viewTypes.isEmpty() || viewHolder.itemViewType in step.viewTypes }
            .minBy { it.endX }
    }

    internal fun endX(viewHolder: RecyclerView.ViewHolder): Float{
        return steps
            .filter { step -> step.viewTypes.isEmpty() || viewHolder.itemViewType in step.viewTypes }
            .maxBy { it.endX }
            ?.endX
            ?.toFloat() ?: 0f
    }

    companion object {
        const val MULTIPLIER_RIGHT_TO_LEFT = -1
        const val MULTIPLIER_LEFT_TO_RIGHT = 1
    }
}