package com.example.astrodashalib.view.modules.pricingPlan

import com.example.astrodashalib.Constant
import com.example.astrodashalib.data.models.PaymentDetail
import com.example.astrodashalib.data.models.PaytmHashRequestBody
import com.example.astrodashalib.data.models.PaytmOrderStatusBody
import com.example.astrodashalib.data.models.PaytmPaymentDetails
import com.example.astrodashalib.provider.rest.RestProvider
import com.example.astrodashalib.view.modules.chat.ChatDetailContract
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

class PricingPlanPresenter : PricingPlanContract.Presenter {

    var mView: PricingPlanContract.View? = null
    val mCompositeSubscription = CompositeSubscription()

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

    override fun postPaymentDetails(paymentDetail: PaymentDetail, purchaseTimestamp: Long, userId: String) {
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

    override fun attachView(view: PricingPlanContract.View) {
        mView = view
    }

    override fun detachView() {
        mView = null
        mCompositeSubscription.clear()
    }
}