package com.nobugexception.hermes;

import android.os.Parcel;
import android.os.Parcelable;

public class Request implements Parcelable {

    // 请求名称
    private String requestName;

    // 数据（json）
    private String paramJson;

    // 向哪个服务类请求
    private String serviceClassFullName;

    public Request(String requestName, String paramJson, String serviceClassFullName) {
        this.requestName = requestName;
        this.paramJson = paramJson;
        this.serviceClassFullName = serviceClassFullName;
    }

    protected Request(Parcel in) {
        requestName = in.readString();
        paramJson = in.readString();
        serviceClassFullName = in.readString();
    }

    public static final Creator<Request> CREATOR = new Creator<Request>() {
        @Override
        public Request createFromParcel(Parcel in) {
            return new Request(in);
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(requestName);
        dest.writeString(paramJson);
        dest.writeString(serviceClassFullName);
    }

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public String getParamJson() {
        return paramJson;
    }

    public void setParamJson(String paramJson) {
        this.paramJson = paramJson;
    }

    public String getServiceFullClassName() {
        return serviceClassFullName;
    }

    public void setServiceFullClassName(String serviceClassName) {
        this.serviceClassFullName = serviceClassName;
    }
}
