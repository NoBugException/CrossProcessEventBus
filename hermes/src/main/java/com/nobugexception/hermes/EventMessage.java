package com.nobugexception.hermes;

import android.os.Parcel;
import android.os.Parcelable;

public class EventMessage implements Parcelable {

    protected EventMessage(Parcel in) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
