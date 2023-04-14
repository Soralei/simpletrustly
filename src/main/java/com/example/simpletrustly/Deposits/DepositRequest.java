package com.example.simpletrustly.Deposits;

import java.util.HashMap;

public class DepositRequest {
    private String method;
    private String version;
    private HashMap<String, Object> params;

    public HashMap<String, Object> getParams() {
        return params;
    }

    public void setParams(HashMap<String, Object> params) {
        this.params = params;
    }

    public DepositRequest() {
        this.method = "Deposit";
        this.version = "1.1";
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
