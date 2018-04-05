package com.example.taquio.trasearch.Samok;

/**
 * Created by Taquio on 2/21/2018.
 */

public class Conv2 {

    public boolean seen;
    public long timestamp;

    public Conv2(){

    }

    public Conv2(boolean seen, long timestamp) {
        this.seen = seen;
        this.timestamp = timestamp;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
