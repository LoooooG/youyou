package com.videolibrary.videochat.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jac on 2017/10/30.
 */

public class RoomInfo implements Parcelable {

    public String   roomID;
    public String   roomName;
    public String   roomCreator;
    public String   mixedPlayURL;
    public List<PusherInfo> pushers;


    public RoomInfo() {

    }

    public RoomInfo(String roomID, String roomName, String roomCreator, String mixedPlayURL, List<PusherInfo> pushers) {
        this.roomID = roomID;
        this.roomName = roomName;
        this.roomCreator = roomCreator;
        this.mixedPlayURL = mixedPlayURL;
        this.pushers = pushers;
    }

    protected RoomInfo(Parcel in) {
        this.roomID = in.readString();
        this.roomName = in.readString();
        this.roomCreator = in.readString();
        this.mixedPlayURL = in.readString();
        this.pushers = new ArrayList<PusherInfo>();
        in.readList(this.pushers, PusherInfo.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.roomID);
        dest.writeString(this.roomName);
        dest.writeString(this.roomCreator);
        dest.writeString(this.mixedPlayURL);
        dest.writeList(this.pushers);
    }

    public static final Creator<RoomInfo> CREATOR = new Creator<RoomInfo>() {
        @Override
        public RoomInfo createFromParcel(Parcel source) {
            return new RoomInfo(source);
        }

        @Override
        public RoomInfo[] newArray(int size) {
            return new RoomInfo[size];
        }
    };
}
