package com.app.mvvm.base

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class BaseBottomDialogFragment : BottomSheetDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val bottomSheet: FrameLayout =
                dialog.window?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            // 设备dialog无背景，避免圆角出现白色背景
            bottomSheet.setBackgroundResource(android.R.color.transparent)
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheetBehavior.isDraggable = false
            bottomSheetBehavior.isHideable = false
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return dialog
    }
}
