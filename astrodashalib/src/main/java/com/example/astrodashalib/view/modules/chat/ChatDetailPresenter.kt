package com.example.astrodashalib.view.modules.chat

import android.util.Log
import com.example.astrodashalib.Constant
import com.example.astrodashalib.data.models.PaymentDetail
import com.example.astrodashalib.provider.rest.RestProvider
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

/**
 * Created by himanshu on 06/10/17.
 */
class ChatDetailPresenter : ChatDetailContract.Presenter {

    var mView: ChatDetailContract.View? = null
    val mCompositeSubscription = CompositeSubscription()


    override fun attachView(view: ChatDetailContract.View) {
        mView = view
    }

    override fun detachView() {
        mView = null
        mCompositeSubscription.clear()
    }

    override fun getCrmUserId(userId:String) {
        mView?.showLoader()
        mCompositeSubscription.add(RestProvider.getUserService().getCrmUserId(Constant.KEY_VALUE, userId, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ crmUser ->
                    try {
                        if (crmUser?.id != null) {

                            mView?.let {
                                it.setCrmId(crmUser.id)
                                it.dismissLoader()
                                it.startFayeService()
                            }
                        } else
                            mView?.let {
                                it.dismissLoader()
                                it.onCrmUserError()
                            }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, { e ->
                    try {
                        mView?.let {
                            it.dismissLoader()
                            it.onCrmUserError()
                        }
                        Log.e(ChatDetailPresenter.TAG, "onError: " + e.message)
                    } catch (e1: Exception) {
                        e1.printStackTrace()
                    }

                }))
    }


    override fun postPaymentDetails(paymentDetail: PaymentDetail, purchaseTimestamp: Long,userId:String) {
        try {
            mView?.showLoader()
            mCompositeSubscription.add(RestProvider.getPaymentService().postPaymentDetails(Constant.KEY_VALUE, paymentDetail, userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        mView?.onPaymentSuccess(paymentDetail, purchaseTimestamp)
                    }, {
                        mView?.onPaymentError(purchaseTimestamp)
                    }))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        @JvmField
        val TAG = "ChatDetailPresenter"
    }
}