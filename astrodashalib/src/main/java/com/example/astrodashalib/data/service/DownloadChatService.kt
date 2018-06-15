package com.example.astrodashalib.data.service

import com.example.astrodashalib.data.models.ChatModel
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import rx.Observable

/**
 * Created by himanshu on 03/10/17.
 */
interface DownloadChatService {

    @GET("/chats")
    fun downloadAllChats(@Query("user_id") userId: String, @Header("faye-token") fayeToken: String): Observable<ArrayList<ChatModel>>

    @GET("/chats")
    fun downloadChatsByTimestamp(@Query("user_id") userId: String, @Query("timestamp") timestamp: String, @Header("faye-token") fayeToken: String): Observable<ArrayList<ChatModel>>
}