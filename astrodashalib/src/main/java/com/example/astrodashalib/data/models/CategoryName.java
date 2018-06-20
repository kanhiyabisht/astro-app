package com.example.astrodashalib.data.models;

import java.io.Serializable;

/**
 * Created by himanshu on 11/10/17.
 */

public class CategoryName implements Serializable {
    public String categoryName;

    public CategoryName() {

    }

    public CategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return categoryName;
    }

    @Override
    public boolean equals(Object o) {
        return ((o instanceof CategoryName)) && this.categoryName.equals(((CategoryName) o).categoryName);
    }

}
