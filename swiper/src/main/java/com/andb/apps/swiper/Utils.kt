package com.andb.apps.swiper

import android.content.res.Resources

internal fun dpToPx(dp: Int): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return (dp * scale).toInt()
}
