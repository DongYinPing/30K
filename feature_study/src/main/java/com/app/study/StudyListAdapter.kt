package com.app.study

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.app.android.R
import com.app.android.databinding.ItemStudyDescBinding
import com.app.android.databinding.ItemStudyTitleBinding
import com.app.list.AbsDataMultiAdapter

/**
 * @desc: 学习列表适配器
 */
class StudyListAdapter : AbsDataMultiAdapter<StudyItem>(ItemDiffCallback()) {
    override fun createBindView(
        view: View,
        viewType: Int,
    ): ViewBinding {
        if (viewType == R.layout.item_study_title) {
            return ItemStudyTitleBinding.bind(view)
        } else if (viewType == R.layout.item_study_desc) {
            return ItemStudyDescBinding.bind(view)
        }
        throw IllegalArgumentException("This view type cannot be viewBinding")
    }

    override fun getItemLayout(item: StudyItem): Int {
        return item.layout
    }

    override fun onBindItemView(
        viewBind: ViewBinding,
        item: StudyItem,
        position: Int,
    ) {
        if (viewBind is ItemStudyTitleBinding) {
            viewBind.title.text = item.title
        } else if (viewBind is ItemStudyDescBinding) {
            viewBind.desc.text = item.desc
        }
    }

    class ItemDiffCallback : DiffUtil.ItemCallback<StudyItem>() {
        override fun areItemsTheSame(
            oldItem: StudyItem,
            newItem: StudyItem,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: StudyItem,
            newItem: StudyItem,
        ): Boolean {
            return oldItem == newItem
        }
    }
}

data class StudyItem(
    val id: Int,
    val layout: Int,
    val title: String? = null,
    val desc: String? = null,
    val md: String? = null,
    val url: String? = null,
)
