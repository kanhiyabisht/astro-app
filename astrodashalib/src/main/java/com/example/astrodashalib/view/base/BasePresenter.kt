package com.example.astrodashalib.view.base

import com.example.astrodashalib.view.base.BaseView

/**
 * Created by himanshu on 25/09/17.
 */
interface BasePresenter<V : BaseView> {
    fun attachView(view: V)

    fun detachView()
}