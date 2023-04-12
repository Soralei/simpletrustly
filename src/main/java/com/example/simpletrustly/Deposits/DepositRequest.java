package com.example.simpletrustly.Deposits;

public class DepositRequest {
    public String method;
    public String version;
    public Params params = new Params();

    public static class Params {
        public String Signature;
        public String UUID;
        public Data Data = new Data();
    }

    public static class Data {
        public String Username;
        public String Password;
        public String NotificationURL;
        public String EndUserID;
        public String MessageID;
        public Attributes Attributes = new Attributes();
    }

    public static class Attributes {
        public String Currency;
        public String Firstname;
        public String Lastname;
        public String Country;
        public String Locale;
        public String ShopperStatement;
        public String SuccessURL;
        public String FailURL;
        public String Email;
        public String MobilePhone;
        public String Amount;
        public String SuggestedMinAmount;
        public String SuggestedMaxAmount;
        public String AccountID;
        public String IP;
        public String TemplateURL;
        public String URLTarget;
        public String NationalIdentificationNumber;
        public String UnchangeableNationalIdentificationNumber;
        public String ShippingAddressCountry;
        public String ShippingAddressPostalCode;
        public String ShippingAddressCity;
        public String shippingAddressLine1;
        public String ShippingAddressLine2;
        public String ShippingAddress;
        public String RequestDirectDebitMandate;
        public String URLScheme;
        public String ExternalReference;
        public String PSPMerchant;
        public String PSPMerchantURL;
        public String MerchantCategoryCode;
        public RecipientInformation RecipientInformation = new RecipientInformation();
    }

    public static class RecipientInformation {
        public String Partytype;
        public String Firstname;
        public String Lastname;
        public String CountryCode;
        public String CustomerID;
        public String Address;
        public String DateOfBirth;
    }
}
