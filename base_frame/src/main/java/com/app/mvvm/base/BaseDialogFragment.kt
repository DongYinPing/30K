package com.app.mvvm.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.app.app.App
import com.app.frame.R

open class BaseDialogFragment : DialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogFragment)
    }

    open fun setViewInsets(
        view: View,
        top: Boolean = true,
        bottom: Boolean = false,
    ) {
        if (!top && !bottom) {
            return
        }
        App.windowInsets?.let {
            val originalPaddingTop: Int = view.paddingTop
            val originalPaddingBottom: Int = view.paddingBottom
            view.setPadding(
                view.paddingLeft,
                originalPaddingTop + if (top) it.top else 0,
                view.paddingRight,
                originalPaddingBottom + if (bottom) it.bottom else 0,
            )
        }
    }
}
