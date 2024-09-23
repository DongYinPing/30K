package com.app.mvvm.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.app.app.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * FragmentActivity 主题全屏模式
 */
open class BaseActivity : AppCompatActivity() {
    private var loadingDialog: DialogFragment? = null
    private var originalPaddingTop: Int = 0
    private var originalPaddingBottom: Int = 0

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        initWindowInsetsOnCreate()
    }

    /**
     * 在window.decorView上下增加内间距，避免导航栏和状态栏重叠
     */
    private fun initWindowInsetsOnCreate() {
        originalPaddingTop = window.decorView.paddingTop
        originalPaddingBottom = window.decorView.paddingBottom
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { _, windowInsetsCompat ->
            saveWindowInsets(windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars()))
            setViewInsets(
                window.decorView,
                isInsetsTop(),
                isInsetsBottom(),
            )
            windowInsetsCompat
        }
    }

    open fun setViewInsets(
        view: View,
        topInset: Boolean = true,
        bottomInset: Boolean = false,
    ) {
        if (!topInset && !bottomInset) {
            return
        }
        App.windowInsets?.let {
            view.setPadding(
                view.paddingLeft,
                originalPaddingTop + if (topInset) it.top else 0,
                view.paddingRight,
                originalPaddingBottom + if (bottomInset) it.bottom else 0,
            )
        }
    }

    private fun saveWindowInsets(insets: Insets) {
        lifecycleScope.launch(Dispatchers.IO) {
            App.setWindowInsets(insets)
        }
    }

    protected open fun isInsetsBottom(): Boolean {
        return true
    }

    protected open fun isInsetsTop(): Boolean {
        return false
    }
}
