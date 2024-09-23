package com.app.app

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Process
import androidx.multidex.MultiDex

abstract class BaseApplication : Application() {
    override fun attachBaseContext(context: Context?) {
        super.attachBaseContext(context)
        MultiDex.install(context)
        App.app = this
    }

    override fun onCreate() {
        super.onCreate()
        // 注册APP前后台切换监听
        registerActivityLifecycle()
        if (isInMainProcess()) {
            initOnMainProcess()
        }
    }

    private fun isInMainProcess(): Boolean {
        val myPid = Process.myPid()
        val mActivityManager = this.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        var processName: String? = null
        for (appProcess in mActivityManager.runningAppProcesses) {
            if (appProcess.pid == myPid) {
                processName = appProcess.processName
                break
            }
        }
        return this.packageName == processName
    }

    /**
     * 注册APP前后台切换监听
     */
    private fun registerActivityLifecycle() {
        App.register(this, { onFront() }, { onBack() })
    }

    protected open fun onFront() {
        // App处于前台
    }

    protected open fun onBack() {
        // App处于后台
    }

    abstract fun initOnMainProcess()
}
