package com.example.taquio.trasearch.Samok;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Del Mar on 2/24/2018.
 */

public class BusinessInfo implements Parcelable {

    String bsnEmail;
    String bsnBusinessName;
    String bsnLocation;
    String bsnMobile;
    String bsnPhone;
    String imagePermit;
    String image;
    String image_thumb;
    String device_token;
    String userID;
    String userType;
    Boolean isVerified;

   BusinessInfo(){}

    protected BusinessInfo(Parcel in) {
        bsnEmail = in.readString();
        bsnBusinessName = in.readString();
        bsnLocation = in.readString();
        bsnMobile = in.readString();
        bsnPhone = in.readString();
        imagePermit = in.readString();
        image = in.readString();
        image_thumb = in.readString();
        device_token = in.readString();
        userID = in.readString();
        userType = in.readString();
        byte tmpIsVerified = in.readByte();
        isVerified = tmpIsVerified == 0 ? null : tmpIsVerified == 1;
    }

    public static final Creator<BusinessInfo> CREATOR = new Creator<BusinessInfo>() {
        @Override
        public BusinessInfo createFromParcel(Parcel in) {
            return new BusinessInfo(in);
        }

        @Override
        public BusinessInfo[] newArray(int size) {
            return new BusinessInfo[size];
        }
    };

    @Override
    public String toString() {
        return "BusinessInfo{" +
                "bsnEmail='" + bsnEmail + '\'' +
                ", bsnBusinessName='" + bsnBusinessName + '\'' +
                ", bsnLocation='" + bsnLocation + '\'' +
                ", bsnMobile='" + bsnMobile + '\'' +
                ", bsnPhone='" + bsnPhone + '\'' +
                ", imagePermit='" + imagePermit + '\'' +
                ", image='" + image + '\'' +
                ", image_thumb='" + image_thumb + '\'' +
                ", device_token='" + device_token + '\'' +
                ", userID='" + userID + '\'' +
                ", userType='" + userType + '\'' +
                ", isVerified=" + isVerified +
                '}';
    }

    public String getBsnEmail() {
        return bsnEmail;
    }

    public void setBsnEmail(String bsnEmail) {
        this.bsnEmail = bsnEmail;
    }

    public String getBsnBusinessName() {
        return bsnBusinessName;
    }

    public void setBsnBusinessName(String bsnBusinessName) {
        this.bsnBusinessName = bsnBusinessName;
    }

    public String getBsnLocation() {
        return bsnLocation;
    }

    public void setBsnLocation(String bsnLocation) {
        this.bsnLocation = bsnLocation;
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

    public String getImagePermit() {
        return imagePermit;
    }

    public void setImagePermit(String imagePermit) {
        this.imagePermit = imagePermit;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_thumb() {
        return image_thumb;
    }

    public void setImage_thumb(String image_thumb) {
        this.image_thumb = image_thumb;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
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
        dest.writeString(bsnEmail);
        dest.writeString(bsnBusinessName);
        dest.writeString(bsnLocation);
        dest.writeString(bsnMobile);
        dest.writeString(bsnPhone);
        dest.writeString(imagePermit);
        dest.writeString(image);
        dest.writeString(image_thumb);
        dest.writeString(device_token);
        dest.writeString(userID);
        dest.writeString(userType);
        dest.writeByte((byte) (isVerified == null ? 0 : isVerified ? 1 : 2));
    }
}
