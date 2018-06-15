package com.example.astrodashalib.data.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by himanshu on 12/10/17.
 */

public class Category implements Serializable {

    @SerializedName("categoryName")
    public String categoryName;

    @SerializedName("image")
    public String categoryImage;

    @SerializedName("questionAnswer")
    public ArrayList<QuestionAnswer> questionAnswerArrayList;

    public Category() {
    }

    public Category(String categoryName, ArrayList<QuestionAnswer> questionAnswerArrayList) {
        this.categoryName = categoryName;
        this.questionAnswerArrayList = questionAnswerArrayList;
    }

    public static ArrayList<Category> getDefaultList() {

        ArrayList<QuestionAnswer> defaultList = new ArrayList<>();
        defaultList.add(new QuestionAnswer("", new ArrayList<Answer>()));

        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category("health", defaultList));
        categories.add(new Category("daughter", defaultList));
        categories.add(new Category("relatives", defaultList));
        categories.add(new Category("friend", defaultList));
        categories.add(new Category("parents", defaultList));
        categories.add(new Category("sister", defaultList));
        categories.add(new Category("brother", defaultList));
        categories.add(new Category("marriage", defaultList));
        categories.add(new Category("education", defaultList));
        categories.add(new Category("son", defaultList));
        categories.add(new Category("wealth", defaultList));
        categories.add(new Category("nature", defaultList));
        categories.add(new Category("occupation", defaultList));
        categories.add(new Category("Travel", defaultList));
        categories.add(new Category("Precaution", defaultList));
        categories.add(new Category("others", defaultList));
        categories.add(new Category("family", defaultList));
        categories.add(new Category("love life", defaultList));
        return categories;
    }

    public static boolean isCategoryDefault(Category category) {
        return category.questionAnswerArrayList.size() == 1 && category.questionAnswerArrayList.get(0).question.isEmpty();
    }
}

