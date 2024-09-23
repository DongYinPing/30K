package com.app.autosize;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.SparseArray;

import com.app.autosize.external.ExternalAdaptInfo;
import com.app.autosize.internal.CustomAdapt;
import com.app.autosize.utils.Preconditions;

public final class AutoSizeCompat {
    private static SparseArray<DisplayMetricsInfo> mCache = new SparseArray<>();
    private static final int MODE_SHIFT = 30;
    private static final int MODE_MASK = 0x3 << MODE_SHIFT;
    private static final int MODE_ON_WIDTH = 1 << MODE_SHIFT;
    private static final int MODE_DEVICE_SIZE = 2 << MODE_SHIFT;

    private AutoSizeCompat() {
        throw new IllegalStateException("you can't instantiate me!");
    }

    public static void autoConvertDensityOfGlobal(Resources resources) {
        if (AutoSizeConfig.getInstance().isBaseOnWidth()) {
            autoConvertDensityBaseOnWidth(resources, AutoSizeConfig.getInstance().getDesignWidthInDp());
        } else {
            autoConvertDensityBaseOnHeight(resources, AutoSizeConfig.getInstance().getDesignHeightInDp());
        }
    }

    public static void autoConvertDensityOfCustomAdapt(Resources resources, CustomAdapt customAdapt) {
        Preconditions.checkNotNull(customAdapt, "customAdapt == null");
        float sizeInDp = customAdapt.getSizeInDp();

        //如果 CustomAdapt#getSizeInDp() 返回 0, 则使用在 AndroidManifest 上填写的设计图尺寸
        if (sizeInDp <= 0) {
            if (customAdapt.isBaseOnWidth()) {
                sizeInDp = AutoSizeConfig.getInstance().getDesignWidthInDp();
            } else {
                sizeInDp = AutoSizeConfig.getInstance().getDesignHeightInDp();
            }
        }
        autoConvertDensity(resources, sizeInDp, customAdapt.isBaseOnWidth());
    }

    public static void autoConvertDensityOfExternalAdaptInfo(Resources resources, ExternalAdaptInfo externalAdaptInfo) {
        Preconditions.checkNotNull(externalAdaptInfo, "externalAdaptInfo == null");
        float sizeInDp = externalAdaptInfo.getSizeInDp();

        //如果 ExternalAdaptInfo#getSizeInDp() 返回 0, 则使用在 AndroidManifest 上填写的设计图尺寸
        if (sizeInDp <= 0) {
            if (externalAdaptInfo.isBaseOnWidth()) {
                sizeInDp = AutoSizeConfig.getInstance().getDesignWidthInDp();
            } else {
                sizeInDp = AutoSizeConfig.getInstance().getDesignHeightInDp();
            }
        }
        autoConvertDensity(resources, sizeInDp, externalAdaptInfo.isBaseOnWidth());
    }

    public static void autoConvertDensityBaseOnWidth(Resources resources, float designWidthInDp) {
        autoConvertDensity(resources, designWidthInDp, true);
    }

    public static void autoConvertDensityBaseOnHeight(Resources resources, float designHeightInDp) {
        autoConvertDensity(resources, designHeightInDp, false);
    }

    public static void autoConvertDensity(Resources resources, float sizeInDp, boolean isBaseOnWidth) {
        Preconditions.checkNotNull(resources, "resources == null");
        Preconditions.checkMainThread();

        float subunitsDesignSize = isBaseOnWidth ? AutoSizeConfig.getInstance().getUnitsManager().getDesignWidth()
                : AutoSizeConfig.getInstance().getUnitsManager().getDesignHeight();
        subunitsDesignSize = subunitsDesignSize > 0 ? subunitsDesignSize : sizeInDp;

        int screenSize = isBaseOnWidth ? AutoSizeConfig.getInstance().getScreenWidth()
                : AutoSizeConfig.getInstance().getScreenHeight();

        int key = Math.round((sizeInDp + subunitsDesignSize + screenSize) * AutoSizeConfig.getInstance().getInitScaledDensity()) & ~MODE_MASK;
        key = isBaseOnWidth ? (key | MODE_ON_WIDTH) : (key & ~MODE_ON_WIDTH);
        key = AutoSizeConfig.getInstance().isUseDeviceSize() ? (key | MODE_DEVICE_SIZE) : (key & ~MODE_DEVICE_SIZE);

        DisplayMetricsInfo displayMetricsInfo = mCache.get(key);

        float targetDensity;
        int targetDensityDpi;
        float targetScaledDensity;
        float targetXdpi;
        int targetScreenWidthDp;
        int targetScreenHeightDp;

        if (displayMetricsInfo == null) {
            if (isBaseOnWidth) {
                targetDensity = AutoSizeConfig.getInstance().getScreenWidth() * 1.0f / sizeInDp;
            } else {
                targetDensity = AutoSizeConfig.getInstance().getScreenHeight() * 1.0f / sizeInDp;
            }
            if (AutoSizeConfig.getInstance().getPrivateFontScale() > 0) {
                targetScaledDensity = targetDensity * AutoSizeConfig.getInstance().getPrivateFontScale();
            } else {
                float systemFontScale = AutoSizeConfig.getInstance().isExcludeFontScale() ? 1 : AutoSizeConfig.getInstance().
                        getInitScaledDensity() / AutoSizeConfig.getInstance().getInitDensity();
                targetScaledDensity = targetDensity * systemFontScale;
            }
            targetDensityDpi = (int) (targetDensity * 160);

            targetScreenWidthDp = (int) (AutoSizeConfig.getInstance().getScreenWidth() / targetDensity);
            targetScreenHeightDp = (int) (AutoSizeConfig.getInstance().getScreenHeight() / targetDensity);

            if (isBaseOnWidth) {
                targetXdpi = AutoSizeConfig.getInstance().getScreenWidth() * 1.0f / subunitsDesignSize;
            } else {
                targetXdpi = AutoSizeConfig.getInstance().getScreenHeight() * 1.0f / subunitsDesignSize;
            }

            mCache.put(key, new DisplayMetricsInfo(targetDensity, targetDensityDpi, targetScaledDensity, targetXdpi, targetScreenWidthDp, targetScreenHeightDp));
        } else {
            targetDensity = displayMetricsInfo.getDensity();
            targetDensityDpi = displayMetricsInfo.getDensityDpi();
            targetScaledDensity = displayMetricsInfo.getScaledDensity();
            targetXdpi = displayMetricsInfo.getXdpi();
            targetScreenWidthDp = displayMetricsInfo.getScreenWidthDp();
            targetScreenHeightDp = displayMetricsInfo.getScreenHeightDp();
        }

        setDensity(resources, targetDensity, targetDensityDpi, targetScaledDensity, targetXdpi);
        setScreenSizeDp(resources, targetScreenWidthDp, targetScreenHeightDp);
    }

