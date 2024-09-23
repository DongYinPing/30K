package com.app.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.app.list.holder.BindingViewHolder
import com.app.list.item.ItemLayout
import com.app.list.listener.OnClickChildViewListener
import com.app.list.listener.OnClickItemViewListener
import com.app.list.listener.OnLongClickItemViewListener

/**
 * 单一布局，ItemLayout视图
 */
abstract class AbsItemListAdapter<T : ItemLayout<*>, VB : ViewBinding> protected constructor(
    diffCallback: DiffUtil.ItemCallback<T>,
) : ListAdapter<T, BindingViewHolder<VB>>(diffCallback) {
    private var layoutInflater: LayoutInflater? = null

    private var onClickItemViewListener: OnClickItemViewListener<T, VB>? = null

    private var onClickChildViewListener: OnClickChildViewListener<T, VB>? = null

    private var onLongClickItemViewListener: OnLongClickItemViewListener<T, VB>? = null

    fun setOnLongClickItemViewListener(listener: OnLongClickItemViewListener<T, VB>?) {
        onLongClickItemViewListener = listener
    }

    fun setOnClickItemViewListener(listener: OnClickItemViewListener<T, VB>?) {
        onClickItemViewListener = listener
    }

    fun setOnClickChildViewListener(listener: OnClickChildViewListener<T, VB>?) {
        onClickChildViewListener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BindingViewHolder<VB> {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val viewBinding = createBindView(layoutInflater!!, parent)
        return BindingViewHolder(viewBinding)
    }

    override fun onBindViewHolder(
        holder: BindingViewHolder<VB>,
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

    override fun onFailedToRecycleView(holder: BindingViewHolder<VB>): Boolean {
        onViewRecycled(holder)
        return false
    }

    override fun onViewRecycled(holder: BindingViewHolder<VB>) {
        super.onViewRecycled(holder)
        holder.binding.root.setOnClickListener(null)
        holder.binding.root.setOnLongClickListener(null)
        unBindItemView(holder.binding)
    }

    fun addClickChildView(
        viewBind: VB,
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

    open fun unBindItemView(viewBind: VB) {
    }

    abstract fun createBindView(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): VB

    abstract fun onBindItemView(
        viewBind: VB,
        item: T,
        position: Int,
    )
}
