package com.example.astrodashalib.data.models;

import android.os.Bundle;

import com.google.gson.annotations.SerializedName;

/**
 * Created by himanshu on 09/10/17.
 */

public class PaytmPaymentDetails {

    @SerializedName("CURRENCY")
    public String currency;

    @SerializedName("GATEWAYNAME")
    public String gatewayName;

    @SerializedName("RESPMSG")
    public String respMsg;

    @SerializedName("BANKNAME")
    public String bankName;

    @SerializedName("PAYMENTMODE")
    public String paymentMode;

    @SerializedName("MID")
    public String mid;

    @SerializedName("RESPCODE")
    public String respCode;

    @SerializedName("TXNID")
    public String txnId;

    @SerializedName("TXNAMOUNT")
    public String txnAmount;

    @SerializedName("ORDERID")
    public String orderId;

    @SerializedName("BANKTXNID")
    public String bankTxnId;

    @SerializedName("STATUS")
    public String status;

    @SerializedName("TXNDATE")
    public String txnDate;

    @SerializedName("CHECKSUMHASH")
    public String checksum;

    public PaytmPaymentDetails(Bundle inResponse) {
        status = inResponse.getString("STATUS", "");
        checksum = inResponse.getString("CHECKSUMHASH", "");
        bankName = inResponse.getString("BANKNAME", "");
        orderId = inResponse.getString("ORDERID", "");
        txnAmount = inResponse.getString("TXNAMOUNT", "");
        txnDate = inResponse.getString("TXNDATE", "");
        mid = inResponse.getString("MID", "");
        txnId = inResponse.getString("TXNID", "");
        respCode = inResponse.getString("RESPCODE", "");
        paymentMode = inResponse.getString("PAYMENTMODE", "");
        bankTxnId = inResponse.getString("BANKTXNID", "");
        currency = inResponse.getString("CURRENCY", "");
        gatewayName = inResponse.getString("GATEWAYNAME", "");
        respMsg = inResponse.getString("RESPMSG", "");
    }
}
