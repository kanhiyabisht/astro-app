package com.example.astrodashalib.helper

import android.content.Context
import com.google.gson.Gson
import com.example.astrodashalib.data.models.UserModel
import org.json.JSONObject


/**
 * Created by himanshu on 29/09/17.
 */
object Util {

    @JvmField
    val USER_IMAGE="https://lh3.googleusercontent.com/0MLZOE65TMERUEo9SZPFJpxNRHmWrLYftarhmKAk_8FQkvg0tmj3GmfRGhEzB-lMvrA=w300"

    @JvmField
    val FAYE_URL = "wss://stage-chat.astroscience-backend.com:443/chatService"

    @JvmField
    val CHAT_BASE_URL = "https://stage-chat.astroscience-backend.com/"

    @JvmStatic
    fun getExtension(context: Context) :JSONObject{
        val userSchemaObj = JSONObject()
        try {
            val userModel = Gson().fromJson(getUserModel(getUserId(context),context), UserModel::class.java)
            val userId = getUserId(context)
            userSchemaObj.apply {
//                put("user_id", "5647070f779447110a3c37fa")
                put("user_id", userId)
                put("name", userModel.userName)
                put("image", USER_IMAGE)
                put("faye_token", getFayeToken(context))
                put("type", 0)
                put("token", getDeviceId(context))
//                put("token", "1234")
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return userSchemaObj
    }
}