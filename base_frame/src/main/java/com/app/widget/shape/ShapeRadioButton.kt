package com.app.widget.shape

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton
import com.app.frame.R
import com.app.widget.shape.util.GradientDrawableUtil

class ShapeRadioButton
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
    ) : AppCompatRadioButton(context, attrs, defStyleAttr) {
        // 自定背景边框Drawable
        private val gradientDrawable: GradientDrawable

        // 选中时的Drawable
        private var selectorDrawable: GradientDrawable? = null

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

        // 是否使用选择器
        private var openSelector = false

        // 未选中的字体颜色
        private var textNormalColor = Color.BLACK

        // 选中的字体颜色
        private var textSelectColor = Color.RED

        // 选中填充色
        private var solidSelectColor = 0

        // 选中边框色
        private var strokeSelectColor = 0

        init {
            init(context, attrs)
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
            // 如果设置了选中时的背景
            if (openSelector) {
                selectorDrawable =
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
                        solidSelectColor,
                        strokeWidth,
                        strokeSelectColor,
                    )
                // 动态生成Selector
                val stateListDrawable = StateListDrawable()
                // 是否选中
                val checked = android.R.attr.state_checked
                stateListDrawable.addState(intArrayOf(checked), selectorDrawable)
                stateListDrawable.addState(intArrayOf(), gradientDrawable)
                // 设置背景色
                this.background = stateListDrawable
                val states = arrayOfNulls<IntArray>(2)
                states[0] = intArrayOf(android.R.attr.state_checked)
                states[1] = intArrayOf()
                val textColorStateList =
                    ColorStateList(states, intArrayOf(textSelectColor, textNormalColor))
                this.setTextColor(textColorStateList)
            } else {
                this.background = gradientDrawable
            }
            this.isFocusable = false
            this.isClickable = true
        }

        /**
         * 初始化参数
         *
         * @param context
         * @param attrs
         */
        private fun init(
            context: Context,
            attrs: AttributeSet?,
        ) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.ShapeRadioButton)
            // 填充以及边框
            solidColor = ta.getColor(R.styleable.ShapeRadioButton_solidColor, Color.TRANSPARENT)
            strokeColor = ta.getColor(R.styleable.ShapeRadioButton_strokeColor, Color.TRANSPARENT)
            strokeWidth = ta.getDimensionPixelSize(R.styleable.ShapeRadioButton_strokeWidth, 0)
            // 四个角单独设置会覆盖radius设置
            radius = ta.getDimensionPixelSize(R.styleable.ShapeRadioButton_radius, 0)
            topLeftRadius = ta.getDimensionPixelSize(R.styleable.ShapeRadioButton_topLeftRadius, radius)
            topRightRadius =
                ta.getDimensionPixelSize(R.styleable.ShapeRadioButton_topRightRadius, radius)
            bottomLeftRadius =
                ta.getDimensionPixelSize(R.styleable.ShapeRadioButton_bottomLeftRadius, radius)
            bottomRightRadius =
                ta.getDimensionPixelSize(R.styleable.ShapeRadioButton_bottomRightRadius, radius)
            // 选择器
            openSelector = ta.getBoolean(R.styleable.ShapeRadioButton_openSelector, false)
            textNormalColor = ta.getColor(R.styleable.ShapeRadioButton_textNormalColor, Color.BLACK)
            textSelectColor = ta.getColor(R.styleable.ShapeRadioButton_textSelectColor, Color.RED)
            solidSelectColor =
                ta.getColor(R.styleable.ShapeRadioButton_solidSelectColor, Color.TRANSPARENT)
            strokeSelectColor =
                ta.getColor(R.styleable.ShapeRadioButton_strokeSelectColor, Color.TRANSPARENT)
            ta.recycle()
        }
    }
