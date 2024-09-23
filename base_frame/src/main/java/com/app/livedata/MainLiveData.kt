package com.app.livedata

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * 减少handler创建
 */
val handler by lazy {
    Handler(Looper.getMainLooper())
}

/**
 * 在主线程中更新值
 */
open class MainLiveData<T> : LiveData<T>() {
    override fun observe(
        owner: LifecycleOwner,
        observer: Observer<in T>,
    ) {
        if (isMainThread) {
            super.observe(owner, observer)
        } else {
            handler.post {
                super.observe(owner, observer)
            }
        }
    }

    open fun observe(
        fragment: Fragment,
        observer: Observer<in T>,
    ) {
        // 传入Fragment则转化使用ViewLifecycleOwner
        observe(fragment.viewLifecycleOwner, observer)
    }

    public override fun setValue(value: T?) {
        postValue(value)
    }

    public override fun postValue(value: T?) {
        if (isMainThread) {
            super.setValue(value)
        } else {
            super.postValue(value)
        }
    }

    override fun observeForever(observer: Observer<in T>) {
        if (isMainThread) {
            super.observeForever(observer)
        } else {
            handler.post {
                super.observeForever(observer)
            }
        }
    }

    private val isMainThread: Boolean
        get() = Thread.currentThread() === Looper.getMainLooper().thread
}
