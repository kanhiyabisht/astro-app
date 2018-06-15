package com.example.astrodashalib.view.modules.phoneVerification

import android.util.Log
import com.example.astrodashalib.view.modules.phoneVerification.PhoneVerificationContract
import com.example.astrodashalib.Constant
import com.example.astrodashalib.helper.getUserId
import com.example.astrodashalib.provider.rest.RestProvider
import com.example.astrodashalib.view.modules.birthForm.BirthDetailContract
import com.example.astrodashalib.view.modules.birthForm.BirthDetailPresenter
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

/**
 * Created by himanshu on 27/09/17.
 */
class PhoneVerificationPresenter : PhoneVerificationContract.Presenter {

    var mView: PhoneVerificationContract.View? = null
    val mCompositeSubscription = CompositeSubscription()

    override fun attachView(view: PhoneVerificationContract.View) {
        mView = view
    }

    override fun detachView() {
        mView = null
        mCompositeSubscription.clear()
    }

    override fun getUser(phoneNumber: String) {
        mView?.showLoader()
        mCompositeSubscription.add(RestProvider.getUserService().getUser(Constant.KEY_VALUE, phoneNumber, getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ model ->
                    try {
                        if (model?.id != null)
                            mView?.onUserSuccess(model)
                        else
                            mView?.onUserError()

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, { e ->
                    try {
                        mView?.onUserError()
                        Log.e(TAG, "onError: " + e.message)
                    } catch (e1: Exception) {
                        e1.printStackTrace()
                    }

                }))
    }

    companion object {
        val TAG = "PhonePresenter"
    }
}