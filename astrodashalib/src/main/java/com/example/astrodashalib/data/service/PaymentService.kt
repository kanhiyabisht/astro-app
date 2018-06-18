package com.example.astrodashalib.data.service

import com.example.astrodashalib.data.models.*
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import rx.Observable

/**
 * Created by himanshu on 09/10/17.
 */
interface PaymentService {

    @POST("/payment")
    fun postPaymentDetails(@Header("key") secretValue: String, @Body paymentDetail: PaymentDetail, @Header("user_id") userId: String): Observable<StatusModel>
}