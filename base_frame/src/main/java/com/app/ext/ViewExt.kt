package com.app.ext

import android.view.View

fun View.visible() {
    if (this.visibility != View.VISIBLE) {
        this.visibility = View.VISIBLE
    }
}

fun View.gone() {
    if (this.visibility != View.GONE) {
        this.visibility = View.GONE
    }
}

fun View.invisible() {
    if (this.visibility != View.INVISIBLE) {
        this.visibility = View.INVISIBLE
    }
}

fun View.onClick(l: (View) -> Unit) {
    setOnClickListener(OnSingleClickListener(l))
}

fun View.onClick(l: View.OnClickListener) {
    setOnClickListener(OnSingleClickListener(l))
}

class OnSingleClickListener(private val innerListener: View.OnClickListener) : View.OnClickListener {
    private var previousClickTimeMillis = 0L

    override fun onClick(v: View) {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - previousClickTimeMillis > DELAY_MILLIS) {
            previousClickTimeMillis = currentTimeMillis
            innerListener.onClick(v)
        }
    }

    companion object {
        private const val DELAY_MILLIS = 300
    }
}
