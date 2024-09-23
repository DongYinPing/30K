package com.app.main.ui

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.app.list.ext.onItemClick
import com.app.main.databinding.FragmentMainBinding
import com.app.main.ui.tab.MainPagerAdapter
import com.app.main.ui.tab.MainTabAdapter
import com.app.mvvm.AbsVmVbFragment
import com.app.mvvm.base.EmptyViewModel
import com.app.path.Page

@Route(path = Page.MAIN_FRAGMENT)
class MainFragment : AbsVmVbFragment<EmptyViewModel, FragmentMainBinding>() {
    private var mTableAdapter = MainTabAdapter()
    private val shareMainViewModel: MainViewModel by activityViewModels()

    override fun initView() {
        viewBind.mainTabList.layoutManager = GridLayoutManager(context, 3)
        viewBind.mainTabList.adapter = mTableAdapter
        viewBind.mainTabList.itemAnimator = null
        viewBind.mainTabList.animation = null
        viewBind.mainViewPager.isUserInputEnabled = false
        viewBind.mainViewPager.offscreenPageLimit = 2
        viewBind.mainViewPager.adapter = MainPagerAdapter(this)

        mTableAdapter.onItemClick { _, _, position ->
            shareMainViewModel.updateIndex(position)
        }
    }

    override fun initData() {
        shareMainViewModel.viewData.tabsLiveData.observe(this) {
            mTableAdapter.submitList(it)
        }

        shareMainViewModel.viewData.tabIndex.observe(this) { index ->
            shareMainViewModel.updateTabList(index)
            viewBind.mainViewPager.setCurrentItem(index, false)
        }

        if (shareMainViewModel.viewData.tabIndex.value == null) {
            shareMainViewModel.updateIndex(0)
        }
    }
}
