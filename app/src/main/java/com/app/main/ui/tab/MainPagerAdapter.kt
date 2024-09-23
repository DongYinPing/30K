package com.app.main.ui.tab

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alibaba.android.arouter.launcher.ARouter
import com.app.path.Page

class MainPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val tabFragmentsCreators: Map<Int, () -> Fragment> =
        mapOf(
            0 to {
                ARouter.getInstance()
                    .build(Page.ANDROID_FRAGMENT)
                    .navigation() as Fragment
            },
            1 to {
                ARouter.getInstance()
                    .build(Page.JAVA_FRAGMENT)
                    .navigation() as Fragment
            },
            1 to {
                ARouter.getInstance()
                    .build(Page.KOTLIN_FRAGMENT)
                    .navigation() as Fragment
            },
        )

    override fun getItemCount() = tabFragmentsCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}
