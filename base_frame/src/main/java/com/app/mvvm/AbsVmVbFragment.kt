package com.app.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.app.mvvm.base.BaseFragment
import com.app.mvvm.ext.getTClazz
import com.app.mvvm.ext.inflateBindingWithGeneric

/**
 * MVVM Fragment
 */
abstract class AbsVmVbFragment<VM : ViewModel, VB : ViewBinding> :
    BaseFragment(),
    IMvvmFrame<VM, VB> {
    lateinit var viewModel: VM
        private set
    lateinit var viewBind: VB
        private set
    private var _viewBind: VB? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedState)
        _viewBind = createViewBinding(container)
        viewBind = _viewBind as VB

        return viewBind.root
    }

    override fun onViewCreated(
        view: View,
        savedState: Bundle?,
    ) {
        super.onViewCreated(view, savedState)
        if (savedState == null) {
            viewModel = createViewModel()
            initView()
            initData()
        }
    }

    override fun createViewModel(): VM {
        return ViewModelProvider(this)[getTClazz(this, 0)]
    }

    override fun createViewBinding(container: ViewGroup?): VB {
        return inflateBindingWithGeneric(layoutInflater, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBind = null
    }
}
