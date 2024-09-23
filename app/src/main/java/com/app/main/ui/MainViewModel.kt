package com.app.main.ui

import com.app.livedata.SameLiveData
import com.app.main.R
import com.app.main.ui.tab.MainTabItem
import com.app.mvvm.base.BaseViewModel

class MainViewModel : BaseViewModel<MainViewModel.ViewData>() {
    fun updateTabList(selectPos: Int) {
        val tabList = mutableListOf<MainTabItem>()
        var index = 0
        tabList.add(
            MainTabItem(
                index,
                R.drawable.svg_android,
                "ANDROID",
                R.color.colorTab,
                R.color.colorTabAndroidActive,
                selectPos == index,
            ),
        )
        index++
        tabList.add(
            MainTabItem(
                index,
                R.drawable.svg_java,
                "JAVA",
                R.color.colorTab,
                R.color.colorTabJavaActive,
                selectPos == index,
            ),
        )
        index++
        tabList.add(
            MainTabItem(
                index,
                R.drawable.svg_kotlin,
                "KOTLIN",
                R.color.colorTab,
                R.color.colorTabKotlinActive,
                selectPos == index,
            ),
        )

        viewData.tabsLiveData.value = tabList
    }

    fun updateIndex(index: Int) {
        viewData.tabIndex.value = index
    }

    class ViewData {
        val tabIndex = SameLiveData<Int>()
        var tabsLiveData = SameLiveData<List<MainTabItem>>()
    }
}
