package com.app.main.ui

import android.os.Bundle
import com.app.logger.LogUtil
import com.app.main.databinding.ActivityMainBinding
import com.app.mvvm.AbsVmVbActivity
import com.app.mvvm.base.EmptyViewModel
import com.app.navigator.replacePageFragment
import com.app.path.Page
import com.blankj.utilcode.util.BarUtils

class MainActivity : AbsVmVbActivity<EmptyViewModel, ActivityMainBinding>() {
    override fun initView() {
        // Toolbar 黑字白底
        BarUtils.setStatusBarLightMode(this, true)
        replacePageFragment(
            containerId = android.R.id.content,
            path = Page.MAIN_FRAGMENT,
            setPrimary = true,
        )
    }

    override fun initData() {
    }

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        LogUtil.d(this, msg = "onCreate")
    }

    override fun onStart() {
        super.onStart()
        LogUtil.d(this, msg = "onStart")
    }

    override fun onRestart() {
        super.onRestart()
        LogUtil.d(this, msg = "onRestart")
    }

    override fun onResume() {
        super.onResume()
        LogUtil.d(this, msg = "onResume")
    }

    override fun onPause() {
        super.onPause()
        LogUtil.d(this, msg = "onPause")
    }

    override fun onStop() {
        super.onStop()
        LogUtil.d(this, msg = "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtil.d(this, msg = "onDestroy")
    }
}
