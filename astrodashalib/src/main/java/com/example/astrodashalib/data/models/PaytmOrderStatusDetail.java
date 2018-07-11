package com.example.astrodashalib.data.models;

import com.google.gson.annotations.SerializedName;

public class PaytmOrderStatusDetail {

    @SerializedName("GATEWAYNAME")
    public String gATEWAYNAME;

    @SerializedName("RESPMSG")
    public String rESPMSG;

    @SerializedName("BANKNAME")
    public String bANKNAME;

    @SerializedName("PAYMENTMODE")
    public String pAYMENTMODE;

    @SerializedName("MID")
    public String mID;

    @SerializedName("RESPCODE")
    public String rESPCODE;

    @SerializedName("TXNTYPE")
    public String tXNTYPE;

    @SerializedName("TXNID")
    public String tXNID;

    @SerializedName("TXNAMOUNT")
    public String tXNAMOUNT;

    @SerializedName("ORDERID")
    public String oRDERID;

    @SerializedName("BANKTXNID")
    public String bANKTXNID;

    @SerializedName("STATUS")
    public String sTATUS;

    @SerializedName("REFUNDAMT")
    public String rEFUNDAMT;

    @SerializedName("TXNDATE")
    public String tXNDATE;

}
