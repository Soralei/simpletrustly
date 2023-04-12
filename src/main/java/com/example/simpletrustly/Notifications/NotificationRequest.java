package com.example.simpletrustly.Notifications;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationRequest {
    public String method;
    public String version;
    public Params params;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Params {
        public String signature;
        public String uuid;
        public Data data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        public String notificationid;
        public String messageid;
        public String orderid;
        public String accountid;
        public String verified;
        public Attributes attributes;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Attributes {
        public String clearinghouse;
        public String bank;
        public String descriptor;
        public String lastdigits;
    }
}
