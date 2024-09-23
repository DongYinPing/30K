package com.app.study

import com.alibaba.android.arouter.facade.annotation.Route
import com.app.android.databinding.FragmentStudyBinding
import com.app.mvvm.AbsVmVbFragment
import com.app.path.Page

/**
 * @desc: kotlin 面试 知识点
 */
@Route(path = Page.KOTLIN_FRAGMENT)
class KotlinFragment : AbsVmVbFragment<StudyViewModel, FragmentStudyBinding>() {
    override fun initView() {
        //
    }

    override fun initData() {
        //
    }
}
