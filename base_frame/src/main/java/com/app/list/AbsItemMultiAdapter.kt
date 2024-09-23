package com.app.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.app.list.holder.BindingMultiViewHolder
import com.app.list.item.ItemLayout
import com.app.list.listener.OnClickChildViewListener
import com.app.list.listener.OnClickItemViewListener
import com.app.list.listener.OnLongClickItemViewListener

/**
 * 多布局，ItemLayout视图
 */
abstract class AbsItemMultiAdapter<T : ItemLayout<*>>(
    diffCallback: DiffUtil.ItemCallback<T>,
) : ListAdapter<T, BindingMultiViewHolder>(diffCallback) {
    private var layoutInflater: LayoutInflater? = null

    private var onClickChildViewListener: OnClickChildViewListener<T, ViewBinding>? = null

    private var onClickItemViewListener: OnClickItemViewListener<T, ViewBinding>? = null

    private var onLongClickItemViewListener: OnLongClickItemViewListener<T, ViewBinding>? = null

    fun setOnLongClickItemViewListener(listener: OnLongClickItemViewListener<T, ViewBinding>?) {
        onLongClickItemViewListener = listener
    }

    fun setOnClickItemViewListener(listener: OnClickItemViewListener<T, ViewBinding>?) {
        onClickItemViewListener = listener
    }

    fun setOnClickChildViewListener(listener: OnClickChildViewListener<T, ViewBinding>?) {
        onClickChildViewListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        getItem(position)?.let { item ->
            if (item.viewType != 0) {
                return item.viewType
            }
        }
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BindingMultiViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val view = layoutInflater!!.inflate(viewType, parent, false)
        val viewBinding = createBindView(view, viewType)
        return BindingMultiViewHolder(viewBinding)
    }

    override fun onBindViewHolder(
        holder: BindingMultiViewHolder,
        position: Int,
    ) {
        getItem(position)?.let { item ->
            val viewBind = holder.binding
            changeItemWidthHeightOnBind(viewBind.root, item.width, item.height)
            item.position = position
            viewBind.root.isClickable = item.clickable
            onBindItemView(viewBind, item, position)
            if (onClickItemViewListener != null && item.clickable) {
                viewBind.root.setOnClickListener {
                    onClickItemViewListener?.onClickItemView(viewBind, item, position)
                }
            }
            if (onLongClickItemViewListener != null && item.clickable) {
                viewBind.root.setOnLongClickListener {
                    return@setOnLongClickListener onLongClickItemViewListener?.onLongClickItemView(
                        viewBind,
                        item,
                        position,
                    ) == true
                }
            }
        }
    }

    private fun changeItemWidthHeightOnBind(
        view: View,
        width: Int,
        height: Int,
    ) {
        var requestLayout = false
        if (width > 0) {
            requestLayout = true
            view.layoutParams.width = width
        } else if (width == ViewGroup.MarginLayoutParams.WRAP_CONTENT || width == ViewGroup.MarginLayoutParams.MATCH_PARENT) {
            requestLayout = true
            view.layoutParams.width = width
        }
        if (height > 0) {
            requestLayout = true
            view.layoutParams.height = height
        } else if (height == ViewGroup.MarginLayoutParams.WRAP_CONTENT || height == ViewGroup.MarginLayoutParams.MATCH_PARENT) {
            requestLayout = true
            view.layoutParams.height = height
        }
        if (requestLayout) {
            view.requestLayout()
        }
    }

    override fun onFailedToRecycleView(holder: BindingMultiViewHolder): Boolean {
        onViewRecycled(holder)
        return false
    }

    override fun onViewRecycled(holder: BindingMultiViewHolder) {
        super.onViewRecycled(holder)
        // 移除在onBindViewHolder中设置的Listener，不再进行回调
        holder.binding.root.setOnClickListener(null)
        holder.binding.root.setOnLongClickListener(null)
        // 释放监听
        unBindItemView(holder.binding)
    }

    open fun addClickChildView(
        viewBind: ViewBinding,
        item: T,
        position: Int,
        vararg childList: View,
    ) {
        childList.forEach {
            it.setOnClickListener { child ->
                onClickChildViewListener?.onClickChildView(viewBind, item, child, position)
            }
        }
    }

    fun clearClickChildView(vararg childList: View) {
        childList.forEach {
            it.setOnClickListener(null)
        }
    }

    open fun unBindItemView(viewBind: ViewBinding) {
    }

    abstract fun createBindView(
        view: View,
        viewType: Int,
    ): ViewBinding

    abstract fun onBindItemView(
        viewBind: ViewBinding,
        item: T,
        position: Int,
    )
}
