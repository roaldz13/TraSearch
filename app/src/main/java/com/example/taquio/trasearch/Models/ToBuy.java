package com.example.taquio.trasearch.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Edward on 27/03/2018.
 */

public class ToBuy implements Parcelable {
    String matname;

    public ToBuy(){}
    public ToBuy(String matname) {
        this.matname = matname;
    }

    protected ToBuy(Parcel in) {
        matname = in.readString();
    }

    public static final Creator<ToBuy> CREATOR = new Creator<ToBuy>() {
        @Override
        public ToBuy createFromParcel(Parcel in) {
            return new ToBuy(in);
        }

        @Override
        public ToBuy[] newArray(int size) {
            return new ToBuy[size];
        }
    };

    @Override
    public String toString() {
        return "ToBuy{" +
                "matname='" + matname + '\'' +
                '}';
    }

    public String getMatname() {
        return matname;
    }

    public void setMatname(String matname) {
        this.matname = matname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(matname);
    }
}
