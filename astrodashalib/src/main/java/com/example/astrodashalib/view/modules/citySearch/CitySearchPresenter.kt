package com.example.astrodashalib.view.modules.citySearch

import android.util.Log
import com.example.astrodashalib.data.RetryWithDelay
import com.example.astrodashalib.helper.getUserId
import com.example.astrodashalib.model.Place
import com.example.astrodashalib.provider.rest.RestProvider.getBirthDetailService
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.ArrayList

/**
 * Created by himanshu on 25/09/17.
 */
class CitySearchPresenter : CitySearchContract.Presenter {

    var mView: CitySearchContract.View? = null
    val mCompositeSubscription = CompositeSubscription()

    override fun attachView(view: CitySearchContract.View) {
        mView = view
    }

    override fun detachView() {
        mView = null
        mCompositeSubscription.clear()
    }

    override fun loadPlaces(searchText: String, countryCode: String) {
        try {

            mView?.showLoadingOfPlacesList()
            mCompositeSubscription.add(getBirthDetailService().searchPlaces(searchText, countryCode, getUserId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .retryWhen(RetryWithDelay(3, 2000))
                    .subscribe({ places ->
                        Log.e(TAG, "onPlacesNext" + Thread.currentThread())
                        mView?.let {
                            if (places == null || places.isEmpty())
                                it.showNoResultFoundView()
                            else
                                it.showPlaces(places as ArrayList<Place>)
                        }

                    }, { e ->
                        Log.e(TAG, "onPlacesError")
                        mView?.errorPlaces(e)

                    })
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onPlaceSelected(place: Place) {
//        place.timeZone = "82.30"
//        place.lat = place.latitude.replace("N", "")
//        place.lon = place.longitude.replace("E", "")
        mView?.setPlaceLatLng(place)
    }

    companion object {

        @JvmField
        val TAG = "CitySearchPresenter"
    }
}