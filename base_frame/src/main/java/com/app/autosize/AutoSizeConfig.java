package com.app.autosize;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.app.autosize.external.ExternalAdaptManager;
import com.app.autosize.unit.Subunits;
import com.app.autosize.unit.UnitsManager;
import com.app.autosize.utils.AutoSizeLog;
import com.app.autosize.utils.Preconditions;
import com.app.autosize.utils.ScreenUtils;

import java.lang.reflect.Field;

public final class AutoSizeConfig {
    private static volatile AutoSizeConfig sInstance;
    private static final String KEY_DESIGN_WIDTH_IN_DP = "design_width_in_dp";
    private static final String KEY_DESIGN_HEIGHT_IN_DP = "design_height_in_dp";
    public static final boolean DEPENDENCY_ANDROIDX;
    public static final boolean DEPENDENCY_SUPPORT;
    private Application mApplication;
    private ExternalAdaptManager mExternalAdaptManager = new ExternalAdaptManager();
    private UnitsManager mUnitsManager = new UnitsManager();
    private float mInitDensity = -1;

    private int mInitDensityDpi;

    private float mInitScaledDensity;

    private float mInitXdpi;

    private int mInitScreenWidthDp;

    private int mInitScreenHeightDp;

    private int mDesignWidthInDp;

    private int mDesignHeightInDp;

    private int mScreenWidth;

    private int mScreenHeight;

    private int mStatusBarHeight;

    private boolean isBaseOnWidth = true;

    private boolean isUseDeviceSize = true;

    private ActivityLifecycleCallbacksImpl mActivityLifecycleCallbacks;

    private boolean isStop;

    private boolean isCustomFragment;

    private boolean isVertical;

    private boolean isExcludeFontScale;

    private float privateFontScale;

    private boolean isMiui;

    private Field mTmpMetricsField;

    private onAdaptListener mOnAdaptListener;

    static {
        DEPENDENCY_ANDROIDX = findClassByClassName("androidx.fragment.app.FragmentActivity");
        DEPENDENCY_SUPPORT = findClassByClassName("android.support.v4.app.FragmentActivity");
    }

    private static boolean findClassByClassName(String className) {
        boolean hasDependency;
        try {
            Class.forName(className);
            hasDependency = true;
        } catch (ClassNotFoundException e) {
            hasDependency = false;
        }
        return hasDependency;
    }

    public static AutoSizeConfig getInstance() {
        if (sInstance == null) {
            synchronized (AutoSizeConfig.class) {
                if (sInstance == null) {
                    sInstance = new AutoSizeConfig();
                }
            }
        }
        return sInstance;
    }

    private AutoSizeConfig() {
    }

    public Application getApplication() {
        Preconditions.checkNotNull(mApplication, "Please call the AutoSizeConfig#init() first");
        return mApplication;
    }

    public AutoSizeConfig init(Application application) {
        return init(application, true, null);
    }

    public AutoSizeConfig init(Application application, boolean isBaseOnWidth) {
        return init(application, isBaseOnWidth, null);
    }

