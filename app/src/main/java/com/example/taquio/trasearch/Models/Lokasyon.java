package com.example.taquio.trasearch.Models;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Edward on 01/04/2018.
 */

public class Lokasyon implements Parcelable{

    Location loc;

    public Lokasyon(){}

    public Lokasyon(Location loc) {
        this.loc = loc;
    }

    protected Lokasyon(Parcel in) {
        loc = in.readParcelable(Location.class.getClassLoader());
    }

    public static final Creator<Lokasyon> CREATOR = new Creator<Lokasyon>() {
        @Override
        public Lokasyon createFromParcel(Parcel in) {
            return new Lokasyon(in);
        }

        @Override
        public Lokasyon[] newArray(int size) {
            return new Lokasyon[size];
        }
    };

    @Override
    public String toString() {
        return "Lokasyon{" +
                "loc=" + loc +
                '}';
    }

    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(loc, flags);
    }
}
