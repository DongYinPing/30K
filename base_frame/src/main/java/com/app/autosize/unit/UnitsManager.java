package com.app.autosize.unit;

import com.app.autosize.utils.Preconditions;

public class UnitsManager {

    private float mDesignWidth;

    private float mDesignHeight;

    private boolean isSupportDP = true;

    private boolean isSupportSP = true;

    private Subunits mSupportSubunits = Subunits.NONE;

    private boolean isSupportScreenSizeDP = false;

    public UnitsManager setDesignSize(float designWidth, float designHeight) {
        setDesignWidth(designWidth);
        setDesignHeight(designHeight);
        return this;
    }

    public float getDesignWidth() {
        return mDesignWidth;
    }


    public UnitsManager setDesignWidth(float designWidth) {
        Preconditions.checkArgument(designWidth > 0, "designWidth must be > 0");
        mDesignWidth = designWidth;
        return this;
    }

    public float getDesignHeight() {
        return mDesignHeight;
    }

    public UnitsManager setDesignHeight(float designHeight) {
        Preconditions.checkArgument(designHeight > 0, "designHeight must be > 0");
        mDesignHeight = designHeight;
        return this;
    }

    public boolean isSupportDP() {
        return isSupportDP;
    }


    public UnitsManager setSupportDP(boolean supportDP) {
        isSupportDP = supportDP;
        return this;
    }

    public boolean isSupportSP() {
        return isSupportSP;
    }


    public UnitsManager setSupportSP(boolean supportSP) {
        isSupportSP = supportSP;
        return this;
    }

    public Subunits getSupportSubunits() {
        return mSupportSubunits;
    }

    public boolean isSupportScreenSizeDP() {
        return isSupportScreenSizeDP;
    }

    public UnitsManager setSupportScreenSizeDP(boolean supportScreenSizeDP) {
        isSupportScreenSizeDP = supportScreenSizeDP;
        return this;
    }

    public UnitsManager setSupportSubunits(Subunits supportSubunits) {
        mSupportSubunits = Preconditions.checkNotNull(supportSubunits,
                "The supportSubunits can not be null, use Subunits.NONE instead");
        return this;
    }
}
