package com.app.mvvm

import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

/**
 * MvvM框架定义
 */
interface IMvvmFrame<VM : ViewModel, VB : ViewBinding> {
    /**
     * 创建ViewBind
     */
    fun createViewBinding(container: ViewGroup? = null): VB

    /**
     * 创建ViewModel
     */
    fun createViewModel(): VM

    /**
     * 初始化View
     */
    fun initView()

    /**
     * 初始化数据
     */
    fun initData()
}
