package com.app.app

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.core.graphics.Insets
import com.app.ext.getObjData
import com.app.ext.putFlowData
import com.app.ext.toJson

object App {
    lateinit var app: Application

    private const val KEY_WINDOW_INSETS = "KEY_WINDOW_INSETS"

    var windowInsets: Insets? = null
        private set

    suspend fun initWindowInsets(): Insets? {
        windowInsets?.let {
            return it
        }
        getObjData<Insets>(KEY_WINDOW_INSETS).let {
            windowInsets = it
            return it
        }
    }

    suspend fun setWindowInsets(value: Insets?) {
        windowInsets = value
        value?.let {
            putFlowData(KEY_WINDOW_INSETS, value.toJson())
        }
    }

    private var activityStartCount = 0

    fun register(
        application: Application,
        onFront: () -> Unit,
        onBack: () -> Unit,
    ) {
        // 也可以用ProcessLifecycleOwner去监听程序的前后台
        application.registerActivityLifecycleCallbacks(
            object :
                Application.ActivityLifecycleCallbacks {
                override fun onActivityPaused(activity: Activity) {
                }

                override fun onActivityResumed(activity: Activity) {
                }

                override fun onActivityDestroyed(activity: Activity) {
                    ActivityManager.pop(activity)
                }

                override fun onActivitySaveInstanceState(
                    activity: Activity,
                    outState: Bundle,
                ) {
                }

                override fun onActivityCreated(
                    activity: Activity,
                    savedInstanceState: Bundle?,
                ) {
                    ActivityManager.push(activity)
                }

                override fun onActivityStarted(activity: Activity) {
                    activityStartCount++
                    if (activityStartCount == 1) {
                        onFront()
                    }
                }

                override fun onActivityStopped(activity: Activity) {
                    activityStartCount--
                    if (activityStartCount == 0) {
                        onBack()
                    }
                }
            },
        )
    }
}
