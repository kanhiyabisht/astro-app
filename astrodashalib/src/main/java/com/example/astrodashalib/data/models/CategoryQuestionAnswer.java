package com.example.astrodashalib.data.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Created by himanshu on 11/10/17.
 */

public class CategoryQuestionAnswer  implements Serializable {

    @SerializedName("categoryName")
    public String categoryName;

    @SerializedName("firstRemedyPurchaseTimestamp")
    public long mFirstRemedyPurchaseTimestamp;

    @SerializedName("questionAnswer")
    public QuestionAnswer mQuestionAnswer;

    public CategoryQuestionAnswer(String categoryName, QuestionAnswer questionAnswer, long firstRemedyPurchaseTimestamp) {
        this.categoryName = categoryName;
        mQuestionAnswer = questionAnswer;
        this.mFirstRemedyPurchaseTimestamp = firstRemedyPurchaseTimestamp;
    }
}
