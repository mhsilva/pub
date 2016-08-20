package com.pub.pubcustomer.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Fernando Santiago on 20/08/2016.
 */
public class PubPlaceNotRegistered implements Parcelable {

    private  String id;
    private  String name;

    public PubPlaceNotRegistered(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
    }

    public PubPlaceNotRegistered() {
    }

    protected PubPlaceNotRegistered(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
    }

    public static final Creator<PubPlaceNotRegistered> CREATOR = new Creator<PubPlaceNotRegistered>() {
        @Override
        public PubPlaceNotRegistered createFromParcel(Parcel source) {
            return new PubPlaceNotRegistered(source);
        }

        @Override
        public PubPlaceNotRegistered[] newArray(int size) {
            return new PubPlaceNotRegistered[size];
        }
    };
}