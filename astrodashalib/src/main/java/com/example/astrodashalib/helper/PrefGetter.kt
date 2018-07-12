@file:JvmName("PrefGetter")

package com.example.astrodashalib.helper

import android.content.Context
import java.math.BigDecimal


/**
 * Created by himanshu on 25/09/17.
 */

val USER_ID_KEY = "user_id"
val CRM_USER_ID_KEY = "crm_user_id"
val DEVICE_ID_KEY = "device_id"
val PHONE_NUMBER_KEY = "phone_number"
val EMAIL_KEY = "email"
val LATEST_USER_ADDED_COUNT = "latest_user_added_count"
val LATEST_USER_SHOWN = "latest_user_shown"
val LAST_CHAT_TIMESTAMP = "last_chat_timestamp"
val PHONE_VERIFICATION_SHOWN = "phone_verification_shown"
val FAYE_TOKEN = "faye_token"
val FREE_QUESTION_COUNT = "free_question_count"
val SIMPLE_REMEDIES_CATEGORY_QUESTION_ANSWER_LIST_KEY = "simple_remedies_category_question_answer_list"
val SPECIAL_CATEGORY_NAME_KEY = "special_category_name"
val GOOD_REMEDY_CATEGORY_QUESTION_ANSWER_KEY = "good_remedy_category_question_answer"
val BAD_REMEDY_CATEGORY_QUESTION_ANSWER_KEY = "bad_remedy_category_question_answer"
val ANTAR_REMEDY_CATEGORY_QUESTION_ANSWER_KEY = "antar_remedy_cateogry_question_answer"
val NEXT_ANTAR_REMEDY_CATEGORY_QUESTION_ANSWER_KEY = "next_antar_remedy_cateogry_question_answer"
val SIMPLE_REMEDY_KEY = "simple_remedy"
val ANTAR_REMEDY_KEY = "antar_remedy"
val NEXT_ANTAR_REMEDY_KEY = "next_antar_remedy"
val GOOD_PREDICTION_REMEDY_KEY = "good_prediction_remedy"
val BAD_PREDICTION_REMEDY_KEY = "bad_prediction_remedy"
val COMPLEX_REMEDY_KEY = "complex_remedy"
val ANTARDASHA_FAL_KEY = "antardasha_fal"

fun getSimpleRemediesCateogryQuestionAnswerListKey(userKey: String): String = SIMPLE_REMEDIES_CATEGORY_QUESTION_ANSWER_LIST_KEY + "_" + userKey

fun setSimpleRemediesCateogryQuestionAnswerList(userKey: String, questionAnswerList: String,context: Context) {
    val myRemediesQuestionKey = getSimpleRemediesCateogryQuestionAnswerListKey(userKey)
    set(myRemediesQuestionKey, questionAnswerList,context)
}

fun getSimpleRemediesCateogryQuestionAnswerList(userKey: String,context: Context): String {
    val myRemediesQuestionKey = getSimpleRemediesCateogryQuestionAnswerListKey(userKey)
    return getString(myRemediesQuestionKey, "",context) ?: ""
}

fun getSpecialCategoryNameKey(userKey: String): String = SPECIAL_CATEGORY_NAME_KEY + "_" + userKey

fun setSpecialCategoryNameList(userKey: String, categoryList: String,context: Context) {
    val categoryNameKey = getSpecialCategoryNameKey(userKey)
    set(categoryNameKey, categoryList,context)
}

fun getSpecialCategoryNameList(userKey: String,context: Context): String {
    val categoryNameKey = getSpecialCategoryNameKey(userKey)
    return getString(categoryNameKey, "",context) ?: ""
}

fun getGoodRemedyCategoryQuestionAnswerKey(categoryName: String, userKey: String): String = categoryName + "_" + GOOD_REMEDY_CATEGORY_QUESTION_ANSWER_KEY + "_" + userKey

fun setGoodRemedyCategoryQuestionAnswer(categoryName: String, userKey: String, questionAnswer: String,context: Context) {
    val goodRemedyQuestionAnswerKey = getGoodRemedyCategoryQuestionAnswerKey(categoryName, userKey)
    set(goodRemedyQuestionAnswerKey, questionAnswer,context)
}

fun getGoodRemedyCategoryQuestionAnswer(categoryName: String, userKey: String,context: Context): String {
    val goodRemedyQuestionAnswerKey = getGoodRemedyCategoryQuestionAnswerKey(categoryName, userKey)
    return getString(goodRemedyQuestionAnswerKey, "",context) ?: ""
}

