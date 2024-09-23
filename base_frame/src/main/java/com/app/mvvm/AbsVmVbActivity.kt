package com.app.mvvm

import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.app.mvvm.base.BaseActivity
import com.app.mvvm.ext.getTClazz
import com.app.mvvm.ext.inflateBindingWithGeneric

/**
 * MVVM Activity
 */
abstract class AbsVmVbActivity<VM : ViewModel, VB : ViewBinding> :
    BaseActivity(),
    IMvvmFrame<VM, VB> {
    lateinit var viewModel: VM
        private set
    lateinit var viewBind: VB
        private set

    private var loadingDialog: DialogFragment? = null
    private var isShowLoading = false

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        if (savedState == null) {
            viewModel = createViewModel()
        }
        viewBind = createViewBinding()
        setContentView(viewBind.root)
        initView()
        initData()
    }

    // 子类可覆盖实现，不用反射
    override fun createViewModel(): VM {
        return ViewModelProvider(this)[getTClazz(this, 0)]
    }

    // 子类可覆盖实现，不用反射
    override fun createViewBinding(container: ViewGroup?): VB {
        return inflateBindingWithGeneric(layoutInflater, 1)
    }
}
