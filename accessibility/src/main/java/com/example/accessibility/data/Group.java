package com.example.accessibility.data;


public class Group {
    public int mId;
    public String mGroupLink;
    public String mCountry;

    @Override
    public String toString() {
        return "id=" + mId + " url=" + mGroupLink + " country=" + mCountry;
    }
}
