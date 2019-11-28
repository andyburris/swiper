package com.andb.apps.swiper

import android.content.res.Resources

fun dpToPx(dp: Int): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return (dp * scale).toInt()
}
