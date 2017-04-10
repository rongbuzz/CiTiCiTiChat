package com.example.rongbuzz.citicitichat;

/**
 * Created by User on 3/31/2017.
 */

public class Chat {
    private String mName;
    private String mMessage;
    private String mUid;

    public Chat() {
        // Needed for Firebase
    }

    public Chat(String name, String message, String uid) {
        mName = name;
        mMessage = message;
        mUid = uid;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
    }
}