    public AutoSizeConfig init(final Application application, boolean isBaseOnWidth, AutoAdaptStrategy strategy) {
        Preconditions.checkArgument(mInitDensity == -1, "AutoSizeConfig#init() can only be called once");
        Preconditions.checkNotNull(application, "application == null");
        this.mApplication = application;
        this.isBaseOnWidth = isBaseOnWidth;
        final DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        final Configuration configuration = Resources.getSystem().getConfiguration();

        if (AutoSizeConfig.getInstance().getUnitsManager().getSupportSubunits() == Subunits.NONE) {
            mDesignWidthInDp = 375;
            mDesignHeightInDp = 812;
        }

        getMetaData(application);
        isVertical = application.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        int[] screenSize = ScreenUtils.getScreenSize(application);
        mScreenWidth = Math.min(screenSize[0], screenSize[1]);
        mScreenHeight = Math.max(screenSize[0], screenSize[1]);
        mStatusBarHeight = ScreenUtils.getStatusBarHeight();
        AutoSizeLog.d("designWidthInDp = " + mDesignWidthInDp + ", designHeightInDp = " + mDesignHeightInDp + ", screenWidth = " + mScreenWidth + ", screenHeight = " + mScreenHeight);

        mInitDensity = displayMetrics.density;
        mInitDensityDpi = displayMetrics.densityDpi;
        mInitScaledDensity = displayMetrics.scaledDensity;
        mInitXdpi = displayMetrics.xdpi;
        mInitScreenWidthDp = Math.min(configuration.screenWidthDp, configuration.screenHeightDp);
        mInitScreenHeightDp = Math.max(configuration.screenWidthDp, configuration.screenHeightDp);
        application.registerComponentCallbacks(new ComponentCallbacks() {
            @Override
            public void onConfigurationChanged(Configuration newConfig) {
                if (newConfig != null) {
                    if (newConfig.fontScale > 0) {
                        mInitScaledDensity = Resources.getSystem().getDisplayMetrics().scaledDensity;
                        AutoSizeLog.d("initScaledDensity = " + mInitScaledDensity + " on ConfigurationChanged");
                    }
                    isVertical = newConfig.orientation == Configuration.ORIENTATION_PORTRAIT;
                    int[] screenSize = ScreenUtils.getScreenSize(application);
                    mScreenWidth = Math.min(screenSize[0], screenSize[1]);
                    mScreenHeight = Math.max(screenSize[0], screenSize[1]);
                }
            }

            @Override
            public void onLowMemory() {

            }
        });
        AutoSizeLog.d("initDensity = " + mInitDensity + ", initScaledDensity = " + mInitScaledDensity);
        mActivityLifecycleCallbacks = new ActivityLifecycleCallbacksImpl(new WrapperAutoAdaptStrategy(strategy == null ? new DefaultAutoAdaptStrategy() : strategy));
        application.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
        if ("MiuiResources".equals(application.getResources().getClass().getSimpleName()) || "XResources".equals(application.getResources().getClass().getSimpleName())) {
            isMiui = true;
            try {
                mTmpMetricsField = Resources.class.getDeclaredField("mTmpMetrics");
                mTmpMetricsField.setAccessible(true);
            } catch (Exception e) {
                mTmpMetricsField = null;
            }
        }
        return this;
    }

    public void restart() {
        Preconditions.checkNotNull(mActivityLifecycleCallbacks, "Please call the AutoSizeConfig#init() first");
        synchronized (AutoSizeConfig.class) {
            if (isStop) {
                mApplication.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
                isStop = false;
            }
        }
    }

    public void stop(Activity activity) {
        Preconditions.checkNotNull(mActivityLifecycleCallbacks, "Please call the AutoSizeConfig#init() first");
        synchronized (AutoSizeConfig.class) {
            if (!isStop) {
                mApplication.unregisterActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
                AutoSize.cancelAdapt(activity);
                isStop = true;
            }
        }
    }

    public AutoSizeConfig setAutoAdaptStrategy(AutoAdaptStrategy autoAdaptStrategy) {
        Preconditions.checkNotNull(autoAdaptStrategy, "autoAdaptStrategy == null");
        Preconditions.checkNotNull(mActivityLifecycleCallbacks, "Please call the AutoSizeConfig#init() first");
        mActivityLifecycleCallbacks.setAutoAdaptStrategy(new WrapperAutoAdaptStrategy(autoAdaptStrategy));
        return this;
    }

    public AutoSizeConfig setOnAdaptListener(onAdaptListener onAdaptListener) {
        Preconditions.checkNotNull(onAdaptListener, "onAdaptListener == null");
        mOnAdaptListener = onAdaptListener;
        return this;
    }

    public AutoSizeConfig setBaseOnWidth(boolean baseOnWidth) {
        isBaseOnWidth = baseOnWidth;
        return this;
    }

    public AutoSizeConfig setUseDeviceSize(boolean useDeviceSize) {
        isUseDeviceSize = useDeviceSize;
        return this;
    }

    public AutoSizeConfig setLog(boolean log) {
        AutoSizeLog.setDebug(log);
        return this;
    }

    public AutoSizeConfig setCustomFragment(boolean customFragment) {
        isCustomFragment = customFragment;
        return this;
    }

    public boolean isCustomFragment() {
        return isCustomFragment;
    }

    public boolean isStop() {
        return isStop;
    }

    public ExternalAdaptManager getExternalAdaptManager() {
        return mExternalAdaptManager;
    }

