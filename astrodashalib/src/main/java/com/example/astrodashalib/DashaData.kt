package com.example.astrodashalib

import android.content.ContentValues.TAG
import android.util.Log
import com.example.astrodashalib.service.RestProvider
import com.example.astrodashalib.interfaces.DashaCallback
import com.example.astrodashalib.model.*
import com.example.astrodashalib.model.response.*
import com.example.astrodashalib.utils.Utils
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.ArrayList

public class DashaData {

    internal var mCompositeSubscription = CompositeSubscription()

    internal var mYogYutiResponse: YogYutiResponse? = null
    internal var mHoroscopeResponse: HoroscopeResponse? = null
    internal var mHealthResponse: HealthResponse? = null
    internal var mMarriedLifeResponse: MarriedLifeResponse? = null
    internal var mOccupationResponse: OccupationResponse? = null
    internal var mParentsResponse: ParentsResponse? = null
    internal var mChildrenResponse: ChildrenResponse? = null
    internal var mCurrentMahadashaFalResponse: CurrentMahadashaFalResponse? = null
    internal var mCurrentAntardashaFalResponse: CurrentAntardashaFalResponse? = null


    fun getPlaces(searchText: String, callback: DashaCallback<List<Place>>) {
        val encodedSearchText = searchText.trim().replace(" ", "%20")
        if (encodedSearchText.isNotEmpty()) {
            mCompositeSubscription.add(RestProvider.getDashaService().searchPlaces(searchText, "Ind.")
                    .retryWhen(RetryWithDelay(3, 2000))
                    .subscribe(object : Subscriber<List<Place>>() {
                        override fun onCompleted() {
                        }

                        override fun onError(e: Throwable) {
                            callback.onError(e)
                        }

                        override fun onNext(places: List<Place>) {
                            if (places.isNotEmpty())
                                callback.onSuccess(places)
                            else
                                callback.onError(RemoteDataNotFoundException())
                        }
                    })
            )
        } else
            callback.onError(SearchTextEmptyException())
    }

    fun getPlacesAsync(searchText: String, callback: DashaCallback<List<Place>>) {
        val encodedSearchText = searchText.trim().replace(" ", "%20")
        if (encodedSearchText.isNotEmpty()) {
            mCompositeSubscription.add(RestProvider.getDashaService().searchPlaces(searchText, "Ind.")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .retryWhen(RetryWithDelay(3, 2000))
                    .subscribe(object : Subscriber<List<Place>>() {
                        override fun onCompleted() {
                        }

                        override fun onError(e: Throwable) {
                            callback.onError(e)
                        }

                        override fun onNext(places: List<Place>) {
                            if (places.isNotEmpty())
                                callback.onSuccess(places)
                            else
                                callback.onError(RemoteDataNotFoundException())
                        }
                    })
            )
        } else
            callback.onError(SearchTextEmptyException())
    }


