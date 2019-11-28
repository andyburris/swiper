package com.andb.apps.swiper

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.swipeWith(block: Swiper.() -> Unit) {
    val callback = Swiper()
    callback.apply(block)
    ItemTouchHelper(callback).attachToRecyclerView(this)
}