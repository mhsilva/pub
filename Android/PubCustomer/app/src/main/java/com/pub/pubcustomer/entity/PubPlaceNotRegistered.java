package com.pub.pubcustomer.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Fernando Santiago on 20/08/2016.
 */
public class PubPlaceNotRegistered implements Parcelable {

    private String id;
    private String name;
    private String address;
    private String phoneNumber;
    private String webSiteUri;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWebSiteUri() {
        return webSiteUri;
    }

    public void setWebSiteUri(String webSiteUri) {
        this.webSiteUri = webSiteUri;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.address);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.webSiteUri);
    }

    public PubPlaceNotRegistered() {
    }

    protected PubPlaceNotRegistered(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.address = in.readString();
        this.phoneNumber = in.readString();
        this.webSiteUri = in.readString();
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