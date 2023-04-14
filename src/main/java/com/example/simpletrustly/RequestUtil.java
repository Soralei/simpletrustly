package com.example.simpletrustly;

import java.util.HashMap;
import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.example.simpletrustly.Deposits.DepositRequest;
import com.example.simpletrustly.SignatureManager.SignatureManager;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RequestUtil {
    public static String DoDeposit(String amount) {
        DepositRequest request = new DepositRequest();

        HashMap<String, Object> params = new HashMap<>();
        HashMap<String, Object> data = new HashMap<>();
        HashMap<String, Object> attributes = new HashMap<>();
        //HashMap<String, Object> recipientInformation = new HashMap<>();

        String uuid = UUID.randomUUID().toString();

        request.setParams(params);
        params.put("UUID", uuid);
        params.put("Data", data);

        data.put("Username", "devcode");
        data.put("Password", "change_this_gef4D37ypL");
        data.put("NotificationURL", "https://soralei.com/notification");
        data.put("EndUserID", "12345");
        data.put("MessageID", uuid);
        data.put("Attributes", attributes);

        attributes.put("Country", "SE");
        attributes.put("Locale", "sv_SE");
        attributes.put("Currency", "EUR");
        attributes.put("Amount", amount);
        attributes.put("IP", "123.123.123.123");
        attributes.put("MobilePhone", "+46709876543");
        attributes.put("Firstname", "John");
        attributes.put("Lastname", "Doe");
        attributes.put("Email", "test@trustly.com");
        attributes.put("NationalIdentificationNumber", "790131-123");
        attributes.put("SuccessURL", "https://soralei.com/txsuccess");
        attributes.put("FailURL", "https://soralei.com/txfail");

        // Could conditionally check if recipientInformation should be added here
        // but not needed right now
        // attributes.put("RecipientInformation", recipientInformation);

        params.put("Signature", SignatureManager.CreateSignature(request.getMethod(), uuid, SignatureManager.SerializeData(data)));

        // Map into a JsonNode to easily get rid of null fields
        ObjectMapper mapper = new ObjectMapper().setDefaultPropertyInclusion(Include.NON_NULL);
        JsonNode requestNode = mapper.valueToTree(request);
        System.out.println("Sending request: " + requestNode.toPrettyString());

        // Try sending the deposit request
        try {
            RestTemplate requestTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<JsonNode> ent = new HttpEntity<>(requestNode, headers);
            HttpEntity<JsonNode> response = requestTemplate.postForEntity("https://test.trustly.com/api/1", ent, JsonNode.class);

            JsonNode responseBody = mapper.valueToTree(response.getBody());
            JsonNode responseData = responseBody.get("result").get("data");
            String responseSignature = responseBody.get("result").get("signature").asText();
            String responseMethod = responseBody.get("result").get("method").asText();
            String responseUuid = responseBody.get("result").get("uuid").asText();
            String responseUrl = responseData.get("url").asText();

            System.out.println("Received response: " + responseBody.toPrettyString());

            Boolean verified = SignatureManager.VerifySignature(responseSignature, responseMethod, responseUuid, SignatureManager.SerializeData(responseData));
            if(verified) {
                return responseUrl;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