    public UnitsManager getUnitsManager() {
        return mUnitsManager;
    }

    public onAdaptListener getOnAdaptListener() {
        return mOnAdaptListener;
    }

    public boolean isBaseOnWidth() {
        return isBaseOnWidth;
    }

    public boolean isUseDeviceSize() {
        return isUseDeviceSize;
    }

    public int getScreenWidth() {
        return mScreenWidth;
    }

    public int getScreenHeight() {
        return isUseDeviceSize() ? mScreenHeight : mScreenHeight - mStatusBarHeight;
    }

    public int getDesignWidthInDp() {
        Preconditions.checkArgument(mDesignWidthInDp > 0, "you must set " + KEY_DESIGN_WIDTH_IN_DP + "  in your AndroidManifest file");
        return mDesignWidthInDp;
    }

    public int getDesignHeightInDp() {
        Preconditions.checkArgument(mDesignHeightInDp > 0, "you must set " + KEY_DESIGN_HEIGHT_IN_DP + "  in your AndroidManifest file");
        return mDesignHeightInDp;
    }

    public float getInitDensity() {
        return mInitDensity;
    }

    public int getInitDensityDpi() {
        return mInitDensityDpi;
    }

    public float getInitScaledDensity() {
        return mInitScaledDensity;
    }

    public float getInitXdpi() {
        return mInitXdpi;
    }

    public int getInitScreenWidthDp() {
        return mInitScreenWidthDp;
    }

    public int getInitScreenHeightDp() {
        return mInitScreenHeightDp;
    }

    public boolean isVertical() {
        return isVertical;
    }

    public boolean isMiui() {
        return isMiui;
    }

    public Field getTmpMetricsField() {
        return mTmpMetricsField;
    }

    public AutoSizeConfig setVertical(boolean vertical) {
        isVertical = vertical;
        return this;
    }

    public boolean isExcludeFontScale() {
        return isExcludeFontScale;
    }

    public AutoSizeConfig setExcludeFontScale(boolean excludeFontScale) {
        isExcludeFontScale = excludeFontScale;
        return this;
    }

    public AutoSizeConfig setPrivateFontScale(float fontScale) {
        privateFontScale = fontScale;
        return this;
    }

    public float getPrivateFontScale() {
        return privateFontScale;
    }

    public AutoSizeConfig setScreenWidth(int screenWidth) {
        Preconditions.checkArgument(screenWidth > 0, "screenWidth must be > 0");
        mScreenWidth = screenWidth;
        return this;
    }

    public AutoSizeConfig setScreenHeight(int screenHeight) {
        Preconditions.checkArgument(screenHeight > 0, "screenHeight must be > 0");
        mScreenHeight = screenHeight;
        return this;
    }

    public AutoSizeConfig setDesignWidthInDp(int designWidthInDp) {
        Preconditions.checkArgument(designWidthInDp > 0, "designWidthInDp must be > 0");
        mDesignWidthInDp = designWidthInDp;
        return this;
    }

    public AutoSizeConfig setDesignHeightInDp(int designHeightInDp) {
        Preconditions.checkArgument(designHeightInDp > 0, "designHeightInDp must be > 0");
        mDesignHeightInDp = designHeightInDp;
        return this;
    }

    public AutoSizeConfig setStatusBarHeight(int statusBarHeight) {
        Preconditions.checkArgument(statusBarHeight > 0, "statusBarHeight must be > 0");
        mStatusBarHeight = statusBarHeight;
        return this;
    }

    private void getMetaData(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PackageManager packageManager = context.getPackageManager();
                ApplicationInfo applicationInfo;
                try {
                    applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                    if (applicationInfo != null && applicationInfo.metaData != null) {
                        if (applicationInfo.metaData.containsKey(KEY_DESIGN_WIDTH_IN_DP)) {
                            mDesignWidthInDp = (int) applicationInfo.metaData.get(KEY_DESIGN_WIDTH_IN_DP);
                        }
                        if (applicationInfo.metaData.containsKey(KEY_DESIGN_HEIGHT_IN_DP)) {
                            mDesignHeightInDp = (int) applicationInfo.metaData.get(KEY_DESIGN_HEIGHT_IN_DP);
                        }
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
