package com.example.taquio.trasearch.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Edward on 27/03/2018.
 */

public class ToSell implements Parcelable {
    String matname;

    public ToSell(){}
    public ToSell(String matname) {
        this.matname = matname;
    }

    protected ToSell(Parcel in) {
        matname = in.readString();
    }

    public static final Creator<ToSell> CREATOR = new Creator<ToSell>() {
        @Override
        public ToSell createFromParcel(Parcel in) {
            return new ToSell(in);
        }

        @Override
        public ToSell[] newArray(int size) {
            return new ToSell[size];
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
