package com.app.list.diff

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

/**
 *  列表更新，数据比较
 */
abstract class ModelDiffCallback<T : Any> : DiffUtil.ItemCallback<T>() {
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: T,
        newItem: T,
    ): Boolean {
        // 最好是data class
        return oldItem == newItem
    }
}
