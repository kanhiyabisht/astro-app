package com.example.astrodashalib.chat.messageController

import android.content.Context
import android.util.Log
import com.example.astrodashalib.chat.notificationHandler.ChatNotificationHelper
import com.example.astrodashalib.data.models.ChatModel
import com.example.astrodashalib.data.models.DbStatusModel
import com.example.astrodashalib.data.models.UserModel
import com.example.astrodashalib.generic.*
import com.example.astrodashalib.helper.*
import com.example.astrodashalib.localDB.DatabaseHelper
import com.example.astrodashalib.localDB.DbConstants
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

/**
 * Created by himanshu on 04/10/17.
 */
class MessageController(val context: Context) : MessageControllerInterface {

    var timestamp: Double = 0.0
    var fromUser: UserModel? = Gson().fromJson(getUserModel(getLatestUserShown(context.applicationContext),context.applicationContext), UserModel::class.java)
    var genericController: GenericControllerInterface = GenericControllerFactory.getGenericControllerObj(context)


    override fun sendChat(sendData: String, chatUserId: String, chatUserImage: String, chatUserName: String) {
        val toUserId = chatUserId
        timestamp = DateTimeUtil.getCurrentTimestampSeconds().toDouble()
        try {
            fromUser = Gson().fromJson(getUserModel(getLatestUserShown(context.applicationContext),context.applicationContext), UserModel::class.java)
            Log.e(LOG_TAG, timestamp.toString())

            val chatModel = ChatModel(sendData, toUserId, getUserId(context.applicationContext), fromUser?.userName, timestamp, false)
            chatModel.apply {
                isIncoming = false
                myId = fromUserId
                buddyId = toUserId
                chatStatus = DbConstants.STATUS_SENDING
                readTimestamp = 0.0
                deliveredTimestamp = 0.0
            }

            ChatNotificationHelper.isNewChat(chatModel.localId)
            addChatLocally(chatModel) { error, model ->
                BroadcastUtils.refreshChatList(chatModel, context)
            }

            postChatOnServer(chatModel) { error, `object` ->
                Log.e(LOG_TAG, "postChat")
                updateChatStatusOfSentChats(timestamp, chatUserId, getUserId(context.applicationContext)) { error1, `object1` ->
                    Log.e(LOG_TAG, "updateChat")
                    //send broadcast
                    BroadcastUtils.refreshChatStatus(context, 1.0)
                }
            }


        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(LOG_TAG, e.message)
        }

    }


