package com.example.astrodashalib.view.modules.phoneVerification

import com.example.astrodashalib.data.models.UserModel
import com.example.astrodashalib.view.base.BasePresenter
import com.example.astrodashalib.view.base.BaseView

/**
 * Created by himanshu on 27/09/17.
 */
interface PhoneVerificationContract {

    interface View : BaseView {
        fun onUserError()
        fun onUserSuccess(userModel : UserModel)
        fun showLoader()
    }

    interface Presenter  : BasePresenter<View>{
        fun getUser(phoneNumber : String)
    }
}