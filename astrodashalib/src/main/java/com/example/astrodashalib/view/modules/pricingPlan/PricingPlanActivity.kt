package com.example.astrodashalib.view.modules.pricingPlan

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.example.astrodashalib.R
import com.example.astrodashalib.R.id.fab
import kotlinx.android.synthetic.main.activity_pricing_plan.*

class PricingPlanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pricing_plan)
        setSupportActionBar(toolbar)
    }
}
