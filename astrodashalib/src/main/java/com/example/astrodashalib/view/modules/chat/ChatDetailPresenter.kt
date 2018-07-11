package com.example.astrodashalib.view.modules.chat

import android.util.Log
import com.example.astrodashalib.Constant
import com.example.astrodashalib.data.models.PaymentDetail
import com.example.astrodashalib.data.models.PaytmHashRequestBody
import com.example.astrodashalib.data.models.PaytmOrderStatusBody
import com.example.astrodashalib.data.models.PaytmPaymentDetails
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

    override fun getPaytmHash(paytmHashRequestBody: PaytmHashRequestBody) {
        mView?.showLoader()
        mCompositeSubscription.add(RestProvider.getPaymentService().getPaytmHash(Constant.KEY_VALUE, paytmHashRequestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ paytmHashResponse ->
                    mView?.let {
                        it.dismissLoader()
                        it.startPaytmSDK(paytmHashRequestBody, paytmHashResponse)
                    }

                }, {
                    mView?.let {
                        it.dismissLoader()
                        it.onHashError()
                    }
                }))
    }

    override fun getPaytmOrderStatus(paytmOrderStatusBody: PaytmOrderStatusBody, paytmPaymentDetails: PaytmPaymentDetails) {
        try {
            mView?.showLoader()
            mCompositeSubscription.add(RestProvider.getPaymentService().getPaytmOrderStatus(Constant.KEY_VALUE, paytmOrderStatusBody)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ paytmOrderStatusDetail ->
                        mView?.let {
                            it.dismissLoader()

                            when {
                                (paytmOrderStatusDetail.sTATUS == "TXN_SUCCESS" || paytmOrderStatusDetail.rESPMSG == "Empty response message") -> {
                                    it.postPaytmPaymentDetails(paytmPaymentDetails)
//                                    it.logPaytmOrderError("Empty paytm order body")
                                }
                                else -> {
                                    it.onPaytmOrderStatusError(paytmOrderStatusDetail.rESPMSG)
//                                    it.logPaytmOrderError(paytmOrderStatusDetail.rESPMSG)
                                }
                            }
                        }

                    }, { error ->
                        try {
                            mView?.let {
                                it.dismissLoader()
//                                it.logPaytmOrderError(error.localizedMessage)
                                it.postPaytmPaymentDetails(paytmPaymentDetails)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun postPaymentDetails(paymentDetail: PaymentDetail, purchaseTimestamp: Long, userId:String) {
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