package com.app.study

import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.app.android.databinding.FragmentStudyBinding
import com.app.list.ext.onItemClick
import com.app.mvvm.AbsVmVbFragment
import com.app.navigator.openOutWeb
import com.app.navigator.openPageFragment
import com.app.path.Page

/**
 * @desc: android 面试 知识点
 */
@Route(path = Page.ANDROID_FRAGMENT)
class AndroidFragment : AbsVmVbFragment<StudyViewModel, FragmentStudyBinding>() {
    private val adapter = StudyListAdapter()

    override fun initView() {
        viewBind.list.layoutManager = LinearLayoutManager(context)
        viewBind.list.animation = null
        viewBind.list.itemAnimator = null
        viewBind.list.adapter = adapter
        viewBind.list.setHasFixedSize(true)
        adapter.onItemClick { _, item, _ ->
            item.md?.let {
                openPageFragment(path = Page.MARKDOWN_FRAGMENT, title = item.desc, info = it)
            }
            item.url?.let {
                context?.openOutWeb(url = it)
            }
        }
    }

    override fun initData() {
        viewModel.androidStudyData.observe(this) {
            adapter.submitList(it)
        }

        viewModel.createAndroidStudyData()
    }

    override fun isInsetsTop(): Boolean {
        return true
    }
}
