@file:JvmName("PrefGetter")

package com.example.astrodashalib.helper

import com.example.astrodashalib.view.modules.phoneVerification.PhoneVerificationPresenter


/**
 * Created by himanshu on 25/09/17.
 */

val USER_ID_KEY = "user_id"
val DEVICE_ID_KEY = "device_id"
val PHONE_NUMBER_KEY = "phone_number"
val LATEST_USER_ADDED_COUNT = "latest_user_added_count"
val LATEST_USER_SHOWN = "latest_user_shown"
val PHONE_VERIFICATION_SHOWN = "phone_verification_shown"

fun setUserId(userId: String) {
    set(USER_ID_KEY, userId)
}

fun getUserId(): String {
    return getString(USER_ID_KEY, "-1")?:"-1"
}

fun setDeviceId(deviceId: String) {
    set(DEVICE_ID_KEY, deviceId)
}

fun getDeviceId(): String {
    return getString(DEVICE_ID_KEY, "")?:""
}

fun setPhoneNumber(phoneNumber: String) {
    set(PHONE_NUMBER_KEY, phoneNumber)
}

fun getPhoneNumber(): String {
    return getString(PHONE_NUMBER_KEY, "")?:""
}

fun setPhoneVerificationShown(phoneVerificationShown : Boolean) {
    set(PHONE_VERIFICATION_SHOWN, phoneVerificationShown)
}

fun isPhoneVerificationShown(): Boolean {
    return getBoolean(PHONE_VERIFICATION_SHOWN)
}

fun setUserModel(key: String, userModel: String) {
    set(key, userModel)
}

fun getUserModel(key: String): String? {
    return getString(key)
}

fun setLatestUserAddedCount(count: Int) {
    set(LATEST_USER_ADDED_COUNT, count)
}

fun getLatestUserAddedCount(): Int {
    return getInt(LATEST_USER_ADDED_COUNT, -1)
}

fun setLatestUserShown(userNameKey: String) {
    set(LATEST_USER_SHOWN, userNameKey)
}

fun getLatestUserShown(): String {
    return getString(LATEST_USER_SHOWN, "")?:""
}