package com.example.taquio.trasearch.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Edward on 23/03/2018.
 */

public class Bookmark implements Parcelable {

    private String photo_id, user_id;

    public Bookmark(String photo_id, String uid){}

    protected Bookmark(Parcel in) {
        photo_id = in.readString();
        user_id = in.readString();
    }

    public static final Creator<Bookmark> CREATOR = new Creator<Bookmark>() {
        @Override
        public Bookmark createFromParcel(Parcel in) {
            return new Bookmark(in);
        }

        @Override
        public Bookmark[] newArray(int size) {
            return new Bookmark[size];
        }
    };

    @Override
    public String toString() {
        return "Bookmark{" +
                "photo_id='" + photo_id + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }

    public String getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(photo_id);
        parcel.writeString(user_id);
    }
}
