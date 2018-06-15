package com.example.astrodashalib.chat;

import android.graphics.Bitmap;

import com.example.astrodashalib.data.models.UserModel;


/**
 * Created by abhishek on 07/12/15.
 */
public interface SaveImageInterface {

    void imageSaved(Bitmap bm);
    void imageSaved(Bitmap bm, UserModel model);
}
