package ru.skillbranch.gameofthrones.ui.custom

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView

class ItemDivider : RecyclerView.ItemDecoration() {
    companion object {
        private val DEVIDER_COLOR = Color.parseColor("#E1E1E1")
    }

    private val _paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = DEVIDER_COLOR
        strokeWidth = 0f
    }

    override fun onDraw(c: Canvas, parent: RecyclerView) {
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            72F,
            parent.context.resources.displayMetrics
        )
        val left = px
        val right = parent.right.toFloat()
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val bottom = child.bottom.toFloat()
            c.drawLine(left, bottom, right, bottom, _paint)
        }
    }
}