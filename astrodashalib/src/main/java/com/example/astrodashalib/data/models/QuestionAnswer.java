package com.example.astrodashalib.data.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by himanshu on 11/10/17.
 */

public class QuestionAnswer implements Serializable {

    @SerializedName("question_hindi")
    public String question;

    @SerializedName("yogType")
    public String yogType;

    @SerializedName("question_id")
    public Integer questionId;

    @SerializedName("answers")
    public ArrayList<Answer> answerArrayList;

    public String categoryName;

    public QuestionAnswer(String question, Integer questionId, ArrayList<Answer> answerArrayList, String yogType) {
        this.question = question;
        this.questionId = questionId;
        this.answerArrayList = answerArrayList;
        this.yogType = yogType;
    }

    public QuestionAnswer(String question, Integer questionId, ArrayList<Answer> answerArrayList, String yogType, String categoryName) {
        this.question = question;
        this.yogType = yogType;
        this.questionId = questionId;
        this.answerArrayList = answerArrayList;
        this.categoryName = categoryName;
    }

    public QuestionAnswer(String question, ArrayList<Answer> answerArrayList) {
        this.question = question;
        this.answerArrayList = answerArrayList;
    }
}
