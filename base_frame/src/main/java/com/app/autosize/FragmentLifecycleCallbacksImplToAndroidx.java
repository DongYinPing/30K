package com.app.autosize;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class FragmentLifecycleCallbacksImplToAndroidx extends FragmentManager.FragmentLifecycleCallbacks {

    private AutoAdaptStrategy mAutoAdaptStrategy;

    public FragmentLifecycleCallbacksImplToAndroidx(AutoAdaptStrategy autoAdaptStrategy) {
        mAutoAdaptStrategy = autoAdaptStrategy;
    }

    @Override
    public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
        if (mAutoAdaptStrategy != null) {
            mAutoAdaptStrategy.applyAdapt(f, f.getActivity());
        }
    }

    public void setAutoAdaptStrategy(AutoAdaptStrategy autoAdaptStrategy) {
        mAutoAdaptStrategy = autoAdaptStrategy;
    }
}
