package com.app.autosize.external;

import com.app.autosize.utils.Preconditions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExternalAdaptManager {
    private List<String> mCancelAdaptList;
    private Map<String, ExternalAdaptInfo> mExternalAdaptInfos;
    private boolean isRun;

    public synchronized ExternalAdaptManager addCancelAdaptOfActivity(Class<?> targetClass) {
        Preconditions.checkNotNull(targetClass, "targetClass == null");
        if (!isRun) {
            isRun = true;
        }
        if (mCancelAdaptList == null) {
            mCancelAdaptList = new ArrayList<>();
        }
        mCancelAdaptList.add(targetClass.getCanonicalName());
        return this;
    }

    public synchronized ExternalAdaptManager addExternalAdaptInfoOfActivity(Class<?> targetClass, ExternalAdaptInfo info) {
        Preconditions.checkNotNull(targetClass, "targetClass == null");
        if (!isRun) {
            isRun = true;
        }
        if (mExternalAdaptInfos == null) {
            mExternalAdaptInfos = new HashMap<>(16);
        }
        mExternalAdaptInfos.put(targetClass.getCanonicalName(), info);
        return this;
    }

    public synchronized boolean isCancelAdapt(Class<?> targetClass) {
        Preconditions.checkNotNull(targetClass, "targetClass == null");
        if (mCancelAdaptList == null) {
            return false;
        }
        return mCancelAdaptList.contains(targetClass.getCanonicalName());
    }

    public synchronized ExternalAdaptInfo getExternalAdaptInfoOfActivity(Class<?> targetClass) {
        Preconditions.checkNotNull(targetClass, "targetClass == null");
        if (mExternalAdaptInfos == null) {
            return null;
        }
        return mExternalAdaptInfos.get(targetClass.getCanonicalName());
    }

    public boolean isRun() {
        return isRun;
    }

    public ExternalAdaptManager setRun(boolean run) {
        isRun = run;
        return this;
    }
}
