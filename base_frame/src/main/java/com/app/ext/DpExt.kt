package com.app.ext

import com.app.app.App

val density: Float by lazy {
    // 依赖AppHelper.app初始化
    App.app.resources.displayMetrics.density
}

inline val Int.dp: Int
    get() {
        return this.toFloat().dp
    }

inline val Int.px: Int
    get() {
        return (this / density + 0.5f).toInt()
    }

inline val Float.px: Int
    get() {
        return (this / density + 0.5f).toInt()
    }

inline val Float.pxf: Float
    get() {
        return (this / density + 0.5f)
    }

inline val Float.dp: Int
    get() {
        if (this == 0f) {
            return 0
        }
        return (density * this + 0.5f).toInt()
    }

inline val Float.dpf: Float
    get() {
        return density * this
    }

inline val Int.dpf: Float
    get() {
        return density * this
    }
