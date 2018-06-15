package com.example.astrodashalib.view.modules.chat

import com.example.astrodashalib.data.models.*
import com.example.astrodashalib.view.base.BasePresenter
import com.example.astrodashalib.view.base.BaseView

/**
 * Created by himanshu on 04/10/17.
 */
interface ChatDetailContract {
    interface View : BaseView {
        fun showLoader()
        fun dismissLoader()
        fun startFayeService()
        fun onHashError()
        fun onCrmUserError()
        fun setCrmId(id:String)
        fun onPaymentSuccess(paymentDetail: PaymentDetail, purchaseTimestamp: Long)
        fun onPaymentError(purchaseTimestamp: Long)
        fun onUserUpdateSuccess(userModel: UserModel)
        fun onUserUpdateError()
    }

    interface Presenter : BasePresenter<View>{
        fun getCrmUserId(userId:String)
        fun postPaymentDetails(paymentDetail: PaymentDetail, purchaseTimestamp: Long,userId:String)
    }
}