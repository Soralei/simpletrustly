package com.example.simpletrustly.Deposits;

public class DepositResponse {
    public String version;
    public Result result;

    public static class Result {
        public String signature;
        public String uuid;
        public String method;
        public Data data;
    }

    public static class Data {
        public String orderid;
        public String url;
    }
}
