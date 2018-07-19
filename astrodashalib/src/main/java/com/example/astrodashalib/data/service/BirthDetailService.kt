package com.example.astrodashalib.data.service


import com.example.astrodashalib.model.Place
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import rx.Observable

/**
 * Created by himanshu on 25/09/17.
 */
interface BirthDetailService {

    @GET("/getAllPlaces/{search_text}/{countryCode}")
    fun searchPlaces(@Path(value = "search_text", encoded = true) searchText: String, @Path("countryCode") countryCode: String, @Header("user_id") userId: String?): Observable<List<Place>>
}