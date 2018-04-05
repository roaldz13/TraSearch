package com.example.taquio.trasearch.Samok;

/**
 * Created by Taquio on 3/19/2018.
 */

public class Reports {
    public String message_report,photo_path;

    public Reports(){}

    public String getMessage_report() {
        return message_report;
    }

    public void setMessage_report(String message_report) {
        this.message_report = message_report;
    }

    public String getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(String photo_path) {
        this.photo_path = photo_path;
    }

    public Reports(String message_report, String photo_path) {
        this.message_report = message_report;
        this.photo_path = photo_path;
    }
}
