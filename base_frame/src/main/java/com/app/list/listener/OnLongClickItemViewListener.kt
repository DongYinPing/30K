package com.app.list.listener

import androidx.viewbinding.ViewBinding

/**
 * 长按事件
 */
interface OnLongClickItemViewListener<T, VB : ViewBinding> {
    fun onLongClickItemView(
        viewBind: VB,
        item: T,
        position: Int,
    ): Boolean
}
