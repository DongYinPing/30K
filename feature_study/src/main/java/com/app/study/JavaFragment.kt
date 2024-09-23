package com.app.study

import com.alibaba.android.arouter.facade.annotation.Route
import com.app.android.databinding.FragmentStudyBinding
import com.app.mvvm.AbsVmVbFragment
import com.app.path.Page

/**
 * @desc: java 面试 知识点
 */
@Route(path = Page.JAVA_FRAGMENT)
class JavaFragment : AbsVmVbFragment<StudyViewModel, FragmentStudyBinding>() {
    override fun initView() {
        //
    }

    override fun initData() {
        //
    }
}
