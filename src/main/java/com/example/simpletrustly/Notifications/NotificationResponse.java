package com.example.simpletrustly.Notifications;

public class NotificationResponse {
    public String version = "1.1";
    public Result result = new Result();

    public static class Result {
        public String signature;
        public String uuid;
        public String method;
        public Data data = new Data();
    }

    public static class Data {
        public String status = "OK";
    };
}
