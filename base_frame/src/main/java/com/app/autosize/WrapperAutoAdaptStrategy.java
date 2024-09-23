package com.app.autosize;

import android.app.Activity;

public class WrapperAutoAdaptStrategy implements AutoAdaptStrategy {
    private final AutoAdaptStrategy mAutoAdaptStrategy;

    public WrapperAutoAdaptStrategy(AutoAdaptStrategy autoAdaptStrategy) {
        mAutoAdaptStrategy = autoAdaptStrategy;
    }

    @Override
    public void applyAdapt(Object target, Activity activity) {
        onAdaptListener onAdaptListener = AutoSizeConfig.getInstance().getOnAdaptListener();
        if (onAdaptListener != null) {
            onAdaptListener.onAdaptBefore(target, activity);
        }
        if (mAutoAdaptStrategy != null) {
            mAutoAdaptStrategy.applyAdapt(target, activity);
        }
        if (onAdaptListener != null) {
            onAdaptListener.onAdaptAfter(target, activity);
        }
    }
}
