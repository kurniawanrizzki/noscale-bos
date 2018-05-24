package com.noscale.bos.models;

import android.os.Parcel;
import android.os.Parcelable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by kurniawanrizzki on 20/05/18.
 */

public class RequestMessage implements Parcelable {

    public static final String MESSAGE_FILTERED_REGEX = "\\(([^)]*)\\)[^(]*$";

    public interface Status {
        int UNDELIVERED = -1;
        int WAITING = 0;
        int QUEUE = 1;
        int DELIVERED = 2;
    }

    private long id;
    private String src;
    private String content;
    private int status;
    private int lastAttempt;
    private long receivedTime;
    private long forwardedTime;

    public RequestMessage () {

    }

    protected RequestMessage(Parcel in) {
        id = in.readLong();
        src = in.readString();
        content = in.readString();
        status = in.readInt();
        lastAttempt = in.readInt();
        receivedTime = in.readLong();
        forwardedTime = in.readLong();
    }

    public static final Creator<RequestMessage> CREATOR = new Creator<RequestMessage>() {
        @Override
        public RequestMessage createFromParcel(Parcel in) {
            return new RequestMessage(in);
        }

        @Override
        public RequestMessage[] newArray(int size) {
            return new RequestMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(src);
        dest.writeString(content);
        dest.writeInt(status);
        dest.writeInt(lastAttempt);
        dest.writeLong(receivedTime);
        dest.writeLong(forwardedTime);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLastAttempt() {
        return lastAttempt;
    }

    public void setLastAttempt(int lastAttempt) {
        this.lastAttempt = lastAttempt;
    }

    public long getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(long receivedTime) {
        this.receivedTime = receivedTime;
    }

    public long getForwardedTime() {
        return forwardedTime;
    }

    public void setForwardedTime(long forwardedTime) {
        this.forwardedTime = forwardedTime;
    }

    @Override
    public String toString() {
        return id
                +" "+src
                +" "+content
                +" "+getStatusLabel(status)
                +" "+lastAttempt
                +" "+getStringTime(receivedTime)
                +" "+(forwardedTime > 0?getStringTime(forwardedTime):"-");
    }

    public String getStatusLabel (int status) {
        if (status == Status.DELIVERED) {
            return "undelivered";
        } else if (status == Status.DELIVERED) {
            return "delivered";
        } else {
            return "waiting";
        }
    }

    public String getStringTime (long timeMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMillis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return sdf.format(calendar.getTime());
    }

}
