package com.app.application

import android.app.Activity
import android.os.Build
import coil.Coil
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.alibaba.android.arouter.launcher.ARouter
import com.app.app.BaseApplication
import com.app.autosize.AutoSize
import com.app.autosize.AutoSizeConfig
import com.app.autosize.onAdaptListener
import com.app.autosize.utils.ScreenUtils
import com.app.main.BuildConfig

/**
 * MyApplication
 */
class MyApplication : BaseApplication() {
    override fun initOnMainProcess() {
        //  AutoSize init
        AutoSize.checkAndInit(this, BuildConfig.DEBUG)
        AutoSizeConfig.getInstance()
            .setOnAdaptListener(
                object : onAdaptListener {
                    override fun onAdaptBefore(
                        target: Any?,
                        activity: Activity,
                    ) {
                        AutoSizeConfig.getInstance()
                            .setScreenWidth(ScreenUtils.getScreenSize(activity)[0])
                    }

                    override fun onAdaptAfter(
                        target: Any?,
                        activity: Activity,
                    ) {
                    }
                },
            )

        // ARouter init
        if (BuildConfig.DEBUG) {
            // 开启打印日志
            ARouter.openLog()
            // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openDebug()
        }
        ARouter.init(this)

        // Coil init
        val imageLoader =
            ImageLoader.Builder(applicationContext).componentRegistry {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder(applicationContext))
                } else {
                    add(GifDecoder())
                }
            }.build()
        Coil.setImageLoader(imageLoader)
    }

    override fun onFront() {
    }

    override fun onBack() {
    }
}
