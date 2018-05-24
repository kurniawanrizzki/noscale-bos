package com.noscale.bos.models;

/**
 * Created by kurniawanrizzki on 21/05/18.
 */

public class HttpMessage {

    public class WhatsappRequest {
        private String phone;
        private String body;

        public WhatsappRequest(String phone, String body) {
            this.phone = phone;
            this.body = body;
        }

        @Override
        public String toString () {
            return phone
                    +" "+body;
        }

    }

    public class WhatsappResponse {
        private boolean sent;
        private String message;

        public WhatsappResponse (boolean sent, String message) {
            this.sent = sent;
            this.message = message;
        }

        @Override
        public String toString() {
            return sent
                    +" "+message;
        }
    }

    public class Request {
        public long id;
        public int lastAttempt;
        public String phone;
        public String message;
        public String token;

        public Request (String token,String phone, String message, long id, int lastAttempt) {
            this.id = id;
            this.lastAttempt = lastAttempt;
            this.token = token;
            this.phone = phone;
            this.message = message;
        }

        @Override
        public String toString() {
            return token
                    +" "+phone
                    +" "+message
                    +" "+id;
        }

    }

    public class Response {
        public String status;
        public String message;

        public Response (String status, String message) {
            this.status = status;
            this.message = message;
        }

        @Override
        public String toString() {
            return status
                    +" "+message;
        }
    }

}
