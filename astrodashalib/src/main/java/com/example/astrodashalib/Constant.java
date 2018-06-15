package com.example.astrodashalib;

import com.example.astrodashalib.data.models.Country;

/**
 * Created by himanshu on 25/09/17.
 */

public class Constant {

    public static String CONTENT_TYPE_KEY = "Content-Type";
    public static String ACCEPT_TYPE_KEY = "Accept";
    public static String ACCEPT_TYPE_VALUE = "application/json";

    public static String KEY = "key";
    public static String KEY_VALUE = "qYLL2KNmMDPt2-Z*-g&_eSPh+";

    public static String CUSTOMER_CARE_NO = "0124 6660999";

    public static final String DEFAULT_COUNTRY_CODE = "IN";

    public static Country getIndiaCountryData() {
        Country country = new Country();
        country.country = "India";
        country.countryCode = "Ind.";
        country.isProgressBar = false;
        return country;
    }
}
