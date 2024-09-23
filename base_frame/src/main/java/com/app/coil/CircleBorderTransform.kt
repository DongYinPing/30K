package com.app.coil

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.RectF
import android.graphics.Shader
import androidx.core.graphics.applyCanvas
import coil.bitmap.BitmapPool
import coil.size.OriginalSize
import coil.size.PixelSize
import coil.size.Size
import coil.transform.Transformation

/**
 * 自定义圆角
 * @param radius 园的弧度
 */
class CircleBorderTransform(private val radius: Float) : Transformation {
    override fun key(): String {
        return "CircleBorderTransform-$radius"
    }

    override suspend fun transform(
        pool: BitmapPool,
        input: Bitmap,
        size: Size,
    ): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        val outputWidth: Int
        val outputHeight: Int
        var scale = 1.0f
        var dx = 0f
        var dy = 0f
        when (size) {
            is PixelSize -> {
                outputWidth = size.width
                outputHeight = size.height
                if (input.width * outputHeight > outputWidth * input.height) {
                    // 高度比大于宽度比
                    scale = outputHeight * 1.0f / input.height
                    dx = (outputWidth - input.width * scale) * 0.5f
                } else {
                    scale = outputWidth * 1.0f / input.width
                    dy = (outputHeight - input.height * scale) * 0.5f
                }
            }

            is OriginalSize -> {
                outputWidth = input.width
                outputHeight = input.height
            }
        }

        val output = pool.get(outputWidth, outputHeight, input.safeConfig)
        output.applyCanvas {
            drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

            val matrix = Matrix()
            matrix.setScale(scale, scale)
            matrix.postTranslate((dx + 0.5f).toInt().toFloat(), (dy + 0.5f).toInt().toFloat())
            val shader = BitmapShader(input, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            shader.setLocalMatrix(matrix)
            paint.shader = shader

            val radii = floatArrayOf(radius, radius, radius, radius, radius, radius, radius, radius)
            val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
            val path = Path().apply { addRoundRect(rect, radii, Path.Direction.CW) }
            drawPath(path, paint)
        }
        return output
    }

    private val Bitmap.safeConfig: Bitmap.Config
        get() = config

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return other is CircleBorderTransform && radius == other.radius
    }

    override fun hashCode(): Int {
        return radius.hashCode()
    }

    override fun toString(): String {
        return "CircleBorderTransform(radius=$radius)"
    }
}
