package com.app.list.ext

import android.view.View
import androidx.viewbinding.ViewBinding
import com.app.list.AbsDataListAdapter
import com.app.list.AbsDataMultiAdapter
import com.app.list.AbsItemListAdapter
import com.app.list.AbsItemMultiAdapter
import com.app.list.item.ItemLayout
import com.app.list.listener.OnClickChildViewListener
import com.app.list.listener.OnClickItemViewListener
import com.app.list.listener.OnLongClickItemViewListener

/**
 * Adapter的点击事件Kotlin扩展，方便调用
 */
fun <T : ItemLayout<*>, VB : ViewBinding> AbsItemListAdapter<T, VB>.onItemClick(listener: (VB, T, position: Int) -> Unit) {
    setOnClickItemViewListener(
        object : OnClickItemViewListener<T, VB> {
            override fun onClickItemView(
                viewBind: VB,
                item: T,
                position: Int,
            ) {
                listener.invoke(viewBind, item, position)
            }
        },
    )
}

fun <T : ItemLayout<*>, VB : ViewBinding> AbsItemListAdapter<T, VB>.onLongItemClick(listener: (VB, T, position: Int) -> Boolean) {
    setOnLongClickItemViewListener(
        object : OnLongClickItemViewListener<T, VB> {
            override fun onLongClickItemView(
                viewBind: VB,
                item: T,
                position: Int,
            ): Boolean {
                return listener.invoke(viewBind, item, position)
            }
        },
    )
}

fun <T : ItemLayout<*>, VB : ViewBinding> AbsItemListAdapter<T, VB>.onChildClick(listener: (VB, T, child: View, position: Int) -> Unit) {
    setOnClickChildViewListener(
        object : OnClickChildViewListener<T, VB> {
            override fun onClickChildView(
                viewBind: VB,
                item: T,
                child: View,
                position: Int,
            ) {
                listener.invoke(viewBind, item, child, position)
            }
        },
    )
}

fun <T : ItemLayout<*>> AbsItemMultiAdapter<T>.onItemClick(listener: (viewBind: ViewBinding, T, position: Int) -> Unit) {
    setOnClickItemViewListener(
        object : OnClickItemViewListener<T, ViewBinding> {
            override fun onClickItemView(
                viewBind: ViewBinding,
                item: T,
                position: Int,
            ) {
                listener.invoke(viewBind, item, position)
            }
        },
    )
}

fun <T : ItemLayout<*>> AbsItemMultiAdapter<T>.onLongItemClick(listener: (viewBind: ViewBinding, T, position: Int) -> Boolean) {
    setOnLongClickItemViewListener(
        object : OnLongClickItemViewListener<T, ViewBinding> {
            override fun onLongClickItemView(
                viewBind: ViewBinding,
                item: T,
                position: Int,
            ): Boolean {
                return listener.invoke(viewBind, item, position)
            }
        },
    )
}

fun <T : ItemLayout<*>> AbsItemMultiAdapter<T>.onChildClick(listener: (viewBind: ViewBinding, T, child: View, position: Int) -> Unit) {
    setOnClickChildViewListener(
        object : OnClickChildViewListener<T, ViewBinding> {
            override fun onClickChildView(
                viewBind: ViewBinding,
                item: T,
                child: View,
                position: Int,
            ) {
                listener.invoke(viewBind, item, child, position)
            }
        },
    )
}

fun <T, VB : ViewBinding> AbsDataListAdapter<T, VB>.onItemClick(listener: (VB, T, position: Int) -> Unit) {
    setOnClickItemViewListener(
        object : OnClickItemViewListener<T, VB> {
            override fun onClickItemView(
                viewBind: VB,
                item: T,
                position: Int,
            ) {
                listener.invoke(viewBind, item, position)
            }
        },
    )
}

fun <T, VB : ViewBinding> AbsDataListAdapter<T, VB>.onLongItemClick(listener: (VB, T, position: Int) -> Boolean) {
    setOnLongClickItemViewListener(
        object : OnLongClickItemViewListener<T, VB> {
            override fun onLongClickItemView(
                viewBind: VB,
                item: T,
                position: Int,
            ): Boolean {
                return listener.invoke(viewBind, item, position)
            }
        },
    )
}

fun <T, VB : ViewBinding> AbsDataListAdapter<T, VB>.onChildClick(listener: (VB, T, child: View, position: Int) -> Unit) {
    setOnClickChildViewListener(
        object : OnClickChildViewListener<T, VB> {
            override fun onClickChildView(
                viewBind: VB,
                item: T,
                child: View,
                position: Int,
            ) {
                listener.invoke(viewBind, item, child, position)
            }
        },
    )
}

fun <T> AbsDataMultiAdapter<T>.onItemClick(listener: (viewBind: ViewBinding, T, position: Int) -> Unit) {
    setOnClickItemViewListener(
        object : OnClickItemViewListener<T, ViewBinding> {
            override fun onClickItemView(
                viewBind: ViewBinding,
                item: T,
                position: Int,
            ) {
                listener.invoke(viewBind, item, position)
            }
        },
    )
}

fun <T> AbsDataMultiAdapter<T>.onChildClick(listener: (viewBind: ViewBinding, T, child: View, position: Int) -> Unit) {
    setOnClickChildViewListener(
        object : OnClickChildViewListener<T, ViewBinding> {
            override fun onClickChildView(
                viewBind: ViewBinding,
                item: T,
                child: View,
                position: Int,
            ) {
                listener.invoke(viewBind, item, child, position)
            }
        },
    )
}

fun <T> AbsDataMultiAdapter<T>.onLongItemClick(listener: (viewBind: ViewBinding, T, position: Int) -> Boolean) {
    setOnLongClickItemViewListener(
        object : OnLongClickItemViewListener<T, ViewBinding> {
            override fun onLongClickItemView(
                viewBind: ViewBinding,
                item: T,
                position: Int,
            ): Boolean {
                return listener.invoke(viewBind, item, position)
            }
        },
    )
}
