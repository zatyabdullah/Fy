package com.fly.firefly.ui.object;

/**
 * Created by Dell on 12/31/2015.
 */
public class Payment {

    private String signature;
    private String channelType;
    private String channelCode;
    private String cardNumber;
    private String expirationDateMonth;
    private String expirationDateYear;
    private String cvv;
    private String issuingBank;
    private String cardHolderName;

    public Payment(){

    }

    public Payment(Payment obj){

        this.signature = obj.getSignature();
        this.channelType = obj.getChannelType();
        this.channelCode = obj.getChannelCode();
        this.cardNumber = obj.getCardNumber();
        this.expirationDateMonth = obj.getExpirationDateMonth();
        this.expirationDateYear = obj.getExpirationDateYear();
        this.cvv = obj.getCvv();
        this.issuingBank = obj.getIssuingBank();
        this.cardHolderName = obj.getCardHolderName();

    }


    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpirationDateMonth() {
        return expirationDateMonth;
    }

    public void setExpirationDateMonth(String expirationDateMonth) {
        this.expirationDateMonth = expirationDateMonth;
    }

    public String getExpirationDateYear() {
        return expirationDateYear;
    }

    public void setExpirationDateYear(String expirationDateYear) {
        this.expirationDateYear = expirationDateYear;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getIssuingBank() {
        return issuingBank;
    }

    public void setIssuingBank(String issuingBank) {
        this.issuingBank = issuingBank;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

}
