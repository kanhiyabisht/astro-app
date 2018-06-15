package com.example.astrodashalib;

import com.example.astrodashalib.data.models.Country;
import com.example.astrodashalib.utils.BaseConfiguration;

import java.util.HashMap;

/**
 * Created by himanshu on 25/09/17.
 */

public class Constant {

    public static final String SIMPLE = "simple";
    public static final String ANTAR = "antar";
    public static final String NEXT_ANTAR = "next_antar";
    public static final String GOOD_YOG_TYPE = "Good";
    public static final String BAD_YOG_TYPE = "bad";

    public static final String PHONE = "phone";
    public static final String EMAIL = "email";
    public static final String PAYTM = "paytm";
    public static final String GOOGLE_WALLET = "google wallet";

    public static final String GCM_SOURCE = "gcm";
    public static final String FAYE_SOURCE = "faye";
    public static final String CHAT = "chat";
    public static final int CHAT_NOTIFICATION_ID = 1789;

    public static String CONTENT_TYPE_KEY = "Content-Type";
    public static String ACCEPT_TYPE_KEY = "Accept";
    public static String ACCEPT_TYPE_VALUE = "application/json";

    public static String KEY = "key";
    public static String KEY_VALUE = "qYLL2KNmMDPt2-Z*-g&_eSPh+";
    public static String CHAT_KEY = "android0873954";
    public static String CHAT_SECRET = "16r4Gcuvsf";

    public static String CUSTOMER_CARE_NO = "0124 6660999";

    public static final String WELCOME_MESSAGE = "Welcome to Tell My Luck! \n" +
            "We are the team of authentic Vedic astrologers. Now, you don't need to travel far away to meet us. Here we are to answer your queries. \n" +
            "Try today and get an extra bonus!";

    public static final String LOGIN_MEESAGE = "Now you can ask first question for FREE";

    public static final String DEFAULT_COUNTRY_CODE = "IN";
    public static final int DEVICE_ID_REQUEST_CODE = 238;
    public static final int DEVICE_ID_SERVICE_JOB_ID = 239;
    public static final int FAYE_SERVICE_JOB_ID = 258;

    public static final int INDIA_REMEDY_PRICE = 261;
    public static HashMap<Integer, String> upayIdHashMap = new HashMap<>();

    public static String TAG_PREDICTION_DETAIL_FRAGMENT = "Prediction_Detail_Fragment";
    public static String TAG_PURCHASE_REMEDY_FRAGMENT = "Purchase_Remedy_Fragment";

    static {
        upayIdHashMap.put(1, "upay_1");
        /*upayIdHashMap.put(2, "upay_2");
        upayIdHashMap.put(3, "upay_3");
        upayIdHashMap.put(4, "upay_4");
        upayIdHashMap.put(5, "upay_5");
        upayIdHashMap.put(6, "upay_6");
        upayIdHashMap.put(7, "upay_7");
        upayIdHashMap.put(8, "upay_8");
        upayIdHashMap.put(9, "upay_9");
        upayIdHashMap.put(10, "upay_10");
        upayIdHashMap.put(11, "upay_11");
        upayIdHashMap.put(12, "upay_12");
        upayIdHashMap.put(13, "upay_13");
        upayIdHashMap.put(14, "upay_14");
        upayIdHashMap.put(15, "upay_15");
        upayIdHashMap.put(16, "upay_16");
        upayIdHashMap.put(17, "upay_17");
        upayIdHashMap.put(18, "upay_18");
        upayIdHashMap.put(19, "upay_19");
        upayIdHashMap.put(20, "upay_20");
        upayIdHashMap.put(21, "upay_21");
        upayIdHashMap.put(22, "upay_22");
        upayIdHashMap.put(23, "upay_23");
        upayIdHashMap.put(24, "upay_24");
        upayIdHashMap.put(25, "upay_25");
        upayIdHashMap.put(26, "upay_26");
        upayIdHashMap.put(27, "upay_27");
        upayIdHashMap.put(28, "upay_28");
        upayIdHashMap.put(29, "upay_29");
        upayIdHashMap.put(30, "upay_30");*/
    }

    public static Country getIndiaCountryData() {
        Country country = new Country();
        country.country = "India";
        country.countryCode = "Ind.";
        country.isProgressBar = false;
        return country;
    }
}
