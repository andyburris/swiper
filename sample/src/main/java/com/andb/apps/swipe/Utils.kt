package com.andb.apps.swipe

import android.content.res.Resources
import android.graphics.Color

fun Int.withAlpha(alpha: Float): Int {
    return Color.argb((alpha * 255).toInt(), Color.red(this), Color.green(this), Color.blue(this))
}

fun Int.withSaturation(saturation: Float): Int {
    val hsv = floatArrayOf(0f, 0f, 0f)
    Color.RGBToHSV(Color.red(this), Color.green(this), Color.blue(this), hsv)
    hsv[1] = saturation
    return Color.HSVToColor(hsv)
}

fun dpToPx(dp: Int): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return (dp * scale).toInt()
}

val Int.dp
    get() = dpToPx(this)