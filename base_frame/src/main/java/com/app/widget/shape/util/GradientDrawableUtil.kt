package com.app.widget.shape.util

import android.graphics.drawable.GradientDrawable

class GradientDrawableUtil {
    /**
     * @param radius      四个角的半径
     * @param colors      渐变的颜色
     * @param strokeWidth 边框宽度
     * @param strokeColor 边框颜色
     * @return
     */
    fun getNeedDrawable(
        radius: FloatArray?,
        colors: IntArray?,
        strokeWidth: Int,
        strokeColor: Int,
    ): GradientDrawable {
        val drawable = GradientDrawable()
        drawable.orientation = GradientDrawable.Orientation.LEFT_RIGHT
        drawable.cornerRadii = radius
        drawable.colors = colors
        drawable.setStroke(strokeWidth, strokeColor)
        drawable.gradientType = GradientDrawable.LINEAR_GRADIENT
        return drawable
    }

    /**
     * @param radius      四个角的半径
     * @param bgColor     背景颜色
     * @param strokeWidth 边框宽度
     * @param strokeColor 边框颜色
     * @return
     */
    fun getNeedDrawable(
        radius: FloatArray?,
        bgColor: Int,
        strokeWidth: Int,
        strokeColor: Int,
    ): GradientDrawable {
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.RECTANGLE
        drawable.cornerRadii = radius
        drawable.setStroke(strokeWidth, strokeColor)
        drawable.setColor(bgColor)
        return drawable
    }

    /**
     * @param radius      四个角的半径
     * @param bgColor     背景颜色
     * @param strokeWidth 边框宽度
     * @param strokeColor 边框颜色
     * @param dashWidth   虚线边框宽度
     * @param dashGap     虚线边框间隙
     * @return
     */
    fun getNeedDrawable(
        radius: FloatArray?,
        bgColor: Int,
        strokeWidth: Int,
        strokeColor: Int,
        dashWidth: Float,
        dashGap: Float,
    ): GradientDrawable {
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.RECTANGLE
        drawable.cornerRadii = radius
        drawable.setStroke(strokeWidth, strokeColor, dashWidth, dashGap)
        drawable.setColor(bgColor)
        return drawable
    }

    companion object {
        private var gradientDrawableUtil: GradientDrawableUtil? = null

        fun init(): GradientDrawableUtil {
            if (gradientDrawableUtil == null) {
                gradientDrawableUtil = GradientDrawableUtil()
            }
            return gradientDrawableUtil!!
        }
    }
}