    fun getGenerateNewResponse(merchantId: String,generateNewRequestBody: GenerateNewRequestBody, callback: DashaCallback<GenerateNewResponse>) {
        try {
            mCompositeSubscription.add(RestProvider.getDashaService().getMahaDashaResponse(merchantId, Utils.KEY_VALUE, generateNewRequestBody)
                    .retryWhen(RetryWithDelay(3, 2000))
                    .subscribe(object : Subscriber<GenerateNewResponse>() {
                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable) {
                            callback.onError(e)
                        }

                        override fun onNext(generateNewResponseBody: GenerateNewResponse) {
                            callback.onSuccess(generateNewResponseBody)

                        }
                    }))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun getGenerateNewResponseAsync(merchantId: String, generateNewRequestBody: GenerateNewRequestBody, callback: DashaCallback<GenerateNewResponse>) {
        try {
            mCompositeSubscription.add(RestProvider.getDashaService().getMahaDashaResponse(merchantId, Utils.KEY_VALUE, generateNewRequestBody)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .retryWhen(RetryWithDelay(3, 2000))
                    .subscribe(object : Subscriber<GenerateNewResponse>() {
                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable) {
                            callback.onError(e)
                        }

                        override fun onNext(generateNewResponseBody: GenerateNewResponse) {
                            callback.onSuccess(generateNewResponseBody)
                        }
                    }))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun getCurrentMahadashaFal(merchantId: String, currentMahadashaFalRequestBody: CurrentMahadashaFalRequestBody, callback: DashaCallback<CurrentMahadashaFalResponse>) {

        try {
            mCompositeSubscription.add(RestProvider.getDashaService().getCurrentMahadashaFalText(merchantId, Utils.KEY_VALUE, currentMahadashaFalRequestBody)
                    .subscribe(object : Subscriber<CurrentMahadashaFalResponse>() {
                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable) {
                            callback.onError(e)

                        }

                        override fun onNext(currentMahadashaFalResponse: CurrentMahadashaFalResponse) {
                            mCurrentMahadashaFalResponse = currentMahadashaFalResponse
                            callback.onSuccess(mCurrentMahadashaFalResponse!!)

                        }
                    }))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun getCurrentMahadashaFalAsync(merchantId: String, currentMahadashaFalRequestBody: CurrentMahadashaFalRequestBody, callback: DashaCallback<CurrentMahadashaFalResponse>) {

        try {
            mCompositeSubscription.add(RestProvider.getDashaService().getCurrentMahadashaFalText(merchantId, Utils.KEY_VALUE, currentMahadashaFalRequestBody)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Subscriber<CurrentMahadashaFalResponse>() {
                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable) {
                            callback.onError(e)

                        }

                        override fun onNext(currentMahadashaFalResponse: CurrentMahadashaFalResponse) {
                            mCurrentMahadashaFalResponse = currentMahadashaFalResponse
                            callback.onSuccess(mCurrentMahadashaFalResponse!!)

                        }
                    }))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    fun getCurrentAntardashaFal(merchantId: String, currentAntardashaFalRequestBody: CurrentAntardashaFalRequestBody, callback: DashaCallback<CurrentAntardashaFalResponse>) {

        try {

            mCompositeSubscription.add(RestProvider.getDashaService().getCurrentAntardashaFalText(merchantId, Utils.KEY_VALUE, currentAntardashaFalRequestBody)
                    .subscribe(object : Subscriber<CurrentAntardashaFalResponse>() {
                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable) {
                            callback.onError(e)

                        }

                        override fun onNext(currentAntardashaFalResponse: CurrentAntardashaFalResponse) {
                            mCurrentAntardashaFalResponse = currentAntardashaFalResponse
                            callback.onSuccess(mCurrentAntardashaFalResponse!!)

                        }
                    }))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun getCurrentAntardashaFalAsync(merchantId: String, currentAntardashaFalRequestBody: CurrentAntardashaFalRequestBody, callback: DashaCallback<CurrentAntardashaFalResponse>) {

        try {

            mCompositeSubscription.add(RestProvider.getDashaService().getCurrentAntardashaFalText(merchantId, Utils.KEY_VALUE, currentAntardashaFalRequestBody)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Subscriber<CurrentAntardashaFalResponse>() {
                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable) {
                            callback.onError(e)

                        }

                        override fun onNext(currentAntardashaFalResponse: CurrentAntardashaFalResponse) {
                            mCurrentAntardashaFalResponse = currentAntardashaFalResponse
                            callback.onSuccess(mCurrentAntardashaFalResponse!!)

                        }
                    }))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    fun getYogYutiResponse(merchantId: String, yogYutiRequestBody: YogYutiRequestBody, callback: DashaCallback<YogYutiResponse>) {

        try {

            mCompositeSubscription.add(RestProvider.getDashaService().getOnlyYogYutiText(merchantId, Utils.KEY_VALUE, yogYutiRequestBody)
                    .subscribe(object : Subscriber<YogYutiResponse>() {
                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable) {
                            callback.onError(e)

                        }

                        override fun onNext(yogYutiResponse: YogYutiResponse) {
                            mYogYutiResponse = yogYutiResponse
                            callback.onSuccess(mYogYutiResponse!!)

                        }
                    }))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun getYogYutiResponseAsync(merchantId: String, yogYutiRequestBody: YogYutiRequestBody, callback: DashaCallback<YogYutiResponse>) {

        try {

            mCompositeSubscription.add(RestProvider.getDashaService().getOnlyYogYutiText(merchantId, Utils.KEY_VALUE, yogYutiRequestBody)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Subscriber<YogYutiResponse>() {
                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable) {
                            callback.onError(e)

                        }

                        override fun onNext(yogYutiResponse: YogYutiResponse) {
                            mYogYutiResponse = yogYutiResponse
                            callback.onSuccess(mYogYutiResponse!!)

                        }
                    }))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun getHoroscopeResponse(merchantId: String, horoscopeRequestBody: HoroscopeRequestBody, callback: DashaCallback<HoroscopeResponse>) {

        try {

            mCompositeSubscription.add(RestProvider.getDashaService().getHoroscope(merchantId, Utils.KEY_VALUE, horoscopeRequestBody)
                    .subscribe(object : Subscriber<HoroscopeResponse>() {
                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable) {
                            callback.onError(e)

                        }

                        override fun onNext(horoscopeResponse: HoroscopeResponse) {
                            mHoroscopeResponse = horoscopeResponse
                            callback.onSuccess(mHoroscopeResponse!!)

                        }
                    }))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun getHoroscopeResponseAsync(merchantId: String, horoscopeRequestBody: HoroscopeRequestBody, callback: DashaCallback<HoroscopeResponse>) {

        try {

            mCompositeSubscription.add(RestProvider.getDashaService().getHoroscope(merchantId, Utils.KEY_VALUE, horoscopeRequestBody)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Subscriber<HoroscopeResponse>() {
                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable) {
                            callback.onError(e)

                        }

                        override fun onNext(horoscopeResponse: HoroscopeResponse) {
                            mHoroscopeResponse = horoscopeResponse
                            callback.onSuccess(mHoroscopeResponse!!)

                        }
                    }))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    fun getHealthResponse(merchantId: String, predictionRequestBody: PredictionRequestBody, callback: DashaCallback<HealthResponse>) {

        try {

            mCompositeSubscription.add(RestProvider.getDashaService().getOnlyHealthText(merchantId, Utils.KEY_VALUE, predictionRequestBody)
                    .subscribe(object : Subscriber<HealthResponse>() {
                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable) {
                            callback.onError(e)

                        }

                        override fun onNext(healthResponse: HealthResponse) {
                            mHealthResponse = healthResponse
                            callback.onSuccess(mHealthResponse!!)

                        }
                    }))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun getHealthResponseAsync(merchantId: String, predictionRequestBody: PredictionRequestBody, callback: DashaCallback<HealthResponse>) {

        try {

            mCompositeSubscription.add(RestProvider.getDashaService().getOnlyHealthText(merchantId, Utils.KEY_VALUE, predictionRequestBody)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Subscriber<HealthResponse>() {
                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable) {
                            callback.onError(e)

                        }

                        override fun onNext(healthResponse: HealthResponse) {
                            mHealthResponse = healthResponse
                            callback.onSuccess(mHealthResponse!!)

                        }
                    }))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    fun getMarriedLifeResponse(merchantId: String, predictionRequestBody: PredictionRequestBody, callback: DashaCallback<MarriedLifeResponse>) {

        try {

            mCompositeSubscription.add(RestProvider.getDashaService().getOnlyMarriedLifeText(merchantId, Utils.KEY_VALUE, predictionRequestBody)
                    .subscribe(object : Subscriber<MarriedLifeResponse>() {
                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable) {
                            callback.onError(e)

                        }

                        override fun onNext(marriedLifeResponse: MarriedLifeResponse) {
                            mMarriedLifeResponse = marriedLifeResponse
                            callback.onSuccess(mMarriedLifeResponse!!)

                        }
                    }))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun getMarriedLifeResponseAsync(merchantId: String, predictionRequestBody: PredictionRequestBody, callback: DashaCallback<MarriedLifeResponse>) {
        try {
            mCompositeSubscription.add(RestProvider.getDashaService().getOnlyMarriedLifeText(merchantId, Utils.KEY_VALUE, predictionRequestBody)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Subscriber<MarriedLifeResponse>() {
                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable) {
                            callback.onError(e)
                        }

                        override fun onNext(marriedLifeResponse: MarriedLifeResponse) {
                            mMarriedLifeResponse = marriedLifeResponse
                            callback.onSuccess(mMarriedLifeResponse!!)

                        }
                    }))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    fun getOccupationResponse(merchantId:String,predictionRequestBody: PredictionRequestBody, callback: DashaCallback<OccupationResponse>) {

        try {

            mCompositeSubscription.add(RestProvider.getDashaService().getOnlyOccupationText(merchantId, Utils.KEY_VALUE, predictionRequestBody)
                    .subscribe(object : Subscriber<OccupationResponse>() {
                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable) {
                            callback.onError(e)
                        }

                        override fun onNext(occupationResponse: OccupationResponse) {
                            mOccupationResponse = occupationResponse
                            callback.onSuccess(mOccupationResponse!!)

                        }
                    }))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun getOccupationResponseAsync(merchantId:String,predictionRequestBody: PredictionRequestBody, callback: DashaCallback<OccupationResponse>) {

        try {
            mCompositeSubscription.add(RestProvider.getDashaService().getOnlyOccupationText(merchantId, Utils.KEY_VALUE, predictionRequestBody)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Subscriber<OccupationResponse>() {
                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable) {
                            callback.onError(e)
                        }

                        override fun onNext(occupationResponse: OccupationResponse) {
                            mOccupationResponse = occupationResponse
                            callback.onSuccess(mOccupationResponse!!)

                        }
                    }))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    fun getParentsResponse(merchantId:String,predictionRequestBody: PredictionRequestBody, callback: DashaCallback<ParentsResponse>) {

        try {

            mCompositeSubscription.add(RestProvider.getDashaService().getOnlyParentsText(merchantId, Utils.KEY_VALUE, predictionRequestBody)
                    .subscribe(object : Subscriber<ParentsResponse>() {
                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable) {
                            callback.onError(e)

                        }

                        override fun onNext(parentsResponse: ParentsResponse) {

                            mParentsResponse = parentsResponse
                            callback.onSuccess(mParentsResponse!!)

                        }
                    }))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun getParentsResponseAsync(merchantId:String,predictionRequestBody: PredictionRequestBody, callback: DashaCallback<ParentsResponse>) {

        try {

            mCompositeSubscription.add(RestProvider.getDashaService().getOnlyParentsText(merchantId, Utils.KEY_VALUE, predictionRequestBody)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Subscriber<ParentsResponse>() {
                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable) {
                            callback.onError(e)

                        }

                        override fun onNext(parentsResponse: ParentsResponse) {

                            mParentsResponse = parentsResponse
                            callback.onSuccess(mParentsResponse!!)

                        }
                    }))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    fun getChildrenResponse(merchantId:String,predictionRequestBody: PredictionRequestBody, callback: DashaCallback<ChildrenResponse>) {

        try {

            mCompositeSubscription.add(RestProvider.getDashaService().getOnlyChildrenText(merchantId, Utils.KEY_VALUE, predictionRequestBody)
                    .subscribe(object : Subscriber<ChildrenResponse>() {
                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable) {
                            callback.onError(e)

                        }

                        override fun onNext(childrenResponse: ChildrenResponse) {
                            mChildrenResponse = childrenResponse
                            callback.onSuccess(mChildrenResponse!!)

                        }
                    }))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun getChildrenResponseAsync(merchantId:String,predictionRequestBody: PredictionRequestBody, callback: DashaCallback<ChildrenResponse>) {

        try {

            mCompositeSubscription.add(RestProvider.getDashaService().getOnlyChildrenText(merchantId, Utils.KEY_VALUE, predictionRequestBody)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Subscriber<ChildrenResponse>() {
                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable) {
                            callback.onError(e)

                        }

                        override fun onNext(childrenResponse: ChildrenResponse) {
                            mChildrenResponse = childrenResponse
                            callback.onSuccess(mChildrenResponse!!)

                        }
                    }))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun clear() {
        mCompositeSubscription.clear()
    }

}
