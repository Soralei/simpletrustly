package com.example.simpletrustly;

import java.util.HashMap;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.simpletrustly.Notifications.NotificationResponse;
import com.example.simpletrustly.SignatureManager.SignatureManager;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
public class APIController {

    @PostMapping(path="/notification", consumes="application/json", produces="application/json")
    public NotificationResponse OnNotification(@RequestBody JsonNode notification) {

        JsonNode requestParams = notification.get("params");
        JsonNode requestData = requestParams.get("data");
        String requestMethod = notification.get("method").asText();
        String requestSignature = requestParams.get("signature").asText();
        String requestUuid = requestParams.get("uuid").asText();

        NotificationResponse response = new NotificationResponse();
        HashMap<String, Object> responseResult = new HashMap<>();
        HashMap<String, Object> responseData = new HashMap<>();
        response.setResult(responseResult);
        responseResult.put("data", responseData);

        responseData.put("status", "OK");

        responseResult.put("uuid", requestUuid);
        responseResult.put("method", requestMethod);
        responseResult.put("signature", SignatureManager.CreateSignature(requestMethod, requestUuid, SignatureManager.SerializeData(responseData)));

        System.out.println("/notification received: " + requestMethod);

        Boolean verified = SignatureManager.VerifySignature(requestSignature, requestMethod, requestUuid, SignatureManager.SerializeData(requestData));
        if(verified) {
            System.out.println("notification signature verified");
            return response;
        }
        System.out.println("notification signature validation failed");

        // Don't return any information if the signature verification failed. Potentially malicious notification.
        return null;
    }
}