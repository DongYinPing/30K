package com.app.mvvm.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.app.app.App

/**
 * 全屏模式下
 */
open class BaseFragment : Fragment() {
    private val fragmentsKey: String = "android:support:fragments"
    private val fragmentsKey2: String = "android:fragments"

    override fun onViewCreated(
        view: View,
        savedState: Bundle?,
    ) {
        savedState?.remove(fragmentsKey)
        savedState?.remove(fragmentsKey2)
        super.onViewCreated(view, savedState)
        if (savedState == null) {
            setViewInsets(view, isInsetsTop(), isInsetsBottom())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.remove(fragmentsKey)
        outState.remove(fragmentsKey2)
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

    protected open fun isInsetsTop(): Boolean {
        return true
    }

    protected open fun isInsetsBottom(): Boolean {
        return false
    }
}
