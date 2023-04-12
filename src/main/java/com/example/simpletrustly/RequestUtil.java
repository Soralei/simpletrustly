package com.example.simpletrustly;

import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.example.simpletrustly.Deposits.DepositRequest;
import com.example.simpletrustly.Deposits.DepositResponse;
import com.example.simpletrustly.SignatureManager.SignatureManager;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RequestUtil {
    public static String DoDeposit(String amount) {
        DepositRequest request = new DepositRequest();
        request.method = "Deposit";
        request.version = "1.1";

        request.params.UUID = UUID.randomUUID().toString();

        DepositRequest.Data data = request.params.Data;
        data.Username = "devcode";
        data.Password = "change_this_gef4D37ypL";
        data.NotificationURL = "https://soralei.com/notification";
        data.EndUserID = "12345";
        data.MessageID = request.params.UUID;

        DepositRequest.Attributes attributes = request.params.Data.Attributes;
        attributes.Country = "SE";
        attributes.Locale = "sv_SE";
        attributes.Currency = "EUR";
        attributes.Amount = amount;
        attributes.IP = "123.123.123.123";
        attributes.MobilePhone = "+46709876543";
        attributes.Firstname = "John";
        attributes.Lastname = "Doe";
        attributes.Email = "test@trustly.com";
        attributes.NationalIdentificationNumber = "790131-123";
        attributes.SuccessURL = "https://soralei.com/txsuccess";
        attributes.FailURL = "https://soralei.com/txfail";

        request.params.Signature = SignatureManager.CreateSignature(request.method, request.params.UUID, SignatureManager.SerializeData(data));

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


            DepositResponse depositResponse = mapper.treeToValue(response.getBody(), DepositResponse.class);
            JsonNode responseJson = mapper.valueToTree(response.getBody());
            JsonNode responseData = responseJson.get("result").get("data");

            System.out.println("Received response: " + responseJson.toPrettyString());

            Boolean verified = SignatureManager.VerifySignature(depositResponse.result.signature, depositResponse.result.method, depositResponse.result.uuid, SignatureManager.SerializeData(responseData));
            if(verified) {
                return depositResponse.result.data.url;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
