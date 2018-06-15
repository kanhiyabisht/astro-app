package com.example.astrodashalib.data.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Created by himanshu on 11/10/17.
 */

public class Answer implements Serializable {

    public static final String SIMPLE_PREDICTION_TXT = "अभी तक लिखित अच्छे हालात तो आपके जीवन में घटित होंगे ही परंतु आगे दिए कुछ बुरे योगों का फल भी साथ में घटित होगा, जिनका उपाय करना ज़रूरी होगा";
    public static final String ANTARDASHA_PREDICTION_TXT = "अभी तक लिखित अच्छे हालात तो आपके जीवन में इस वक़्त घटित होंगे ही परंतु इसी वक़्त आगे दिए कुछ बुरे योगों का फल भी साथ में घटित होगा, जिनका उपाय करना ज़रूरी होगा";

    @SerializedName("rule_question_id")
    public int ruleQuestionId;

    @SerializedName("rule_id")
    public String ruleId = "";

    @SerializedName("question_id")
    public Integer questionId;

    @SerializedName("tag_name")
    public String tagName = "";

    @SerializedName("question_hindi")
    public String questionHindi = "";

    @SerializedName("answer_hindi")
    public String answerHindi = "";

    @SerializedName("answer_text")
    public String answer = "";

    @SerializedName("answer_english")
    public String answerEnglish = "";

    @SerializedName("created_by")
    public String createdBy = "";

    @SerializedName("created_date")
    public String createdDate = "";

    @SerializedName("updated_by")
    public String updatedBy = "";

    @SerializedName("status_id")
    public int statusId ;

    @SerializedName("rule_text")
    public String ruleText ;

    @SerializedName("status_updated_by")
    public String statusUpdatedBy = "";

    @SerializedName("status_updated_date")
    public String statusUpdatedDate = "";

    @SerializedName("rating")
    public int rating = 0;

    @SerializedName("gender")
    public int gender ;

    @SerializedName("updated_date")
    public String updatedDate;

    @SerializedName("yogType")
    public String yogType;

    @SerializedName("purchaseTimestamp")
    public String purchaseTimestamp;

    public Answer(String answer, int rating, String ruleId) {
        this.answer = answer;
        this.rating = rating;
        this.ruleId = ruleId;
        this.questionId = -1;
    }

}
