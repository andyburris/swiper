package com.andb.apps.swiper

import android.content.res.Resources

class SwipeDirection(val multiplier: Int) {
    val list = mutableListOf<SwipeStep>()

    /**Set the view to either be swiped across the screen when an action is run or to return to its original position**/
    var fullySwipeable: (Int) -> Boolean = { true }

    /**Length (in dp) a swipe needs to travel to trigger the action**/
    var threshold = dpToPx(16)
        set(value) {
            field = dpToPx(value)
        }
    var friction = 1f

    /**Add a step within the swipe, going from whatever the next lowest step position is until the position (in dp) of this**/
    fun step(absoluteDp: Int, block: SwipeStep.() -> Unit) {
        val step = SwipeStep(dpToPx(absoluteDp))
        step.apply(block)
        list.add(step)
    }

    /**Add a step within the swipe, going from the highest step position is until the end of the swipe**/
    fun endStep(block: SwipeStep.() -> Unit) {
        val step = SwipeStep(Resources.getSystem().displayMetrics.widthPixels)
        step.apply(block)
        list.add(step)
    }

    companion object {
        const val MULTIPLIER_RIGHT_TO_LEFT = -1
        const val MULTIPLIER_LEFT_TO_RIGHT = 1
    }
}