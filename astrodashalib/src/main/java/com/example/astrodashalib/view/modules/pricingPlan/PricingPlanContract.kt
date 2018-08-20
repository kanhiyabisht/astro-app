package com.example.astrodashalib.view.modules.pricingPlan

import com.example.astrodashalib.data.models.*
import com.example.astrodashalib.view.base.BasePresenter
import com.example.astrodashalib.view.base.BaseView

interface PricingPlanContract {
    interface View : BaseView {
        fun showLoader()
        fun dismissLoader()
        fun onHashError()
        fun postPaytmPaymentDetails(paytmPaymentDetails: PaytmPaymentDetails)
        fun onPaytmOrderStatusError(message: String)
        fun startPaytmSDK(paytmHashRequestBody: PaytmHashRequestBody, paytmHashResponse: PaytmHashResponse)
        fun onPaymentSuccess(paymentDetail: PaymentDetail, purchaseTimestamp: Long)
        fun onPaymentError(purchaseTimestamp: Long)
    }

    interface Presenter : BasePresenter<View> {
        fun getPaytmHash(paytmHashRequestBody: PaytmHashRequestBody)
        fun getPaytmOrderStatus(paytmOrderStatusBody: PaytmOrderStatusBody, paytmPaymentDetails: PaytmPaymentDetails)
        fun postPaymentDetails(paymentDetail: PaymentDetail, purchaseTimestamp: Long,userId:String)
    }
}