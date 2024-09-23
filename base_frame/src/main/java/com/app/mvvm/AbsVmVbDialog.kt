package com.app.mvvm

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.app.mvvm.base.BaseDialogFragment
import com.app.mvvm.ext.getTClazz
import com.app.mvvm.ext.inflateBindingWithGeneric

/**
 * MVVM Dialog
 */
abstract class AbsVmVbDialog<VM : ViewModel, VB : ViewBinding> :
    BaseDialogFragment(),
    IMvvmFrame<VM, VB> {
    lateinit var viewModel: VM
        private set
    lateinit var viewBind: VB
        private set
    private var _viewBind: VB? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // 去掉dialog的标题，需要在setContentView()之前
        this.dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val window = this.dialog!!.window
        // 去掉dialog默认的padding
        window!!.decorView.setPadding(0, 0, 0, 0)
        val lp = window.attributes
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = lp
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // 背景全透明
        window.setDimAmount(0.7f)
        // 弹出时状态栏颜色不改变
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        _viewBind = createViewBinding(container)
        viewBind = _viewBind as VB
        return viewBind.root
    }

    override fun onViewCreated(
        view: View,
        savedState: Bundle?,
    ) {
        super.onViewCreated(view, savedState)
        viewModel = createViewModel()
        initView()
        initData()
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
