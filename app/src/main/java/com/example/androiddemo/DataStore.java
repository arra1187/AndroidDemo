package com.example.androiddemo;

import android.os.Parcel;
import android.os.Parcelable;

public class DataStore implements Parcelable
{
    private String mFirst;
    private String mSecond;

    public DataStore(String one, String two)
    {
        mFirst = one;
        mSecond = one;
    }

    protected DataStore(Parcel in) {
        mFirst = in.readString();
        mSecond = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mFirst);
        dest.writeString(mSecond);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DataStore> CREATOR = new Creator<DataStore>() {
        @Override
        public DataStore createFromParcel(Parcel in) {
            return new DataStore(in);
        }

        @Override
        public DataStore[] newArray(int size) {
            return new DataStore[size];
        }
    };

    public String toString()
    {
        return mFirst + "::" + mSecond;
    }
}