    public static void cancelAdapt(Resources resources) {
        Preconditions.checkMainThread();
        float initXdpi = AutoSizeConfig.getInstance().getInitXdpi();
        switch (AutoSizeConfig.getInstance().getUnitsManager().getSupportSubunits()) {
            case PT:
                initXdpi = initXdpi / 72f;
                break;
            case MM:
                initXdpi = initXdpi / 25.4f;
                break;
            default:
        }
        setDensity(resources, AutoSizeConfig.getInstance().getInitDensity()
                , AutoSizeConfig.getInstance().getInitDensityDpi()
                , AutoSizeConfig.getInstance().getInitScaledDensity()
                , initXdpi);
        setScreenSizeDp(resources
                , AutoSizeConfig.getInstance().getInitScreenWidthDp()
                , AutoSizeConfig.getInstance().getInitScreenHeightDp());
    }

    private static void setDensity(Resources resources, float density, int densityDpi, float scaledDensity, float xdpi) {
        DisplayMetrics activityDisplayMetrics = resources.getDisplayMetrics();
        setDensity(activityDisplayMetrics, density, densityDpi, scaledDensity, xdpi);
        DisplayMetrics appDisplayMetrics = AutoSizeConfig.getInstance().getApplication().getResources().getDisplayMetrics();
        setDensity(appDisplayMetrics, density, densityDpi, scaledDensity, xdpi);

        DisplayMetrics activityDisplayMetricsOnMIUI = getMetricsOnMiui(resources);
        DisplayMetrics appDisplayMetricsOnMIUI = getMetricsOnMiui(AutoSizeConfig.getInstance().getApplication().getResources());

        if (activityDisplayMetricsOnMIUI != null) {
            setDensity(activityDisplayMetricsOnMIUI, density, densityDpi, scaledDensity, xdpi);
        }
        if (appDisplayMetricsOnMIUI != null) {
            setDensity(appDisplayMetricsOnMIUI, density, densityDpi, scaledDensity, xdpi);
        }
    }

    private static void setDensity(DisplayMetrics displayMetrics, float density, int densityDpi, float scaledDensity, float xdpi) {
        if (AutoSizeConfig.getInstance().getUnitsManager().isSupportDP()) {
            displayMetrics.density = density;
            displayMetrics.densityDpi = densityDpi;
        }
        if (AutoSizeConfig.getInstance().getUnitsManager().isSupportSP()) {
            displayMetrics.scaledDensity = scaledDensity;
        }
        switch (AutoSizeConfig.getInstance().getUnitsManager().getSupportSubunits()) {
            case NONE:
                break;
            case PT:
                displayMetrics.xdpi = xdpi * 72f;
                break;
            case IN:
                displayMetrics.xdpi = xdpi;
                break;
            case MM:
                displayMetrics.xdpi = xdpi * 25.4f;
                break;
            default:
        }
    }

    private static void setScreenSizeDp(Resources resources, int screenWidthDp, int screenHeightDp) {
        if (AutoSizeConfig.getInstance().getUnitsManager().isSupportDP() && AutoSizeConfig.getInstance().getUnitsManager().isSupportScreenSizeDP()) {
            Configuration activityConfiguration = resources.getConfiguration();
            setScreenSizeDp(activityConfiguration, screenWidthDp, screenHeightDp);

            Configuration appConfiguration = AutoSizeConfig.getInstance().getApplication().getResources().getConfiguration();
            setScreenSizeDp(appConfiguration, screenWidthDp, screenHeightDp);
        }
    }

    private static void setScreenSizeDp(Configuration configuration, int screenWidthDp, int screenHeightDp) {
        configuration.screenWidthDp = screenWidthDp;
        configuration.screenHeightDp = screenHeightDp;
    }

    private static DisplayMetrics getMetricsOnMiui(Resources resources) {
        if (AutoSizeConfig.getInstance().isMiui() && AutoSizeConfig.getInstance().getTmpMetricsField() != null) {
            try {
                return (DisplayMetrics) AutoSizeConfig.getInstance().getTmpMetricsField().get(resources);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
