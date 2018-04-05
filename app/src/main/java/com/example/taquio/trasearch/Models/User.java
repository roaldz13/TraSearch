package com.example.taquio.trasearch.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Edward 2018.
 */

public class User implements Parcelable {


    private String userID, Email, Name, Image,
            Image_thumb, Address, device_token, PhoneNumber;
    private Boolean isVerified;

    public User() {

    }

    protected User(Parcel in) {
        userID = in.readString();
        Email = in.readString();
        Name = in.readString();
        Image = in.readString();
        Image_thumb = in.readString();
        Address = in.readString();
        device_token = in.readString();
        PhoneNumber = in.readString();
        byte tmpIsVerified = in.readByte();
        isVerified = tmpIsVerified == 0 ? null : tmpIsVerified == 1;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public String toString() {
        return "User{" +
                "userID='" + userID + '\'' +
                ", Email='" + Email + '\'' +
                ", Name='" + Name + '\'' +
                ", Image='" + Image + '\'' +
                ", Image_thumb='" + Image_thumb + '\'' +
                ", Address='" + Address + '\'' +
                ", device_token='" + device_token + '\'' +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                ", isVerified=" + isVerified +
                '}';
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getImage_thumb() {
        return Image_thumb;
    }

    public void setImage_thumb(String image_thumb) {
        Image_thumb = image_thumb;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
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
        dest.writeString(userID);
        dest.writeString(Email);
        dest.writeString(Name);
        dest.writeString(Image);
        dest.writeString(Image_thumb);
        dest.writeString(Address);
        dest.writeString(device_token);
        dest.writeString(PhoneNumber);
        dest.writeByte((byte) (isVerified == null ? 0 : isVerified ? 1 : 2));
    }
}