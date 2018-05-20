package com.noscale.bos.models;

/**
 * Created by kurniawanrizzki on 21/05/18.
 */

public class ResponseMessage {
    private String status;
    private String message;

    public static enum Status {
        SUCCESS_STATUS, FAILED_STATUS
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
