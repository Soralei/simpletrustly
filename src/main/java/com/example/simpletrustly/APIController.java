package com.example.simpletrustly;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.simpletrustly.Notifications.NotificationRequest;
import com.example.simpletrustly.Notifications.NotificationResponse;
import com.example.simpletrustly.SignatureManager.SignatureManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class APIController {

    @PostMapping(path="/notification", consumes="application/json", produces="application/json")
    public NotificationResponse OnNotification(@RequestBody JsonNode notification) throws JsonProcessingException, IllegalArgumentException {

        ObjectMapper mapper = new ObjectMapper();
        NotificationRequest notificationRequest = mapper.treeToValue(notification, NotificationRequest.class);

        NotificationResponse response = new NotificationResponse();
        response.result.uuid = notificationRequest.params.uuid;
        response.result.method = notificationRequest.method;
        response.result.signature = SignatureManager.CreateSignature(response.result.method, response.result.uuid, SignatureManager.SerializeData(response.result.data));

        System.out.println("/notification received: " + response.result.method);

        Boolean verified = SignatureManager.VerifySignature(notificationRequest.params.signature, notificationRequest.method, notificationRequest.params.uuid, SignatureManager.SerializeData(notification.get("params").get("data")));
        if(verified) {
            System.out.println("notification signature verified");
            return response;
        }

        return null;
    }
}