fun getBadRemedyCategoryQuestionAnswerKey(categoryName: String, userKey: String): String = categoryName + "_" + BAD_REMEDY_CATEGORY_QUESTION_ANSWER_KEY + "_" + userKey

fun setBadRemedyCategoryQuestionAnswer(categoryName: String, userKey: String, questionAnswer: String,context: Context) {
    val badRemedyQuestionAnswerKey = getBadRemedyCategoryQuestionAnswerKey(categoryName, userKey)
    set(badRemedyQuestionAnswerKey, questionAnswer,context)
}

fun getBadRemedyCategoryQuestionAnswer(categoryName: String, userKey: String,context: Context): String {
    val badRemedyQuestionAnswerKey = getBadRemedyCategoryQuestionAnswerKey(categoryName, userKey)
    return getString(badRemedyQuestionAnswerKey, "",context) ?: ""
}

fun getAntarRemedyCateogryQuestionAnswerKey(userKey: String): String = ANTAR_REMEDY_CATEGORY_QUESTION_ANSWER_KEY + "_" + userKey


fun setAntarRemedyCategoryQuestionAnswer(userKey: String, questionAnswer: String,context: Context) {
    val antarRemedyCateogryQuestionAnswerKey = getAntarRemedyCateogryQuestionAnswerKey(userKey)
    set(antarRemedyCateogryQuestionAnswerKey, questionAnswer,context)
}

fun getAntarRemedyCategoryQuestionAnswer(userKey: String,context: Context): String {
    val antarRemedyCateogryQuestionAnswerKey = getAntarRemedyCateogryQuestionAnswerKey(userKey)
    return getString(antarRemedyCateogryQuestionAnswerKey, "",context) ?: ""
}


fun getNextAntarRemedyCateogryQuestionAnswerKey(userKey: String): String = NEXT_ANTAR_REMEDY_CATEGORY_QUESTION_ANSWER_KEY + "_" + userKey

fun getNextAntarRemedyCategoryQuestionAnswer(userKey: String,context: Context): String {
    val nextAntarRemedyCateogryQuestionAnswerKey = getNextAntarRemedyCateogryQuestionAnswerKey(userKey)
    return getString(nextAntarRemedyCateogryQuestionAnswerKey, "",context) ?: ""
}

fun getSimpleRemedyKey(userKey: String, ruleId: String, questionId: String): String {
    return ruleId + "_" + questionId + "_" + SIMPLE_REMEDY_KEY + "_" + userKey
}

fun setSimpleRemedy(userKey: String, ruleId: String, questionId: String, remedy: String,context: Context) {
    val simpleRemedyKey = getSimpleRemedyKey(userKey, ruleId, questionId)
    set(simpleRemedyKey, remedy,context)
}

fun getSimpleRemedy(ruleId: String, questionId: String, userKey: String,context: Context): String {
    val simpleRemedyKey = getSimpleRemedyKey(userKey, ruleId, questionId)
    return getString(simpleRemedyKey, "",context)?:""
}

fun getAntarRemedyKey(userKey: String): String {
    return ANTAR_REMEDY_KEY + "_" + userKey
}

fun setAntarRemedyList(userKey: String, remedyList: String,context: Context) {
    val antarRemedyKey = getAntarRemedyKey(userKey)
    set(antarRemedyKey, remedyList,context)
}

fun getAntarRemedyList(userKey: String,context: Context): String? {
    val antarRemedyKey = getAntarRemedyKey(userKey)
    return getString(antarRemedyKey, "",context)
}

fun getNextAntarRemedyKey(userKey: String): String {
    return NEXT_ANTAR_REMEDY_KEY + "_" + userKey
}

fun setNextAntarRemedyList(userKey: String, remedyList: String,context: Context) {
    val nextAntarRemedyKey = getNextAntarRemedyKey(userKey)
    set(nextAntarRemedyKey, remedyList,context)
}

fun getNextAntarRemedyList(userKey: String,context: Context): String? {
    val nextAntarRemedyKey = getNextAntarRemedyKey(userKey)
    return getString(nextAntarRemedyKey, "",context)
}

fun getGoodPredictionRemedyKey(categoryName: String, userKey: String): String {
    return categoryName + "_" + GOOD_PREDICTION_REMEDY_KEY + "_" + userKey
}

fun setGoodPredictionRemedy(categoryName: String, userKey: String, remedyList: String,context: Context) {
    val goodPredictionRemedyKey = getGoodPredictionRemedyKey(categoryName, userKey)
    set(goodPredictionRemedyKey, remedyList,context)
}

