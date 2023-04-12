package com.example.simpletrustly.SignatureManager;

import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SignatureManager {
    
    public static RSAPrivateKey ReadPrivateKeyFromFile() {
        try {
            ClassPathResource classPathResource = new ClassPathResource("trustly_private_test.pem");
            File privateKeyFile = classPathResource.getFile();

            PEMParser pemParser = new PEMParser(new FileReader(privateKeyFile));
            PEMKeyPair pemKeyPair = (PEMKeyPair)pemParser.readObject();
            pemParser.close();

            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            PrivateKeyInfo privateKeyInfo = pemKeyPair.getPrivateKeyInfo();
            PrivateKey privateKey = converter.getPrivateKey(privateKeyInfo);

            return (RSAPrivateKey)privateKey;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static RSAPublicKey ReadPublicKeyFromFile() {
        try {
            ClassPathResource classPathResource = new ClassPathResource("trustly_public_test.pem");
            File publicKeyFile = classPathResource.getFile();

            PEMParser pemParser = new PEMParser(new FileReader(publicKeyFile));
            SubjectPublicKeyInfo object = (SubjectPublicKeyInfo)pemParser.readObject();

            pemParser.close();

            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            PublicKey publicKey = converter.getPublicKey(object);

            return (RSAPublicKey)publicKey;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static String CreateSignature(String method, String uuid, String serializedData) {
        try {
            RSAPrivateKey privateKey = ReadPrivateKeyFromFile();
            String plainText = method + uuid + serializedData;

            Signature signatureInstance = Signature.getInstance("SHA1withRSA");
            signatureInstance.initSign(privateKey);
            signatureInstance.update(plainText.getBytes(StandardCharsets.UTF_8));

            byte[] signature = signatureInstance.sign();
            String signatureEncoded = Base64.getEncoder().encodeToString(signature);

            return signatureEncoded;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Boolean VerifySignature(String responseSignature, String method, String uuid, String serializedData) {
        RSAPublicKey publicKey = ReadPublicKeyFromFile();
        try {
            final byte[] signature = Base64.getDecoder().decode(responseSignature);
            final Signature signatureInstance = Signature.getInstance("SHA1withRSA");
            signatureInstance.initVerify(publicKey);
            final String expectedPlainText = String.format("%s%s%s", method, uuid, serializedData);
            signatureInstance.update(expectedPlainText.getBytes("UTF-8"));
            return signatureInstance.verify(signature);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String SerializeData(Object data) {
        // Map objects to a JsonNode with null exclusion before serializing
        ObjectMapper mapper = new ObjectMapper().setDefaultPropertyInclusion(Include.NON_NULL);
        JsonNode dataToSerialize = mapper.valueToTree(data);
        return SerializeData(dataToSerialize);
    }

    // Recursive serializing
    public static String SerializeData(JsonNode node) {
        String result = "";

        if(node.isArray()) {
            Iterator<JsonNode> it = node.elements();
            while(it.hasNext()) {
                result += SerializeData(it.next());
            }
        } else if(node.isObject()) {
            Iterator<String> fields = node.fieldNames();
            ArrayList<String> sortedFields = new ArrayList<>();

            while(fields.hasNext()) {
                sortedFields.add(fields.next());
            }
            sortedFields.sort((a, b) -> a.compareTo(b));

            for (String field : sortedFields) {
                result += field + SerializeData(node.get(field));
            }
        } else {
            result += node.asText();
        }

        return result;
    }
}
