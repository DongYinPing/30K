package com.app.list.listener

import androidx.viewbinding.ViewBinding

/**
 * 点击事件
 */
interface OnClickItemViewListener<T, VB : ViewBinding> {
    fun onClickItemView(
        viewBind: VB,
        item: T,
        position: Int,
    )
}
