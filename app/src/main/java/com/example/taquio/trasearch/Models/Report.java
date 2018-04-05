package com.example.taquio.trasearch.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Edward on 28/02/2018.
 */

public class Report implements Parcelable{

    private String message_report;
    private String report_id;
    private String photo_id;

    public Report(String s, String report_id, String toString) {}

    public Report(Parcel in) {
        message_report = in.readString();
        report_id = in.readString();
        photo_id = in.readString();
    }

    public static final Creator<Report> CREATOR = new Creator<Report>() {
        @Override
        public Report createFromParcel(Parcel in) {
            return new Report(in);
        }

        @Override
        public Report[] newArray(int size) {
            return new Report[size];
        }
    };

    @Override
    public String toString() {
        return "Report{" +
                "message_report='" + message_report + '\'' +
                ", report_id='" + report_id + '\'' +
                ", photo_id='" + photo_id + '\'' +
                '}';
    }

    public String getMessage_report() {
        return message_report;
    }

    public void setMessage_report(String message_report) {
        this.message_report = message_report;
    }

    public String getReport_id() {
        return report_id;
    }

    public void setReport_id(String report_id) {
        this.report_id = report_id;
    }

    public String getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message_report);
        dest.writeString(report_id);
        dest.writeString(photo_id);
    }
}


