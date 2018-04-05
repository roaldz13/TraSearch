package com.example.taquio.trasearch.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.List;
import java.util.Map;

/**
 * Created by Edward 2018.
 */

public class Photo implements Parcelable {

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
    private String photo_description;
    private Long date_created;
    private String image_path;
    private String photo_id;
    private String user_id;
    private String quantity;
    public Photo() {
    }

    public Photo(String photo_description, Long date_created, String image_path, String photo_id,
                 String user_id, String quantity) {
        this.photo_description = photo_description;
        this.date_created = date_created;
        this.image_path = image_path;
        this.photo_id = photo_id;
        this.user_id = user_id;
        this.quantity = quantity;
    }

    protected Photo(Parcel in) {
        photo_description = in.readString();
        date_created = Long.parseLong(in.readString());
        image_path = in.readString();
        photo_id = in.readString();
        user_id = in.readString();
        quantity = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(photo_description);
        dest.writeString(String.valueOf(date_created));
        dest.writeString(image_path);
        dest.writeString(photo_id);
        dest.writeString(user_id);
        dest.writeString(quantity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getPhoto_description() {
        return photo_description;
    }

    public void setPhoto_description(String photo_description) {
        this.photo_description = photo_description;
    }

//    public long getDate_created() {
//        return date_created;
//    }
    public java.util.Map<String, String> getDate_created() {
        return ServerValue.TIMESTAMP;
    }
    @Exclude
    public Long getDate_createdLong() {
        return date_created;
    }
//    public void setDate_created(long date_created) {
//        this.date_created = date_created;
//    }
    public void setDate_created(Long date_created) {
        this.date_created = date_created;
    }
    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }


    @Override
    public String toString() {
        return "Photo{" +
                "photo_description='" + photo_description + '\'' +
                ", date_created='" + date_created + '\'' +
                ", image_path='" + image_path + '\'' +
                ", photo_id='" + photo_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", quantity='" + quantity + '\'' +
                '}';
    }
}
