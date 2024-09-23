package com.app.navigator

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.alibaba.android.arouter.launcher.ARouter
import com.app.frame.R

/**
 * 路由导航
 */

private var lastClickTime = 0L

private const val INTERVAL_TIME = 500

fun FragmentActivity.openPageFragment(
    containerId: Int = android.R.id.content,
    path: String,
    title: String? = "",
    info: String? = "",
    fromType: Int = 0,
    hidePrimary: Boolean = true,
    addToBackStack: Boolean = true,
    setToPrimary: Boolean = true,
): Fragment? {
    return openPageFragment(
        supportFragmentManager,
        containerId,
        path,
        title,
        info,
        fromType,
        hidePrimary,
        addToBackStack,
        setToPrimary,
    )
}

fun FragmentActivity.replacePageFragment(
    containerId: Int,
    path: String,
    setPrimary: Boolean? = false,
    addToBackStack: Boolean? = false,
) {
    val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
    replacePageFragment(ft, containerId, path, setPrimary, addToBackStack)
}

fun FragmentActivity.showDialogFragment(
    path: String,
    title: String? = "",
    info: String? = "",
): DialogFragment? {
    return showDialogFragment(supportFragmentManager, path, title, info)
}

fun FragmentActivity.popBackStack(
    path: String?,
    flags: Int? = 0,
) {
    popBackStack(supportFragmentManager, path, flags)
}

fun FragmentActivity.popBackAllStack() {
    popBackAllStack(supportFragmentManager)
}

fun FragmentActivity.removeAllFragment() {
    val fm = supportFragmentManager
    val al = fm.fragments
    for (frag in al) {
        if (frag != null) {
            supportFragmentManager.beginTransaction().remove(frag).commit()
            fm.popBackStack()
        }
    }
}

fun Fragment.openPageFragment(
    containerId: Int = android.R.id.content,
    path: String,
    title: String? = "",
    info: String? = "",
    fromType: Int = 0,
    hidePrimary: Boolean = true,
    addToBackStack: Boolean = true,
    setToPrimary: Boolean = true,
): Fragment? {
    activity?.let {
        return it.openPageFragment(
            containerId,
            path,
            title,
            info,
            fromType,
            hidePrimary,
            addToBackStack,
            setToPrimary,
        )
    }
    return null
}

fun Fragment.replacePageFragment(
    containerId: Int,
    path: String,
    setPrimary: Boolean? = false,
    addToBackStack: Boolean? = false,
) {
    activity?.let {
        it.replacePageFragment(containerId, path, setPrimary, addToBackStack)
    }
}

fun Fragment.showDialogFragment(
    path: String,
    title: String? = null,
    info: String? = null,
): DialogFragment? {
    activity?.let {
        return it.showDialogFragment(path, title, info)
    }
    return null
}

fun Fragment.popBackStack(
    path: String? = null,
    flags: Int? = 0,
) {
    activity?.popBackStack(path, flags)
}

fun Fragment.popBackAllStack() {
    activity?.popBackAllStack()
}

fun Context.openOutWeb(url: String?) {
    if (url == null) {
        return
    }
    val uri = Uri.parse(url)
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
}

private fun replacePageFragment(
    ft: FragmentTransaction,
    containerId: Int,
    path: String,
    setPrimary: Boolean? = false,
    addToBackStack: Boolean? = false,
) {
    val fragment =
        ARouter.getInstance()
            .build(path)
            .navigation() as Fragment
    if (addToBackStack == true) {
        path.let {
            ft.addToBackStack(path)
        }
    }
    ft.replace(containerId, fragment)
    if (setPrimary == true || addToBackStack == true) {
        ft.setPrimaryNavigationFragment(fragment)
        ft.setReorderingAllowed(true)
    }
    ft.commitAllowingStateLoss()
}

private fun openPageFragment(
    fm: FragmentManager,
    containerId: Int = android.R.id.content,
    path: String,
    title: String? = "",
    info: String? = "",
    fromType: Int = 0,
    hidePrimary: Boolean = true,
    addToBackStack: Boolean = true,
    setToPrimary: Boolean = true,
): Fragment? {
    if (System.currentTimeMillis() - lastClickTime < INTERVAL_TIME) {
        return null
    }
    lastClickTime = System.currentTimeMillis()
    // 检查Fragment是否已经添加
    val to =
        ARouter.getInstance()
            .build(path)
            .setTag(path)
            .withString("title", title)
            .withString("info", info)
            .navigation() as Fragment
    val ft: FragmentTransaction = fm.beginTransaction()
    when (fromType) {
        // 左退右进显示
        0 -> {
            ft.setCustomAnimations(
                R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_left,
                R.anim.anim_lide_in_right,
                R.anim.anim_slide_out_right,
            )
        }
        // 直接显示
        1 -> {
            ft.setCustomAnimations(0, 0, 0, 0)
        }
        // 从底部显示
        2 -> {
            ft.setCustomAnimations(
                R.anim.anim_slide_in_bottom,
                0,
                0,
                R.anim.anim_slide_out_bottom,
            )
        }
    }
    // 是否隐藏上一个界面
    if (hidePrimary) {
        fm.primaryNavigationFragment?.let {
            ft.hide(it)
        }
    }

    // 当前界面是否已经添加
    if (to.isAdded) {
        ft.show(to)
    } else {
        // 是否添加到回退栈中
        if (addToBackStack) {
            path.let {
                ft.addToBackStack(it)
            }
        }
        ft.add(containerId, to, path)
    }
    if (setToPrimary) {
        // 记录上一个Fragment
        ft.setPrimaryNavigationFragment(to)
        ft.setReorderingAllowed(true)
    } else {
        ft.setReorderingAllowed(false)
    }
    ft.commitAllowingStateLoss()
    fm.executePendingTransactions()
    return to
}

private fun showDialogFragment(
    fm: FragmentManager,
    path: String,
    title: String?,
    info: String?,
): DialogFragment? {
    val dialog =
        ARouter.getInstance()
            .build(path)
            .withString("title", title)
            .withString("info", info)
            .navigation() as DialogFragment
    dialog.show(fm, path)
    return dialog
}

/**
 * @param tag fragment堆栈对应的tag，Router可以用path
 * @param flags 退栈方式
 * tag可以为null或者相对应的tag，flags只有0和1(POP_BACK_STACK_INCLUSIVE)两种情况
 * 如果tag为null，flags为0时，弹出回退栈中最上层的那个fragment。
 * 如果tag为null，flags为1时，弹出回退栈中所有fragment。
 * 如果tag不为null，flags为0时，弹出tag不包含tag的所有fragment。
 * 如果tag不为null，flags为1时，弹出tag上包含tag的所有fragment。
 */
private fun popBackStack(
    fm: FragmentManager,
    tag: String? = null,
    flags: Int? = 0,
) {
    // 禁止连续点击弹出页面
    if (System.currentTimeMillis() - lastClickTime < INTERVAL_TIME) {
        return
    }
    lastClickTime = System.currentTimeMillis()
    try {
        if (flags == null || flags == 0 || flags == 1) {
            fm.popBackStackImmediate(tag, flags ?: 0)
        }
    } catch (_: Exception) {
    }
}

private fun popBackAllStack(fm: FragmentManager) {
    popBackStack(fm, null, 1)
}
