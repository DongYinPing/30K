package com.app.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.app.list.holder.BindingMultiViewHolder
import com.app.list.listener.OnClickChildViewListener
import com.app.list.listener.OnClickItemViewListener
import com.app.list.listener.OnLongClickItemViewListener

/**
 * 多布局，data视图
 */
abstract class AbsDataMultiAdapter<T>(
    diffCallback: DiffUtil.ItemCallback<T>,
) : ListAdapter<T, BindingMultiViewHolder>(diffCallback) {
    private var layoutInflater: LayoutInflater? = null

    private var onClickChildViewListener: OnClickChildViewListener<T, ViewBinding>? = null

    private var onClickItemViewListener: OnClickItemViewListener<T, ViewBinding>? = null

    private var onLongClickItemViewListener: OnLongClickItemViewListener<T, ViewBinding>? = null

    fun setOnClickItemViewListener(listener: OnClickItemViewListener<T, ViewBinding>?) {
        onClickItemViewListener = listener
    }

    fun setOnClickChildViewListener(listener: OnClickChildViewListener<T, ViewBinding>?) {
        onClickChildViewListener = listener
    }

    fun setOnLongClickItemViewListener(listener: OnLongClickItemViewListener<T, ViewBinding>?) {
        onLongClickItemViewListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        getItem(position)?.let { item ->
            return getItemLayout(item)
        }
        return position
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

    fun addClickChildView(
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

    abstract fun getItemLayout(item: T): Int
}
