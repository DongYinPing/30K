package com.app.autosize.external;

import android.os.Parcel;
import android.os.Parcelable;

public class ExternalAdaptInfo implements Parcelable {
    private boolean isBaseOnWidth;

    private float sizeInDp;

    public ExternalAdaptInfo(boolean isBaseOnWidth) {
        this.isBaseOnWidth = isBaseOnWidth;
    }

    public ExternalAdaptInfo(boolean isBaseOnWidth, float sizeInDp) {
        this.isBaseOnWidth = isBaseOnWidth;
        this.sizeInDp = sizeInDp;
    }

    public boolean isBaseOnWidth() {
        return isBaseOnWidth;
    }

    public void setBaseOnWidth(boolean baseOnWidth) {
        isBaseOnWidth = baseOnWidth;
    }

    public float getSizeInDp() {
        return sizeInDp;
    }

    public void setSizeInDp(float sizeInDp) {
        this.sizeInDp = sizeInDp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isBaseOnWidth ? (byte) 1 : (byte) 0);
        dest.writeFloat(this.sizeInDp);
    }

    protected ExternalAdaptInfo(Parcel in) {
        this.isBaseOnWidth = in.readByte() != 0;
        this.sizeInDp = in.readFloat();
    }

    public static final Creator<ExternalAdaptInfo> CREATOR = new Creator<ExternalAdaptInfo>() {
        @Override
        public ExternalAdaptInfo createFromParcel(Parcel source) {
            return new ExternalAdaptInfo(source);
        }

        @Override
        public ExternalAdaptInfo[] newArray(int size) {
            return new ExternalAdaptInfo[size];
        }
    };

    @Override
    public String toString() {
        return "ExternalAdaptInfo{" + "isBaseOnWidth=" + isBaseOnWidth + ", sizeInDp=" + sizeInDp + '}';
    }
}
