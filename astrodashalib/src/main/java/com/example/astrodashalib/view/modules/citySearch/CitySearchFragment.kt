package com.example.astrodashalib.view.modules.citySearch

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.example.astrodashalib.*
import com.example.astrodashalib.helper.Bundler
import com.example.astrodashalib.model.Place
import com.example.astrodashalib.view.adapter.place.PlaceAdapter
import kotlinx.android.synthetic.main.fragment_city_search.*
import java.util.ArrayList


class CitySearchFragment : Fragment(), CitySearchContract.View, TextWatcher, PlaceAdapter.OnPlaceSelected {


    var mListener: OnCitySearchFragmentInteractionListener? = null
    var mPresenter: CitySearchContract.Presenter? = null
    var countryCode = ""
    var mPlaceArrayList = ArrayList<Place>()
    var mPlaceSelected: Place? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = CitySearchPresenter()
        if (arguments != null)
            countryCode = arguments.getString(COUNTRY_CODE, "")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = container?.inflate(R.layout.fragment_city_search)
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mPresenter?.attachView(this)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        try {
            super.onViewCreated(view, savedInstanceState)
            mParentLayout.let {
                it.setOnClickListener {  }
            }
            searchBarTv.apply {
                isFocusable = true
                addTextChangedListener(this@CitySearchFragment)
                threshold = 2
            }
            clearAllIv.setOnClickListener {
                try {
                    searchBarTv.setText("")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            searchBarTvNote.visible()
            listView.gone()
            noResultCardView.gone()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun showLoadingOfPlacesList() {
        listView.visible()
        searchBarTvNote.gone()
        noResultCardView.gone()
        val place = Place()
        place.isProgressBar = true
        mPlaceArrayList = ArrayList<Place>()
        mPlaceArrayList.add(place)
        listView.adapter = PlaceAdapter(context, mPlaceArrayList, this)
    }


    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        try {
            clearAllIv.visibility = if (s.length > 0) View.VISIBLE else View.GONE
            if (s.length > searchBarTv.threshold) {
                if (context.applicationContext.isNetworkAvailable())
                    mPresenter?.loadPlaces(s.toString().replace(" ", "%20"), countryCode)
                else {
                    mParentLayout.let {
                        it.hideKeyboard()
                        it.showSnackbar(context.getString(R.string.no_net_connection), context.getString(android.R.string.ok))
                    }
                }
            } else {
                searchBarTvNote.visible()
                listView.gone()
                noResultCardView.gone()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        try {
            super.onResume()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun showPlaces(places: ArrayList<Place>) {
        try {
            mPlaceArrayList = places
            listView.adapter = PlaceAdapter(context, mPlaceArrayList, this)
            listView.visible()
            searchBarTvNote.gone()
            noResultCardView.gone()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun placeSelected(place: Place?) {
        try {
            place?.let {
                mPlaceSelected = it
                mPresenter?.onPlaceSelected(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun errorPlaces(e: Throwable) {
        context.toast("Some error occurred")
    }

    override fun showNoResultFoundView() {
        try {
            mPlaceArrayList = ArrayList<Place>()
            listView.gone()
            searchBarTvNote.gone()
            noResultCardView.visible()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun setPlaceLatLng(place: Place) {
        try {
//            mPlaceSelected?.lat = place.lat
//            mPlaceSelected?.lon = place.lon
//            mPlaceSelected?.timeZone = place.timeZone
            mParentLayout.hideKeyboard()
            mPlaceSelected?.let { mListener?.onPlaceSelected(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnCitySearchFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context?.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onPause() {
        try {
            super.onPause()
            mParentLayout.hideKeyboard()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onDestroyView() {
        try {
            super.onDestroyView()
            mPresenter?.detachView()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    interface OnCitySearchFragmentInteractionListener {
        fun onPlaceSelected(place: Place)
    }

    companion object {

        @JvmField
        val TAG = "CitySearchView"

        @JvmField
        val COUNTRY_CODE = "countryCode"

        @JvmStatic
        fun newInstance(countryCode: String): CitySearchFragment {
            val fragment = CitySearchFragment()
            fragment.arguments = Bundler.start().put(COUNTRY_CODE, countryCode).end()
            return fragment
        }
    }
}
