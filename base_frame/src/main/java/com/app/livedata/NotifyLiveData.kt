package com.app.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import java.util.Timer
import java.util.TimerTask

/**
 * 非粘性通知，避免重复发送通知
 */
class NotifyLiveData<T> : MainLiveData<T>() {
    private var isCleaning = false
    private var hasHandled = true
    private var isDelaying = false
    private val mTimer by lazy {
        Timer()
    }
    private var mTask: TimerTask? = null

    // 是否允许空值
    private var isAllowNullValue = false

    // 是否允许延时清理
    private var isAllowToClear = true

    companion object {
        // 等于1秒内清除数据
        const val DELAY_TO_CLEAR_EVENT = 1000
    }

    override fun observe(
        owner: LifecycleOwner,
        observer: Observer<in T>,
    ) {
        super.observe(
            owner,
            Observer { t: T ->
                if (isCleaning) {
                    hasHandled = true
                    isDelaying = false
                    isCleaning = false
                    return@Observer
                }
                if (!hasHandled) {
                    hasHandled = true
                    isDelaying = true
                    observer.onChanged(t)
                } else if (isDelaying) {
                    observer.onChanged(t)
                }
            },
        )
    }

    // 不建议用observeForever
    override fun observeForever(observer: Observer<in T>) {
        throw IllegalArgumentException("Do not use observeForever for communication between pages to avoid lifecycle security issues")
    }

    override fun setValue(value: T?) {
        if (!isCleaning && !isAllowNullValue && value == null) {
            return
        }
        hasHandled = false
        isDelaying = false
        super.setValue(value)
        mTask?.cancel()
        mTimer.purge()
        if (value != null) {
            mTask =
                object : TimerTask() {
                    override fun run() {
                        clear()
                    }
                }
            mTimer.schedule(mTask, DELAY_TO_CLEAR_EVENT.toLong())
        }
    }

    private fun clear() {
        if (isAllowToClear) {
            isCleaning = true
            super.setValue(null)
        } else {
            hasHandled = true
            isDelaying = false
        }
    }
}
