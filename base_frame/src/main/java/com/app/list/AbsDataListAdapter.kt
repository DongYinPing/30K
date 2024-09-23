package com.app.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.app.list.holder.BindingViewHolder
import com.app.list.listener.OnClickChildViewListener
import com.app.list.listener.OnClickItemViewListener
import com.app.list.listener.OnLongClickItemViewListener

/**
 * 单一布局，data列表
 */
abstract class AbsDataListAdapter<T, VB : ViewBinding> protected constructor(
    diffCallback: DiffUtil.ItemCallback<T>,
) : ListAdapter<T, BindingViewHolder<VB>>(diffCallback) {
    private var layoutInflater: LayoutInflater? = null

    private var onClickItemViewListener: OnClickItemViewListener<T, VB>? = null

    private var onClickChildViewListener: OnClickChildViewListener<T, VB>? = null

    private var onLongClickItemViewListener: OnLongClickItemViewListener<T, VB>? = null

    fun setOnClickItemViewListener(listener: OnClickItemViewListener<T, VB>?) {
        onClickItemViewListener = listener
    }

    fun setOnClickChildViewListener(listener: OnClickChildViewListener<T, VB>?) {
        onClickChildViewListener = listener
    }

    fun setOnLongClickItemViewListener(listener: OnLongClickItemViewListener<T, VB>?) {
        onLongClickItemViewListener = listener
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
            onBindItemView(viewBind, item, position)
            if (onClickItemViewListener != null) {
                viewBind.root.setOnClickListener {
                    onClickItemViewListener?.onClickItemView(viewBind, item, position)
                }
            }
            if (onLongClickItemViewListener != null) {
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
