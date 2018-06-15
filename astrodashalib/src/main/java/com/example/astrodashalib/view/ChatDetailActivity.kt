package com.example.astrodashalib.view

import android.app.FragmentTransaction
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.astrodashalib.R
import com.example.astrodashalib.model.Place
import com.example.astrodashalib.toggleDrawer
import com.example.astrodashalib.view.modules.birthForm.BirthDetailFragment
import com.example.astrodashalib.view.modules.citySearch.CitySearchFragment
import kotlinx.android.synthetic.main.activity_chat_detail.*
import kotlinx.android.synthetic.main.app_bar_chat_detail.*


class ChatDetailActivity : AppCompatActivity(), MainMenuFragment.OnFragmentInteractionListener, TutorialFragment.OnFragmentInteractionListener, SampleQuestionListFragment.OnFragmentInteractionListener, BirthDetailFragment.OnBirthDetailFragmentInteractionListener, CitySearchFragment.OnCitySearchFragmentInteractionListener {

    var mBirthDetailFragment: BirthDetailFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_detail)
        setSupportActionBar(toolbar)

        val toggle = object : ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            override fun onDrawerClosed(drawerView: View?) {
                super.onDrawerClosed(drawerView)
                when (drawerView?.id) {
                    R.id.leftMainNavigationView -> {
                        if (supportFragmentManager.findFragmentByTag(MainMenuFragment.TAG) == null)
                            supportFragmentManager.beginTransaction()
                                    .replace(R.id.fragmentContainer1, MainMenuFragment.newInstance(), MainMenuFragment.TAG)
                                    .commit()
                    }

                    R.id.rightNavigationView -> {
                        if (supportFragmentManager.backStackEntryCount > 0)
                            supportFragmentManager.popBackStack()
                    }
                }

            }

            override fun onDrawerSlide(drawerView: View?, slideOffset: Float) {
                super.onDrawerSlide(drawerView, 0f)
            }

            override fun onDrawerOpened(drawerView: View?) {
                super.onDrawerOpened(drawerView)
            }
        }
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainer1, MainMenuFragment.newInstance(), MainMenuFragment.TAG)
                    .commit()

            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainer2, BirthDetailFragment.newInstance(), BirthDetailFragment.TAG)
                    .commit()
        }


        profileIv.setOnClickListener {
            drawerLayout.toggleDrawer(GravityCompat.END)
        }
    }

    override fun onFaqClick() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer1, SampleQuestionListFragment.newInstance(), SampleQuestionListFragment.TAG)
                .commit()
    }

    override fun onTutsClick() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer1, TutorialFragment.newInstance(), TutorialFragment.TAG)
                .commit()
    }

    override fun onBirthViewAttach(fragment: Fragment) {
        mBirthDetailFragment = fragment as BirthDetailFragment
    }

    override fun onCityViewClick(countryCode: String) {
        supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer2, CitySearchFragment.newInstance(countryCode), CitySearchFragment.TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(CitySearchFragment.TAG)
                .commit()
    }

    override fun closeDrawer() {
        onBackPressed()
    }

    override fun onBackPressed() {
        when {
            drawerLayout.isDrawerOpen(GravityCompat.START) -> drawerLayout.closeDrawer(GravityCompat.START)
            drawerLayout.isDrawerOpen(GravityCompat.END) -> {

                if (supportFragmentManager.backStackEntryCount > 0)
                    supportFragmentManager.popBackStack()
                else
                    drawerLayout.closeDrawer(GravityCompat.END)
            }
            else -> super.onBackPressed()
        }
    }


    override fun onPlaceSelected(place: Place) {
        if (supportFragmentManager.backStackEntryCount > 0)
            supportFragmentManager.popBackStack()
        mBirthDetailFragment?.setCity(place)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            ACCOUNT_KIT_PHONE_REQUEST_CODE -> mBirthDetailFragment?.onActivityResult(requestCode, resultCode, data)
            else -> super.onActivityResult(requestCode, resultCode, data)
        }

    }

    companion object {
        @JvmField
        val ACCOUNT_KIT_PHONE_REQUEST_CODE = 5431

        @JvmStatic
        fun createIntent(context: Context?) : Intent{
            return Intent(context,ChatDetailActivity::class.java)
        }
    }

}
