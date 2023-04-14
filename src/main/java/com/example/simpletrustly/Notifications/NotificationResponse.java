package com.example.simpletrustly.Notifications;

import java.util.HashMap;

public class NotificationResponse {
    private String version;
    private HashMap<String, Object> result;
    
    public NotificationResponse() {
        this.version = "1.1";
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public HashMap<String, Object> getResult() {
        return result;
    }
    public void setResult(HashMap<String, Object> result) {
        this.result = result;
    }
}
