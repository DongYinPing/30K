package com.app.main.ui.tab

import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
import com.app.list.AbsDataListAdapter
import com.app.list.diff.ModelDiffCallback
import com.app.main.databinding.ItemMainTabBinding

class MainTabAdapter : AbsDataListAdapter<MainTabItem, ItemMainTabBinding>(TabDiffCallback()) {
    override fun createBindView(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): ItemMainTabBinding {
        return ItemMainTabBinding.inflate(inflater, parent, false)
    }

    override fun onBindItemView(
        viewBind: ItemMainTabBinding,
        item: MainTabItem,
        position: Int,
    ) {
        viewBind.tabIcon.load(item.icon)
        viewBind.tabTitle.text = item.title
        viewBind.tabTitle.setTextColor(
            if (item.isActive) {
                viewBind.tabTitle.resources.getColor(item.titleActiveColor)
            } else {
                viewBind.tabTitle.resources.getColor(item.titleNormalColor)
            },
        )
    }
}

class TabDiffCallback : ModelDiffCallback<MainTabItem>() {
    override fun areItemsTheSame(
        oldItem: MainTabItem,
        newItem: MainTabItem,
    ): Boolean {
        return oldItem.icon == newItem.icon
    }
}

data class MainTabItem(
    var index: Int = 0,
    var icon: Int = 0,
    var title: String,
    var titleNormalColor: Int = 0,
    var titleActiveColor: Int = 0,
    var isActive: Boolean = false,
)
