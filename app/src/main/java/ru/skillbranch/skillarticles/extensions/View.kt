package ru.skillbranch.skillarticles.extensions

import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop

fun View.setMarginOptionally(
    bottom: Int = marginBottom,
    top: Int = marginTop,
    left: Int = marginLeft,
    right: Int = marginRight
) {
    val p = layoutParams as ViewGroup.MarginLayoutParams
    p.bottomMargin = bottom
    p.topMargin = top
    p.leftMargin = left
    p.rightMargin = right
    this.requestLayout()
}