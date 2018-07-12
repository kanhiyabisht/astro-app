package com.example.astrodashalib.view.modules.chat

import com.example.astrodashalib.data.models.*
import com.example.astrodashalib.model.CurrentAntardashaFalRequestBody
import com.example.astrodashalib.view.base.BasePresenter
import com.example.astrodashalib.view.base.BaseView

/**
 * Created by himanshu on 04/10/17.
 */
interface ChatDetailContract {
    interface View : BaseView {
        fun showLoader()
        fun dismissLoader()
        fun startChatServices()
        fun onAntarDashaTxtSuccess(antarDashaTxt: String)
        fun onAntarDashaTxtError()
        fun onHashError()
        fun onCrmUserError()
        fun setCrmId(id:String)
        fun postPaytmPaymentDetails(paytmPaymentDetails: PaytmPaymentDetails)
        fun onPaytmOrderStatusError(message: String)
        fun startPaytmSDK(paytmHashRequestBody: PaytmHashRequestBody, paytmHashResponse: PaytmHashResponse)
        fun onPaymentSuccess(paymentDetail: PaymentDetail, purchaseTimestamp: Long)
        fun onPaymentError(purchaseTimestamp: Long)
        fun onUserUpdateSuccess(userModel: UserModel)
        fun onUserUpdateError()
    }

    interface Presenter : BasePresenter<View>{
        fun getPaytmHash(paytmHashRequestBody: PaytmHashRequestBody)
        fun getPaytmOrderStatus(paytmOrderStatusBody: PaytmOrderStatusBody, paytmPaymentDetails: PaytmPaymentDetails)
        fun getCrmUserId(userId:String)
        fun postPaymentDetails(paymentDetail: PaymentDetail, purchaseTimestamp: Long,userId:String)
        fun getCurrentAntardashaFalText(merchantId: String,currentAntardashaFalRequestBody: CurrentAntardashaFalRequestBody)
    }
}