fun getGoodPredictionRemedy(categoryName: String, userKey: String,context: Context): String? {
    val goodPredictionRemedyKey = getGoodPredictionRemedyKey(categoryName, userKey)
    return getString(goodPredictionRemedyKey, "",context)
}

fun getBadPredictionRemedyKey(categoryName: String, userKey: String): String {
    return categoryName + "_" + BAD_PREDICTION_REMEDY_KEY + "_" + userKey
}

fun setBadPredictionRemedy(categoryName: String, userKey: String, remedyList: String,context: Context) {
    val badPredictionRemedyKey = getBadPredictionRemedyKey(categoryName, userKey)
    set(badPredictionRemedyKey, remedyList,context)
}

fun getBadPredictionRemedy(categoryName: String, userKey: String,context: Context): String? {
    val badPredictionRemedyKey = getBadPredictionRemedyKey(categoryName, userKey)
    return getString(badPredictionRemedyKey, "",context)
}

fun getComplexRemedyKey(userKey: String, questionId: String): String {
    return questionId + "_" + COMPLEX_REMEDY_KEY + "_" + userKey
}

fun setComplexRemedyList(userKey: String, questionId: String, remedyList: String,context: Context) {
    val complexRemedyKey = getComplexRemedyKey(userKey, questionId)
    set(complexRemedyKey, remedyList,context)
}

fun getComplexRemedyList(questionId: String, userKey: String,context: Context): String? {
    val complexRemedyKey = getComplexRemedyKey(userKey, questionId)
    return getString(complexRemedyKey, "",context)
}

fun setUserId(userId: String,context: Context) {
    set(USER_ID_KEY, userId,context)
}

fun getUserId(context: Context): String {
    return getString(USER_ID_KEY, "-1",context) ?: "-1"
}

fun setCrmUserId(userId: String,context: Context) {
    set(CRM_USER_ID_KEY, userId,context)
}

fun getCrmUserId(context: Context): String {
    return getString(CRM_USER_ID_KEY, "",context) ?: ""
//    return "59c23693390000fd0f3e8c15"
}

fun setDeviceId(deviceId: String,context: Context) {
    set(DEVICE_ID_KEY, deviceId,context)
}

fun getDeviceId(context: Context): String {
    return getString(DEVICE_ID_KEY, "",context) ?: ""
}

fun setFayeToken(fayeToken: String,context: Context) {
    set(FAYE_TOKEN, fayeToken,context)
}

fun getFayeToken(context: Context): String {
    return getString(FAYE_TOKEN, "",context) ?: ""
}

fun setPhoneNumber(phoneNumber: String,context: Context) {
    set(PHONE_NUMBER_KEY, phoneNumber,context)
}

fun getPhoneNumber(context: Context): String {
    return getString(PHONE_NUMBER_KEY, "",context) ?: ""
}

fun setPhoneVerificationShown(phoneVerificationShown: Boolean,context: Context) {
    set(PHONE_VERIFICATION_SHOWN, phoneVerificationShown,context)
}

fun isPhoneVerificationShown(context: Context): Boolean {
    return getBoolean(PHONE_VERIFICATION_SHOWN,context)
}

fun setUserModel(key: String, userModel: String,context: Context) {
    set(key, userModel,context)
}

fun getUserModel(key: String,context: Context): String? {
    return getString(key,context)
}

fun setLastChatTimestamp(time: String,context: Context) {
    val bigDecimal = BigDecimal(time)
    set(LAST_CHAT_TIMESTAMP, bigDecimal.toString(),context)
}

fun getLastChatTimestamp(context: Context): String {
    return getString(LAST_CHAT_TIMESTAMP, "0",context) ?: "0"
}

fun setEmail(email: String,context: Context) {
    set(EMAIL_KEY, email,context)
}

fun getEmail(context: Context): String {
    return getString(EMAIL_KEY, "",context) ?: ""
}

fun setFreeQuestionCount(count: Int,context: Context) {
    set(FREE_QUESTION_COUNT, count,context)
}

fun getFreeQuestionCount(context: Context): Int {
    return getInt(FREE_QUESTION_COUNT, 1,context)
}

fun getAntardashaFalKey(userId: String): String = ANTARDASHA_FAL_KEY + "_" + userId

fun setAntardashaFal(userKey: String, antardashaFal: String,context: Context) {
    val antardashaFalKey = getAntardashaFalKey(userKey)
    set(antardashaFalKey, antardashaFal,context)
}

fun getAntardashaFal(userKey: String,context: Context): String? {
    val antardashaFalKey = getAntardashaFalKey(userKey)
    return getString(antardashaFalKey, null,context)
}