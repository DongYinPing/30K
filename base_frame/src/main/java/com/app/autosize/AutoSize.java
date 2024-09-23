package com.app.autosize;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.SparseArray;

import com.app.autosize.external.ExternalAdaptInfo;
import com.app.autosize.internal.CustomAdapt;
import com.app.autosize.utils.AutoSizeLog;
import com.app.autosize.utils.Preconditions;

import java.util.Locale;

/**
 * 今日头条适配方案
 * https://gitcode.com/JessYanCoding/AndroidAutoSize.git
 */
public final class AutoSize {
    private static SparseArray<DisplayMetricsInfo> mCache = new SparseArray<>();
    private static final int MODE_SHIFT = 30;
    private static final int MODE_MASK = 0x3 << MODE_SHIFT;
    private static final int MODE_ON_WIDTH = 1 << MODE_SHIFT;
    private static final int MODE_DEVICE_SIZE = 2 << MODE_SHIFT;

    private AutoSize() {
        throw new IllegalStateException("you can't instantiate me!");
    }

    public static boolean checkInit() {
        return AutoSizeConfig.getInstance().getInitDensity() != -1;
    }

    public static void checkAndInit(Application application,boolean debug) {
        if (!checkInit()) {
            AutoSizeConfig.getInstance()
                    .setLog(debug)
                    .init(application)
                    .setUseDeviceSize(false);
        }
    }

    public static void autoConvertDensityOfGlobal(Activity activity) {
        if (AutoSizeConfig.getInstance().isBaseOnWidth()) {
            autoConvertDensityBaseOnWidth(activity, AutoSizeConfig.getInstance().getDesignWidthInDp());
        } else {
            autoConvertDensityBaseOnHeight(activity, AutoSizeConfig.getInstance().getDesignHeightInDp());
        }
    }

    public static void autoConvertDensityOfCustomAdapt(Activity activity, CustomAdapt customAdapt) {
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
        autoConvertDensity(activity, sizeInDp, customAdapt.isBaseOnWidth());
    }

    public static void autoConvertDensityOfExternalAdaptInfo(Activity activity, ExternalAdaptInfo externalAdaptInfo) {
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
        autoConvertDensity(activity, sizeInDp, externalAdaptInfo.isBaseOnWidth());
    }

    public static void autoConvertDensityBaseOnWidth(Activity activity, float designWidthInDp) {
        autoConvertDensity(activity, designWidthInDp, true);
    }

    public static void autoConvertDensityBaseOnHeight(Activity activity, float designHeightInDp) {
        autoConvertDensity(activity, designHeightInDp, false);
    }

    public static void autoConvertDensity(Activity activity, float sizeInDp, boolean isBaseOnWidth) {
        Preconditions.checkNotNull(activity, "activity == null");
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

        float targetDensity = 0;
        int targetDensityDpi = 0;
        float targetScaledDensity = 0;
        float targetXdpi = 0;
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
                        getInitScaledDensity() * 1.0f / AutoSizeConfig.getInstance().getInitDensity();
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

        setDensity(activity, targetDensity, targetDensityDpi, targetScaledDensity, targetXdpi);
        setScreenSizeDp(activity, targetScreenWidthDp, targetScreenHeightDp);

        AutoSizeLog.d(String.format(Locale.ENGLISH, "The %s has been adapted! \n%s Info: isBaseOnWidth = %s, %s = %f, %s = %f, targetDensity = %f, targetScaledDensity = %f, targetDensityDpi = %d, targetXdpi = %f, targetScreenWidthDp = %d, targetScreenHeightDp = %d"
                , activity.getClass().getName(), activity.getClass().getSimpleName(), isBaseOnWidth, isBaseOnWidth ? "designWidthInDp"
                        : "designHeightInDp", sizeInDp, isBaseOnWidth ? "designWidthInSubunits" : "designHeightInSubunits", subunitsDesignSize
                , targetDensity, targetScaledDensity, targetDensityDpi, targetXdpi, targetScreenWidthDp, targetScreenHeightDp));
    }

    public static void cancelAdapt(Activity activity) {
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
        setDensity(activity, AutoSizeConfig.getInstance().getInitDensity()
                , AutoSizeConfig.getInstance().getInitDensityDpi()
                , AutoSizeConfig.getInstance().getInitScaledDensity()
                , initXdpi);
        setScreenSizeDp(activity
                , AutoSizeConfig.getInstance().getInitScreenWidthDp()
                , AutoSizeConfig.getInstance().getInitScreenHeightDp());
    }

    public static void initCompatMultiProcess(Context context) {
        context.getContentResolver().query(Uri.parse("content://" + context.getPackageName() + ".autosize-init-provider"), null, null, null, null);
    }

    private static void setDensity(Activity activity, float density, int densityDpi, float scaledDensity, float xdpi) {
        DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        setDensity(activityDisplayMetrics, density, densityDpi, scaledDensity, xdpi);
        DisplayMetrics appDisplayMetrics = AutoSizeConfig.getInstance().getApplication().getResources().getDisplayMetrics();
        setDensity(appDisplayMetrics, density, densityDpi, scaledDensity, xdpi);

        //兼容 MIUI
        DisplayMetrics activityDisplayMetricsOnMIUI = getMetricsOnMiui(activity.getResources());
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

    private static void setScreenSizeDp(Activity activity, int screenWidthDp, int screenHeightDp) {
        if (AutoSizeConfig.getInstance().getUnitsManager().isSupportDP() && AutoSizeConfig.getInstance().getUnitsManager().isSupportScreenSizeDP()) {
            Configuration activityConfiguration = activity.getResources().getConfiguration();
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
