package com.app.mvvm.base

import androidx.annotation.Keep
import androidx.lifecycle.ViewModel
import com.app.mvvm.ext.getTClazz

@Keep
abstract class BaseViewModel<VD> : ViewModel() {
    val viewData: VD = createViewDataByDeclared()

    // 通过反射创建viewData
    private fun createViewDataByDeclared(): VD {
        val clazz: Class<VD> = getTClazz(this)
        return clazz.getDeclaredConstructor().newInstance()
    }
}
