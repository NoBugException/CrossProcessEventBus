package com.nobugexception.hermes;

import android.os.Parcel;
import android.os.Parcelable;

public class EventMessage implements Parcelable {

    // 全类名
    private String classFullName;

    // 数据
    private String data;

    // 是否是粘性消息
    private boolean isSticky;

    public EventMessage() {
    }

    public EventMessage(String classFullName, String data, boolean isSticky) {
        this.classFullName = classFullName;
        this.data = data;
        this.isSticky = isSticky;
    }

    protected EventMessage(Parcel in) {
        classFullName = in.readString();
        data = in.readString();
        isSticky = in.readBoolean();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(classFullName);
        dest.writeString(data);
        dest.writeBoolean(isSticky);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EventMessage> CREATOR = new Creator<EventMessage>() {
        @Override
        public EventMessage createFromParcel(Parcel in) {
            return new EventMessage(in);
        }

        @Override
        public EventMessage[] newArray(int size) {
            return new EventMessage[size];
        }
    };

    public String getClassFullName() {
        return classFullName;
    }

    public void setClassFullName(String classFullName) {
        this.classFullName = classFullName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isSticky() {
        return isSticky;
    }

    public void setSticky(boolean sticky) {
        isSticky = sticky;
    }
}
