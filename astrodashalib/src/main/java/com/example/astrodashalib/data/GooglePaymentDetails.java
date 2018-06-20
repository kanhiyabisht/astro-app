package com.example.astrodashalib.data;

import com.google.gson.annotations.SerializedName;
import com.example.astrodashalib.util.Purchase;

import java.io.Serializable;

/**
 * Created by himanshu on 09/10/17.
 */

public class GooglePaymentDetails implements Serializable {
    @SerializedName("itemType")
    public String mItemType;

    @SerializedName("orderId")
    public String mOrderId;

    @SerializedName("packageName")
    public String mPackageName;

    @SerializedName("sku")
    public String mSku;

    @SerializedName("purchaseTime")
    public long mPurchaseTime;

    @SerializedName("purchaseState")
    public int mPurchaseState;

    @SerializedName("developerPayload")
    public String mDeveloperPayload;

    @SerializedName("token")
    public String mToken;

    @SerializedName("originalJson")
    public String mOriginalJson;

    @SerializedName("signature")
    public String mSignature;

    @SerializedName("autoRenewing")
    public boolean mIsAutoRenewing;

    public GooglePaymentDetails(Purchase purchase) {
        mItemType = purchase.getItemType();
        mOrderId = purchase.getOrderId();
        mPackageName = purchase.getPackageName();
        mSku = purchase.getSku();
        mPurchaseTime = purchase.getPurchaseTime();
        mPurchaseState = purchase.getPurchaseState();
        mDeveloperPayload = purchase.getDeveloperPayload();
        mToken = purchase.getToken();
        mOriginalJson = purchase.getOriginalJson();
        mSignature = purchase.getSignature();
        mIsAutoRenewing = purchase.isAutoRenewing();
    }
}
