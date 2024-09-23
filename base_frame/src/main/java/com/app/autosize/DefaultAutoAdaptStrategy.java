package com.app.autosize;

import android.app.Activity;

import com.app.autosize.external.ExternalAdaptInfo;
import com.app.autosize.internal.CancelAdapt;
import com.app.autosize.internal.CustomAdapt;
import com.app.autosize.utils.AutoSizeLog;

import java.util.Locale;

public class DefaultAutoAdaptStrategy implements AutoAdaptStrategy {
    @Override
    public void applyAdapt(Object target, Activity activity) {

        if (AutoSizeConfig.getInstance().getExternalAdaptManager().isRun()) {
            if (AutoSizeConfig.getInstance().getExternalAdaptManager().isCancelAdapt(target.getClass())) {
                AutoSizeLog.w(String.format(Locale.ENGLISH, "%s canceled the adaptation!", target.getClass().getName()));
                AutoSize.cancelAdapt(activity);
                return;
            } else {
                ExternalAdaptInfo info = AutoSizeConfig.getInstance().getExternalAdaptManager()
                        .getExternalAdaptInfoOfActivity(target.getClass());
                if (info != null) {
                    AutoSizeLog.d(String.format(Locale.ENGLISH, "%s used %s for adaptation!", target.getClass().getName(), ExternalAdaptInfo.class.getName()));
                    AutoSize.autoConvertDensityOfExternalAdaptInfo(activity, info);
                    return;
                }
            }
        }

        if (target instanceof CancelAdapt) {
            AutoSizeLog.w(String.format(Locale.ENGLISH, "%s canceled the adaptation!", target.getClass().getName()));
            AutoSize.cancelAdapt(activity);
            return;
        }

        if (target instanceof CustomAdapt) {
            AutoSizeLog.d(String.format(Locale.ENGLISH, "%s implemented by %s!", target.getClass().getName(), CustomAdapt.class.getName()));
            AutoSize.autoConvertDensityOfCustomAdapt(activity, (CustomAdapt) target);
        } else {
            AutoSizeLog.d(String.format(Locale.ENGLISH, "%s used the global configuration.", target.getClass().getName()));
            AutoSize.autoConvertDensityOfGlobal(activity);
        }
    }
}
