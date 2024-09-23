package com.app.autosize;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public class ActivityLifecycleCallbacksImpl implements Application.ActivityLifecycleCallbacks {

    private AutoAdaptStrategy mAutoAdaptStrategy;
    private FragmentLifecycleCallbacksImplToAndroidx mFragmentLifecycleCallbacksToAndroidx;

    public ActivityLifecycleCallbacksImpl(AutoAdaptStrategy autoAdaptStrategy) {
        if (AutoSizeConfig.DEPENDENCY_ANDROIDX) {
            mFragmentLifecycleCallbacksToAndroidx = new FragmentLifecycleCallbacksImplToAndroidx(autoAdaptStrategy);
        }
        mAutoAdaptStrategy = autoAdaptStrategy;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (AutoSizeConfig.getInstance().isCustomFragment()) {
            if (mFragmentLifecycleCallbacksToAndroidx != null && activity instanceof androidx.fragment.app.FragmentActivity) {
                ((androidx.fragment.app.FragmentActivity) activity).getSupportFragmentManager().registerFragmentLifecycleCallbacks(mFragmentLifecycleCallbacksToAndroidx, true);
            }
        }

        //Activity 中的 setContentView(View) 一定要在 super.onCreate(Bundle); 之后执行
        if (mAutoAdaptStrategy != null) {
            mAutoAdaptStrategy.applyAdapt(activity, activity);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (mAutoAdaptStrategy != null) {
            mAutoAdaptStrategy.applyAdapt(activity, activity);
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public void setAutoAdaptStrategy(AutoAdaptStrategy autoAdaptStrategy) {
        mAutoAdaptStrategy = autoAdaptStrategy;
        if (mFragmentLifecycleCallbacksToAndroidx != null) {
            mFragmentLifecycleCallbacksToAndroidx.setAutoAdaptStrategy(autoAdaptStrategy);
        }
    }
}
