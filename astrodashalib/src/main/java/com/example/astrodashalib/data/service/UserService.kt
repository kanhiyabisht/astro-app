package com.example.astrodashalib.data.service

import com.example.astrodashalib.data.models.CrmUser
import com.example.astrodashalib.data.models.StatusModel
import com.example.astrodashalib.data.models.UserModel
import retrofit2.http.*
import rx.Observable

/**
 * Created by himanshu on 26/09/17.
 */
interface UserService {

    @POST("/appUser")
    fun registerUser(@Header("key") secretValue: String, @Body userModel: UserModel, @Header("user_id") userId: String?): Observable<UserModel>

    @GET("/appUser/phone/{phone}")
    fun getUser(@Header("key") secretValue: String,@Path("phone") phoneId: String, @Header("user_id") userId: String?): Observable<UserModel>

    @PUT("/appUser/{user_id}")
    fun updateUser(@Header("key") secretValue: String, @Path("user_id") userId: String, @Body userModel: UserModel, @Header("user_id") userid: String?): Observable<StatusModel>


    @GET("/checkUserLogin/{user_id}")
    fun getCrmUserId(@Header("key") secretValue: String,@Path("user_id") userId: String, @Header("user_id") userid: String?): Observable<CrmUser>
}