    override fun saveChat(sendData: String?, chatUserId: String?, chatUserImage: String?, chatUserName: String?): ChatModel {
        val toUserId = chatUserId
        timestamp = DateTimeUtil.getCurrentTimestampSeconds().toDouble()
        fromUser = Gson().fromJson(getUserModel(getLatestUserShown(context.applicationContext),context.applicationContext), UserModel::class.java)
        Log.e(LOG_TAG, timestamp.toString())

        val chatModel = ChatModel(sendData, toUserId, getUserId(context.applicationContext), fromUser?.userName, timestamp, false)
        try {
            chatModel.apply {
                isIncoming = false
                myId = fromUserId
                buddyId = toUserId
                chatStatus = DbConstants.STATUS_NOT_PAID
                readTimestamp = 0.0
                deliveredTimestamp = 0.0
            }

            ChatNotificationHelper.isNewChat(chatModel.localId)
            addChatLocally(chatModel) { error, model ->
                BroadcastUtils.refreshChatList(chatModel, context)

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return chatModel
    }


    override fun sendChat(chatModel: ChatModel) {
        val chatModel1 = ChatModel()
        chatModel1.chatStatus = DbConstants.STATUS_SENDING
        chatModel1.sentTimestamp = DateTimeUtil.getCurrentTimestampSeconds().toDouble()
        val meBuddyHashMap = HashMap<String, String>()
        meBuddyHashMap.put(DatabaseHelper.COLUMN_NAME_LOCAL_ID + "=", chatModel.localId)
        val jsonArray = LocalDbFactory.getJsonArray(meBuddyHashMap)
        updateChatLocally(jsonArray, chatModel1) { error, `object` ->
            BroadcastUtils.refreshChatStatus(context, 1.0)
        }
        postChatOnServer(chatModel) { error, `object` ->
            updateChatStatusOfSentChats(chatModel.localId, object : GenericCallback {
                override fun callback(error: JSONObject?, `object`: Any?) {
                    //send broadcast
                    BroadcastUtils.refreshChatStatus(context, 1.0)
                }
            })
        }
    }

    override fun postChatOnServer(chatModel: ChatModel?, genericCallback: GenericCallback?) {
        try {
            val chatParams = chatModel?.toJson()
            chatParams?.put("paid", getFreeQuestionCount(context.applicationContext) != 1)
            val chatJsonObj = JSONObject()
            chatJsonObj.apply {
                put("faye_token", getFayeToken(context.applicationContext))
                put("type", "message")
                put("action", "sendChat")
                put("params", chatParams)
            }
            genericController.serverPost(false, chatJsonObj, genericCallback)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun updateRecievedChatStatus(chatModel: ChatModel, loginUserId: String, chatUserId: String, genericCallback: GenericCallback?) {
        val meBuddyHashMap = HashMap<String, String>()
        meBuddyHashMap.apply {
            put(DatabaseHelper.COLUMN_NAME_MY_ID + "=", loginUserId)
            put(DatabaseHelper.COLUMN_NAME_BUDDY_ID + "=", chatUserId)
            put(DatabaseHelper.COLUMN_NAME_INCOMING + "=", DbConstants.STATUS_RECIEVED.toString())
            put(DatabaseHelper.COLUMN_NAME_CHAT_STATUS + "=", DbConstants.STATUS_NOTIFY.toString())
        }

        val jsonArray = LocalDbFactory.getJsonArray(meBuddyHashMap)
        updateChatLocally(jsonArray, chatModel, genericCallback)
    }

    override fun fetchAllChatsToSend(loginUserId: String, genericQueryCallback: GenericQueryCallback<*>?) {
        val hashMap = HashMap<String, String>()
        hashMap.apply {
            put(DatabaseHelper.COLUMN_NAME_MY_ID + "=", loginUserId)
            put(DatabaseHelper.COLUMN_NAME_CHAT_STATUS + "=", DbConstants.STATUS_SENDING.toString())
        }
        val jsonArray = LocalDbFactory.getJsonArray(hashMap)
        localChatQuery(jsonArray, genericQueryCallback)
    }

    override fun fetchChatsOfTwoUser(loginUserId: String, chatUserId: String, genericQueryCallback: GenericQueryCallback<*>?) {
        val hashMap = HashMap<String, String>()
        hashMap.apply {
            put(DatabaseHelper.COLUMN_NAME_MY_ID + "=", loginUserId)
            put(DatabaseHelper.COLUMN_NAME_BUDDY_ID + "=", chatUserId)
            put(DbConstants.SORT_DSEC, DatabaseHelper.COLUMN_NAME_SENT_TIMESTAMP)
            put(DbConstants.LIMIT, DbConstants.CHAT_LIMIT_COUNT.toString())
        }
        val jsonArray = LocalDbFactory.getJsonArray(hashMap)
        localChatQuery(jsonArray, genericQueryCallback)
    }

    override fun fetchChatsByTimestamp(loginUserId: String, timestamp: Long, genericQueryCallback: GenericQueryCallback<*>?) {
        val hashMap = HashMap<String, String>()
        hashMap.apply {
            put(DatabaseHelper.COLUMN_NAME_MY_ID + "=", loginUserId)
            put(DatabaseHelper.COLUMN_NAME_SENT_TIMESTAMP + ">=", timestamp.toString())
        }
        val jsonArray = LocalDbFactory.getJsonArray(hashMap)
        localChatQuery(jsonArray, genericQueryCallback)
    }

    override fun addChatLocally(chatModel: ChatModel, genericCallback: GenericCallback?) {
        genericController.addLocalChat(false, chatModel.toJsonDb(), genericCallback)
    }

    override fun updateChatLocally(jsonArray: JSONArray?, chatModel: ChatModel, genericCallback: GenericCallback?) {
        genericController.updateLocalChat(jsonArray, DbConstants.DB_UPDATE, false, chatModel.toJsonDb(), genericCallback)
    }

    override fun localChatQuery(jsonArray: JSONArray?, genericQueryCallback: GenericQueryCallback<*>?) {
        genericController.localChatQuery(jsonArray, DbConstants.DB_QUERY, false, genericQueryCallback)
    }

    override fun sendReadTimestamp(loginUserId: String?, chatUserId: String?) {
        try {
            val params = JSONObject()
            params.apply {
                put("reader_user_id", loginUserId)
                put("writer_user_id", chatUserId)
                put("read_timestamp", DateTimeUtil.getCurrentTimestampSeconds().toDouble())
            }

            val readTimestampJsonObj = JSONObject()
            readTimestampJsonObj.apply {
                put("type", "message")
                put("action", "updateReadTimestamp")
                put("params", params)
            }
            genericController.serverPost(false, readTimestampJsonObj) { error, `object` -> }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun getReadTimestamp(readerId: String, writerId: String, genericCallback: GenericCallback) {
        try {
            val params = JSONObject()
            params.apply {
                put("reader_user_id", readerId)
                put("writer_user_id", writerId)
            }

            val jsonObject = JSONObject()
            jsonObject.apply {
                put("type", "message")
                put("action", "getReadTimestamp")
                put("params", params)
            }

            genericController.serverPost(false, jsonObject) { error, `object` ->
                val response = `object` as JSONObject
                val result = response.optJSONObject("result")
                val readTimestamp = result.optDouble("read_timestamp")
                updateReadTimestampOfSentChats(readTimestamp, readerId, writerId, genericCallback)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    override fun updateReadTimestampOfSentChats(readTimestamp: Double, buddyId: String, myId: String, genericCallback: GenericCallback) {
        val chatModel = ChatModel()
        chatModel.apply {
            chatStatus = DbConstants.STATUS_READ
            this.readTimestamp = readTimestamp
        }
        val ME_BUDDY_MAP = HashMap<String, String>()
        ME_BUDDY_MAP.apply {
            put(DatabaseHelper.COLUMN_NAME_BUDDY_ID + "=", buddyId)
            put(DatabaseHelper.COLUMN_NAME_MY_ID + "=", myId)
            put(DatabaseHelper.COLUMN_NAME_CHAT_STATUS + "!=", DbConstants.STATUS_READ.toString())
            put(DatabaseHelper.COLUMN_NAME_SENT_TIMESTAMP + "<=", readTimestamp.toString())
            put(DatabaseHelper.COLUMN_NAME_INCOMING + "=", DbConstants.STATUS_SEND.toString())
        }

        val jsonArray = LocalDbFactory.getJsonArray(ME_BUDDY_MAP)
        updateChatLocally(jsonArray, chatModel) { error, model ->
            val statusModel = model as DbStatusModel
            if (statusModel.statusId > 0 && statusModel.errorMesssage.isEmpty())
                genericCallback.callback(null, readTimestamp)
        }
    }

    fun updateChatStatusOfSentChats(localId: String, genericCallback: GenericCallback?) {
        val chatModel = ChatModel()
        chatModel.chatStatus = DbConstants.STATUS_UNREAD
        val ME_BUDDY_MAP = HashMap<String, String>()
        ME_BUDDY_MAP.apply {
            put(DatabaseHelper.COLUMN_NAME_LOCAL_ID + "=", localId)
        }

        val jsonArray = LocalDbFactory.getJsonArray(ME_BUDDY_MAP)
        updateChatLocally(jsonArray, chatModel) { error, model ->
            val statusModel = model as DbStatusModel
            if (statusModel.statusId > 0 && statusModel.errorMesssage.isEmpty())
                genericCallback?.callback(null, "")
        }
    }


    override fun updateChatStatusOfSentChats(sentTimestamp: Double, buddyId: String, myId: String, genericCallback: GenericCallback?) {
        val chatModel = ChatModel()
        chatModel.chatStatus = DbConstants.STATUS_UNREAD
        val ME_BUDDY_MAP = HashMap<String, String>()
        ME_BUDDY_MAP.apply {
            put(DatabaseHelper.COLUMN_NAME_BUDDY_ID + "=", buddyId)
            put(DatabaseHelper.COLUMN_NAME_MY_ID + "=", myId)
            put(DatabaseHelper.COLUMN_NAME_CHAT_STATUS + "=", DbConstants.STATUS_SENDING.toString())
            put(DatabaseHelper.COLUMN_NAME_SENT_TIMESTAMP + "<=", sentTimestamp.toString())
            put(DatabaseHelper.COLUMN_NAME_INCOMING + "=", DbConstants.STATUS_SEND.toString())
        }

        val jsonArray = LocalDbFactory.getJsonArray(ME_BUDDY_MAP)
        updateChatLocally(jsonArray, chatModel) { error, model ->
            val statusModel = model as DbStatusModel
            if (statusModel.statusId > 0 && statusModel.errorMesssage.isEmpty())
                genericCallback?.callback(null, "")
        }
    }

    override fun updateChatStatusOfSentChats(sentTimestamp: Double, myId: String, genericCallback: GenericCallback?) {
        val chatModel = ChatModel()
        chatModel.chatStatus = DbConstants.STATUS_UNREAD
        val ME_BUDDY_MAP = HashMap<String, String>()
        ME_BUDDY_MAP.apply {
            put(DatabaseHelper.COLUMN_NAME_MY_ID + "=", myId)
            put(DatabaseHelper.COLUMN_NAME_CHAT_STATUS + "=", DbConstants.STATUS_SENDING.toString())
            put(DatabaseHelper.COLUMN_NAME_SENT_TIMESTAMP + "<=", sentTimestamp.toString())
            put(DatabaseHelper.COLUMN_NAME_INCOMING + "=", DbConstants.STATUS_SEND.toString())
        }

        val jsonArray = LocalDbFactory.getJsonArray(ME_BUDDY_MAP)
        updateChatLocally(jsonArray, chatModel) { error, model ->
            val statusModel = model as DbStatusModel
            if (statusModel.statusId > 0 && statusModel.errorMesssage.isEmpty())
                genericCallback?.callback(null, "")
        }
    }

    override fun fetchUnreadRecievedChats(chatModel: ChatModel, genericQueryCallback: GenericQueryCallback<*>?) {
        val ME_BUDDY_MAP = HashMap<String, String>()
        ME_BUDDY_MAP.apply {
            put(DatabaseHelper.COLUMN_NAME_BUDDY_ID + "=", chatModel.buddyId)
            put(DatabaseHelper.COLUMN_NAME_MY_ID + "=", chatModel.myId)
            put(DatabaseHelper.COLUMN_NAME_CHAT_STATUS + "=", DbConstants.STATUS_NOTIFY.toString())
        }
        val meBuddyArr = LocalDbFactory.getJsonArray(ME_BUDDY_MAP)
        localChatQuery(meBuddyArr, genericQueryCallback)
    }

    companion object {

        @JvmField
        val LOG_TAG = MessageController::class.java.simpleName
    }
}