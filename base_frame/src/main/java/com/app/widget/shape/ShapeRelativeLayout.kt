package com.app.widget.shape

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.app.frame.R
import com.app.widget.shape.util.GradientDrawableUtil

class ShapeRelativeLayout
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
    ) : RelativeLayout(context, attrs, defStyleAttr) {
        // 自定背景边框Drawable
        private var gradientDrawable: GradientDrawable? = null

        // 填充色
        private var solidColor = 0

        // 边框色
        private var strokeColor = 0

        // 边框宽度
        private var strokeWidth = 0

        // 四个角的弧度
        private var radius = 0
        private var topLeftRadius = 0
        private var topRightRadius = 0
        private var bottomLeftRadius = 0
        private var bottomRightRadius = 0

        init {
            init(context, attrs)
            // 默认背景
            setCustomBackground()
        }

        /**
         * 初始化参数
         *
         * @param context
         * @param attrs
         */
        @SuppressLint("CustomViewStyleable")
        private fun init(
            context: Context,
            attrs: AttributeSet?,
        ) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.ShapeView)
            // 填充以及边框
            solidColor = ta.getColor(R.styleable.ShapeView_solidColor, Color.TRANSPARENT)
            strokeColor = ta.getColor(R.styleable.ShapeView_strokeColor, Color.TRANSPARENT)
            strokeWidth = ta.getDimensionPixelSize(R.styleable.ShapeView_strokeWidth, 0)
            // 四个角单独设置会覆盖radius设置
            radius = ta.getDimensionPixelSize(R.styleable.ShapeView_radius, 0)
            topLeftRadius = ta.getDimensionPixelSize(R.styleable.ShapeView_topLeftRadius, radius)
            topRightRadius = ta.getDimensionPixelSize(R.styleable.ShapeView_topRightRadius, radius)
            bottomLeftRadius = ta.getDimensionPixelSize(R.styleable.ShapeView_bottomLeftRadius, radius)
            bottomRightRadius =
                ta.getDimensionPixelSize(R.styleable.ShapeView_bottomRightRadius, radius)
            ta.recycle()
        }

        fun setSolidColor(solidColor: Int) {
            this.solidColor = solidColor
        }

        fun setStrokeColor(strokeColor: Int) {
            this.strokeColor = strokeColor
        }

        fun setStrokeWidth(strokeWidth: Int) {
            this.strokeWidth = strokeWidth
        }

        fun setRadius(radius: Int) {
            this.radius = radius
        }

        fun setTopLeftRadius(topLeftRadius: Int) {
            this.topLeftRadius = topLeftRadius
        }

        fun setTopRightRadius(topRightRadius: Int) {
            this.topRightRadius = topRightRadius
        }

        fun setBottomLeftRadius(bottomLeftRadius: Int) {
            this.bottomLeftRadius = bottomLeftRadius
        }

        fun setBottomRightRadius(bottomRightRadius: Int) {
            this.bottomRightRadius = bottomRightRadius
        }

        private fun setCustomBackground() {
            // 默认背景
            gradientDrawable =
                GradientDrawableUtil.init().getNeedDrawable(
                    floatArrayOf(
                        topLeftRadius.toFloat(),
                        topLeftRadius.toFloat(),
                        topRightRadius.toFloat(),
                        topRightRadius.toFloat(),
                        bottomRightRadius.toFloat(),
                        bottomRightRadius.toFloat(),
                        bottomLeftRadius.toFloat(),
                        bottomLeftRadius.toFloat(),
                    ),
                    solidColor,
                    strokeWidth,
                    strokeColor,
                )
            this.background = gradientDrawable
        }
    }
