package com.example.astrodashalib.view.modules.citySearch

import com.example.astrodashalib.model.Place
import com.example.astrodashalib.view.base.BasePresenter
import com.example.astrodashalib.view.base.BaseView
import java.util.ArrayList

/**
 * Created by himanshu on 25/09/17.
 */
interface CitySearchContract {
    interface View : BaseView {

        fun showLoadingOfPlacesList()

        fun showPlaces(places: ArrayList<Place>)

        fun errorPlaces(e: Throwable)

        fun showNoResultFoundView()

        fun setPlaceLatLng(place: Place)
    }

    interface Presenter : BasePresenter<View> {
        fun loadPlaces(searchText: String, countryCode: String)

        fun onPlaceSelected(place: Place)
    }
}