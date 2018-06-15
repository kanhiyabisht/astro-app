package com.example.astrodashalib.data.service

import com.example.astrodashalib.data.models.FayeToken
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import rx.Observable

/**
 * Created by himanshu on 29/09/17.
 */
interface FayeTokenService {

    @GET("/token/{user_id}")
    fun getFayeToken(@Path("user_id") userId: String,@Header("key") secretValue: String,  @Header("chat-key") chatKey: String?,@Header("chat-secret") chatSecret: String?): Observable<FayeToken>
}