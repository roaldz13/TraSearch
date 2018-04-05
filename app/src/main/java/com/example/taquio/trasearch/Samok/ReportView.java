package com.example.taquio.trasearch.Samok;

/**
 * Created by Taquio on 3/26/2018.
 */

public class ReportView {

    String postID,report_message;

    public ReportView(){}


    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getReport_message() {
        return report_message;
    }

    public void setReport_message(String report_message) {
        this.report_message = report_message;
    }

    public ReportView(String postID, String report_message) {
        this.postID = postID;
        this.report_message = report_message;
    }
}
