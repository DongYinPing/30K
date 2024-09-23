package com.app.list.listener

import android.view.View
import androidx.viewbinding.ViewBinding

/**
 * 子视图点击事件
 */
interface OnClickChildViewListener<T, VB : ViewBinding?> {
    fun onClickChildView(
        viewBind: VB,
        item: T,
        child: View,
        position: Int,
    )
}
