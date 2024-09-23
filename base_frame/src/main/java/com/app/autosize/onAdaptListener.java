package com.app.autosize;

import android.app.Activity;

public interface onAdaptListener {

    void onAdaptBefore(Object target, Activity activity);

    void onAdaptAfter(Object target, Activity activity);
}
