package com.zhl.aptintent;

import android.os.Parcel;
import android.os.Parcelable;


public class ObjectTest implements Parcelable {

    public String name = "I am object";

    public ObjectTest() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    protected ObjectTest(Parcel in) {
        this.name = in.readString();
    }

    public static final Creator<ObjectTest> CREATOR = new Creator<ObjectTest>() {
        @Override
        public ObjectTest createFromParcel(Parcel source) {
            return new ObjectTest(source);
        }

        @Override
        public ObjectTest[] newArray(int size) {
            return new ObjectTest[size];
        }
    };
}
