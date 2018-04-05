package com.example.taquio.trasearch.Samok;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Edward on 11/03/2018.
 */

class Junkshop implements Parcelable {

    String bsnBusinessName, bsnEmail, bsnLocation, userType,
            bsnMobile, bsnPhone, image, imagePermit, image_thumb,
            userID, device_token;
    Boolean isVerified;
    public Junkshop(){}

    protected Junkshop(Parcel in) {
        bsnBusinessName = in.readString();
        bsnEmail = in.readString();
        bsnLocation = in.readString();
        userType = in.readString();
        bsnMobile = in.readString();
        bsnPhone = in.readString();
        image = in.readString();
        imagePermit = in.readString();
        image_thumb = in.readString();
        userID = in.readString();
        device_token = in.readString();
        byte tmpIsVerified = in.readByte();
        isVerified = tmpIsVerified == 0 ? null : tmpIsVerified == 1;
    }

    public static final Creator<Junkshop> CREATOR = new Creator<Junkshop>() {
        @Override
        public Junkshop createFromParcel(Parcel in) {
            return new Junkshop(in);
        }

        @Override
        public Junkshop[] newArray(int size) {
            return new Junkshop[size];
        }
    };

    @Override
    public String toString() {
        return "Junkshop{" +
                "bsnBusinessName='" + bsnBusinessName + '\'' +
                ", bsnEmail='" + bsnEmail + '\'' +
                ", bsnLocation='" + bsnLocation + '\'' +
                ", userType='" + userType + '\'' +
                ", bsnMobile='" + bsnMobile + '\'' +
                ", bsnPhone='" + bsnPhone + '\'' +
                ", image='" + image + '\'' +
                ", imagePermit='" + imagePermit + '\'' +
                ", image_thumb='" + image_thumb + '\'' +
                ", userID='" + userID + '\'' +
                ", device_token='" + device_token + '\'' +
                ", isVerified=" + isVerified +
                '}';
    }

    public String getBsnBusinessName() {
        return bsnBusinessName;
    }

    public void setBsnBusinessName(String bsnBusinessName) {
        this.bsnBusinessName = bsnBusinessName;
    }

    public String getBsnEmail() {
        return bsnEmail;
    }

    public void setBsnEmail(String bsnEmail) {
        this.bsnEmail = bsnEmail;
    }

    public String getBsnLocation() {
        return bsnLocation;
    }

    public void setBsnLocation(String bsnLocation) {
        this.bsnLocation = bsnLocation;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getBsnMobile() {
        return bsnMobile;
    }

    public void setBsnMobile(String bsnMobile) {
        this.bsnMobile = bsnMobile;
    }

    public String getBsnPhone() {
        return bsnPhone;
    }

    public void setBsnPhone(String bsnPhone) {
        this.bsnPhone = bsnPhone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImagePermit() {
        return imagePermit;
    }

    public void setImagePermit(String imagePermit) {
        this.imagePermit = imagePermit;
    }

    public String getImage_thumb() {
        return image_thumb;
    }

    public void setImage_thumb(String image_thumb) {
        this.image_thumb = image_thumb;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bsnBusinessName);
        dest.writeString(bsnEmail);
        dest.writeString(bsnLocation);
        dest.writeString(userType);
        dest.writeString(bsnMobile);
        dest.writeString(bsnPhone);
        dest.writeString(image);
        dest.writeString(imagePermit);
        dest.writeString(image_thumb);
        dest.writeString(userID);
        dest.writeString(device_token);
        dest.writeByte((byte) (isVerified == null ? 0 : isVerified ? 1 : 2));
    }
}
